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
                    return new File(task.getManifestOutputDirectory(), ANDROID_MANIFEST)
                }
            }
        }
    }

    def static get(def provider) {
        return provider == null ? null : provider.get()
    }
}