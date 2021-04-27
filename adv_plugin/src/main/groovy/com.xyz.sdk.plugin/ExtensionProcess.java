package com.xyz.sdk.plugin;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.gradle.api.logging.Logger;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2019-08-28
 */
public class ExtensionProcess extends AbsProcess {

    private Map<String, String> toReplace;
    private static Map<String, String> classReplace;
    private Processor.DocumentContainer mContainer;
    private String suffix;

    ExtensionProcess(String suffix, Processor.DocumentContainer container) {
        super(null, null);
        mContainer = container;
        toReplace = new HashMap<>();
        classReplace = new HashMap<>();
        this.suffix = suffix;
    }

    public static Map<String, String> getToReplace() {
        return classReplace;
    }

    @Override
    protected void onHandle() {

        Document document = mContainer.document;

        String xml = document.asXML();
        String pkg = "com.tencent.qqpim.discovery.[A-Za-z0-9.]+Activity";
        Pattern pattern = Pattern.compile(pkg);
        Matcher matcher = pattern.matcher(xml);
        while (matcher.find()) {
            String pkgName = matcher.group();
            toReplace.put(pkgName, pkgName + suffix);
            classReplace.put(pkgName.replace('.', '/'), pkgName.replace('.', '/') + suffix);
            System.out.println(pkgName.replace('.', '/')+" >>>>>>> "+pkgName.replace('.', '/') + suffix);
        }

        boolean replaced = false;
        for (Map.Entry<String, String> replace : toReplace.entrySet()) {
            String name = replace.getKey();
            xml = xml.replace(name, replace.getValue());
            replaced = true;
        }

        if (replaced) {
            try (StringReader reader = new StringReader(xml)) {
                document = mContainer.reader.read(reader);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
            mContainer.document = document;
        }
    }
}
