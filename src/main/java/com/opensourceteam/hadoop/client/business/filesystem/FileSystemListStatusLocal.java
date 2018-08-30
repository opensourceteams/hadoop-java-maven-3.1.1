package com.opensourceteam.hadoop.client.business.filesystem;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;

/**
 * 本地运行
 *  hadoop  jar  /root/temp/hadoop-application-wordcount-1.0-SNAPSHOT.jar  com.opensourceteam.hadoop.client.business.filesystem.FileSystemCat  data/worldcount/input/a.txt
 *  创建多级目录
 */
public class FileSystemListStatusLocal {

    public static void main(String[] args) throws IOException {

        String dest = "";
        if(args == null || args.length ==0){
            dest = "hdfs://c0.com:8020/user/Administrator/data/worldcount/input2";
        }else{
             dest = args[0];
        }
        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.get(URI.create(dest),configuration);
        FileStatus[]  fileStatuses = fileSystem.listStatus(new Path(dest));
        Path[] paths = FileUtil.stat2Paths(fileStatuses);
        for(Path  path : paths){
            System.out.println(path);
        }
    }

}
