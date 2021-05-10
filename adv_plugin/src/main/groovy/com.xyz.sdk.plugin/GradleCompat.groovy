package com.xyz.sdk.plugin

import com.android.build.gradle.tasks.ManifestProcessorTask

class GradleCompat {

    public static final String ANDROID_MANIFEST = "AndroidManifest.xml"

    static File getManifestOutputFile(ManifestProcessorTask task) {
        try {
            return get(task.getManifestOutputFile())
        } catch (Exception e) {
            try {
                return new File(task.getManifestOutputDirectory().get().getAsFile(), ANDROID_MANIFEST)
            } catch (Exception e2) {
                try {
                    return task.getManifestOutputFile()
                } catch (Exception e3) {
                    try {
                        return new File(task.getManifestOutputDirectory(), ANDROID_MANIFEST)
                    } catch (Exception e4) {
                        Set<File> files = task.getOutputs().getPreviousOutputFiles();
                        for (int i = 0; i < files.size(); i++) {
                            if (files.getAt(i).getPath().contains(ANDROID_MANIFEST)) {
                                return new File(files.getAt(i).getPath())
                            }
                        }
                    }
                }
            }
        }
    }

    def static get(def provider) {
        return provider == null ? null : provider.get()
    }
}