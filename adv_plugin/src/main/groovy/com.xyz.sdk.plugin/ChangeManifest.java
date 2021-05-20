package com.xyz.sdk.plugin;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.tasks.ManifestProcessorTask;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.ProjectConfigurationException;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskInputs;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import groovy.util.Node;
import groovy.util.NodeList;
import groovy.util.XmlParser;
import groovy.xml.Namespace;
import groovy.xml.XmlUtil;

/**
 * @author guoliang.zhang
 * @Date on 2021/4/23
 * @Description
 */
public class ChangeManifest {

    public static void apply(Project project) {
        if (!project.getPlugins().hasPlugin("com.android.application")) {
            Throwable e = new RuntimeException();
            throw new ProjectConfigurationException("Plugin requires the 'com.android.application' plugin to be configured.", e);
        }
        project.afterEvaluate(project1 -> {
            // 找到 android 的  AppExtension
            AppExtension appExtension = project1.getExtensions().getByType(AppExtension.class);
            // 遍历所有的 构建变体
            appExtension.getApplicationVariants().forEach(variant -> {
                boolean debuggable = variant.getBuildType().isDebuggable();
                String name = variant.getName();
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                // 找到 形如 processReleaseManifest 的 task
                ManifestProcessorTask manifestTask = (ManifestProcessorTask) project1.getTasks().getByName("process" + name + "Manifest");
                manifestTask.doLast(new Action<Task>() {
                    @Override
                    public void execute(Task task) {
                        try {
                            System.out.println("--------------- RenamePlugin EditManifest: start ---------------");
                            ManifestHelper helper = new ManifestHelper(manifestTask);
                            helper.replace();
                            System.out.println("--------------- RenamePlugin EditManifest: end ---------------");
                        } catch (Exception e) {
                            throw new RuntimeException("处理 Manifest 失败", e.getCause());
                        }
                    }
                });
            });
        });
    }
}
