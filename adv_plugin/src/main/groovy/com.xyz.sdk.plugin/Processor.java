package com.xyz.sdk.plugin;

import com.android.build.gradle.tasks.ManifestProcessorTask;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputFile;

import java.io.File;
import java.io.FileWriter;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2018-04-13
 */
public class Processor {

    @InputFile
    private File mManifest;
    @OutputFile
    private File mOutXml;

    private boolean mDebuggable;
    private String suffix;

    public Processor(String suffix, ManifestProcessorTask task, boolean debuggable) throws Throwable {
        mDebuggable = debuggable;
        mManifest = GradleCompat.getManifestOutputFile(task);
        mOutXml = new File(mManifest.getParentFile(), "EditAndroidManifest.xml");
        this.suffix = suffix;
    }

    public void run() throws Throwable {
        System.out.println("EditManifest: start.");
        if (mOutXml.exists()) {
            mOutXml.delete();
        }
        // 使用 dom4j 处理 xml
        SAXReader reader = new SAXReader();
        Document document = reader.read(mManifest);

        DocumentContainer container = new DocumentContainer(reader, document);

        new ExtensionProcess(suffix,container).process();

        // 输出到文件
        FileWriter out = new FileWriter(mOutXml);
        container.document.write(out);
        out.flush();
        out.close();
        File manifestTmp = new File(mManifest.getAbsolutePath() + ".tmp");
        mManifest.renameTo(manifestTmp);
        if (mOutXml.renameTo(mManifest)) {
            manifestTmp.delete();
        } else {
            manifestTmp.renameTo(mManifest);
            mOutXml.delete();
        }
    }

    static class DocumentContainer {
        SAXReader reader;
        Document document;

        DocumentContainer(SAXReader reader, Document document) {
            this.reader = reader;
            this.document = document;
        }
    }

}
