package com.opensourceteam.hadoop.client.business.filesystem;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import static java.lang.System.out;

/**
 *  hadoop  jar  /root/temp/hadoop-application-wordcount-1.0-SNAPSHOT.jar  com.opensourceteam.hadoop.client.business.filesystem.FileSystemCat  data/worldcount/input/a.txt
 *  控制台输出文件内容
 */
public class FileSystemCat {

    public static void main(String[] args) throws IOException {
        String url = args[0];
        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.get(URI.create(url),configuration);
        InputStream is = null;
        try{
            is = fileSystem.open(new Path(url));
            IOUtils.copy(is, out);
        }finally {

        }

    }

}
