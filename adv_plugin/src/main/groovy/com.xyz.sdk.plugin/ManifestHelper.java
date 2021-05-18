package com.xyz.sdk.plugin;

import com.android.build.gradle.tasks.ManifestProcessorTask;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import groovy.util.Node;
import groovy.util.NodeList;
import groovy.util.XmlParser;
import groovy.xml.Namespace;
import groovy.xml.XmlUtil;

/**
 * @author guoliang.zhang
 * @Date on 2021/5/18
 * @Description
 */
public class ManifestHelper {
    private File manifest;
    private Map<String, String> toReplace;
    private static Map<String, String> classReplace;
    private String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String[] packageParts;

    public ManifestHelper(ManifestProcessorTask manifestTask) {
        manifest = GradleCompat.getManifestOutputFile(manifestTask);
        toReplace = new HashMap<>();
        classReplace = new HashMap<>();
        packageParts = new String[]{RandomStringUtils.random(7, chars), RandomStringUtils.random(7, chars), RandomStringUtils.random(7, chars)};
    }

    public static Map<String, String> getToReplace() {
        return classReplace;
    }


    public void replace() throws Exception{
        XmlParser xmlParser = new XmlParser();
        Node root = xmlParser.parse(manifest);
        NodeList application = (NodeList) root.get("application");
        NodeList activity = application.getAt("activity");
        Namespace android = new Namespace("http://schemas.android.com/apk/res/android", "android");
        for (Object o : activity) {
            Node node = (Node) o;
            String activityName = (String) node.attribute(android.get("name"));
            node.attributes().put(android.get("name"), replaceClass(activityName));
        }
        PrintWriter pw = new PrintWriter(manifest,"UTF-8");
        pw.write(XmlUtil.serialize(root));
        pw.close();
    }

    private String replaceClass(String activityName){
        String pkg = "com.tencent.qqpim.discovery.[A-Za-z0-9.]+Activity";
        Pattern pattern = Pattern.compile(pkg);
        Matcher matcher = pattern.matcher(activityName);

        while (matcher.find()) {
            String pkgName = matcher.group();
            String pathName = pkgName.replace('.', '/');
            classReplace.put(pathName, replacePkg(pathName));
            return replacePkg(pkgName);
        }
        return activityName;
    }
    public static String replacePkg(String pkgName) {
        return pkgName.replace("tencent", packageParts[0])
                .replace("qqpim", packageParts[1])
                .replace("discovery", packageParts[2]);
    }
}
