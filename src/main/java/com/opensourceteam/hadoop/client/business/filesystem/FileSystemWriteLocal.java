package com.opensourceteam.hadoop.client.business.filesystem;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Progressable;

import java.io.*;
import java.net.URI;

import static java.lang.System.out;

/**
 * 本地运行
 *  hadoop  jar  /root/temp/hadoop-application-wordcount-1.0-SNAPSHOT.jar  com.opensourceteam.hadoop.client.business.filesystem.FileSystemCat  data/worldcount/input/a.txt
 *  文件上传
 */
public class FileSystemWriteLocal {

    public static void main(String[] args) throws IOException {

        String localSrc = "";
        String dest = "";
        if(args == null || args.length ==0){
            dest = "hdfs://c0.com:8020/user/Administrator/data/worldcount/input/d.txt";
            localSrc = "E:\\tmp\\a.txt";
        }else{
             dest = args[0];
            localSrc = args[0];
        }
        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.get(URI.create(dest),configuration);
        InputStream is = new BufferedInputStream(new FileInputStream(localSrc));
        OutputStream out = fileSystem.create(new Path(dest), new Progressable() {
            @Override
            public void progress() {
                System.out.println(".");
            }
        });
        IOUtils.copy(is, out);

    }

}
