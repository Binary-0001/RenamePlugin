package com.xyz.sdk.plugin;

import org.dom4j.Document;

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
    private String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String[] packageParts;

    ExtensionProcess(Processor.DocumentContainer container) {
        super(null, null);
        mContainer = container;
        toReplace = new HashMap<>();
        classReplace = new HashMap<>();
        packageParts = new String[]{RandomStringUtils.random(7, chars), RandomStringUtils.random(7, chars), RandomStringUtils.random(7, chars)};
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
            toReplace.put(pkgName, replacePkg(pkgName));
            String pathName = pkgName.replace('.', '/');
            classReplace.put(pathName, replacePkg(pathName));
        }

        boolean replaced = false;
        for (Map.Entry<String, String> replace : toReplace.entrySet()) {
            String name = replace.getKey();
            xml = xml.replace(name, replace.getValue());
            replaced = true;
        }

        xml = xml.replace("\r\n", "\n");

        if (replaced) {
            try (StringReader reader = new StringReader(xml)) {
                document = mContainer.reader.read(reader);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
            mContainer.document = document;
        }
    }

    public static String replacePkg(String pkgName) {
        return pkgName.replace("tencent", packageParts[0])
                .replace("qqpim", packageParts[1])
                .replace("discovery", packageParts[2]);
    }
}
