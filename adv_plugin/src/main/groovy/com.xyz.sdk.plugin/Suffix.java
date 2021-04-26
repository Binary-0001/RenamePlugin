package com.xyz.sdk.plugin;

/**
 * @author guoliang.zhang
 * @Date on 2021/4/23
 * @Description
 */
public class Suffix {
    private String name;
    private String advSdkAarName;

    public String getName() {
        return name;
    }

    public void suffix(String name) {
        this.name = name;
    }

    public String getAdvSdkAarName() {
        return advSdkAarName;
    }

    public void advSdkAarName(String advSdkAarName) {
        this.advSdkAarName = advSdkAarName;
    }
}
