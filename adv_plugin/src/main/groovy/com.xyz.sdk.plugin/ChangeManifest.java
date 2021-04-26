package com.xyz.sdk.plugin;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.tasks.ManifestProcessorTask;

import org.gradle.api.Project;
import org.gradle.api.ProjectConfigurationException;
import org.gradle.api.tasks.TaskInputs;

/**
 * @author guoliang.zhang
 * @Date on 2021/4/23
 * @Description
 */
public class ChangeManifest {

    public static void apply(Project project, Suffix suffix) {
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

                manifestTask.doLast(task -> {
                    Processor processor;
                    try {
                        processor = new Processor(suffix.getName(), manifestTask, debuggable);
                        processor.run();
                    } catch (Throwable e) {
                        throw new RuntimeException("处理 Manifest 失败", e.getCause());
                    }
                });
//                }
            });
        });
    }
}
