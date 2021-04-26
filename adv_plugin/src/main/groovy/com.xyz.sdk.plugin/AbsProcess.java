package com.xyz.sdk.plugin;

import org.dom4j.Element;
import org.dom4j.Namespace;
import org.gradle.api.logging.Logger;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2018-05-14
 */
abstract class AbsProcess {
    static final String ANDROID_PREFIX = "android:";

    Namespace mNamespace;
    Element mParent;

    AbsProcess(Namespace namespace, Element parent) {
        mNamespace = namespace;
        mParent = parent;
    }

    final void process() {
        onHandle();
    }


    protected abstract void onHandle();
}
