package com.binary.plugin;

import android.content.Context;
import android.content.Intent;

/**
 * @author guoliang.zhang
 * @Date on 2021/4/22
 * @Description
 */
@ClassRename
public class HookUtils {

    public static void createIntent(Context context, Intent intent) {
        intent.setClass(context, TestActivity.class);
    }
}
