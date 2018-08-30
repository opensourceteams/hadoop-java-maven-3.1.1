package com.opensourceteam.hadoop.client.business.wordcount.local;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapred.IFile;

public class ReadMapOutterFileTest {

    public static void main(String[] args) {

        String inputPath = "";
        String outerPath = "";
        if(args == null || args.length < 2){
            inputPath = "E:/temp/data/worldcount/input/a.txt";
            outerPath = "E:/temp/data/worldcount/output_mapreduce";
        }else{
            inputPath = args[0];
            outerPath = args[1];
        }

        Configuration conf = getConfigured();
        //IFile.Reader reader = new IFile.Reader(conf,);
    }

    static Configuration getConfigured(){
        Configuration conf = new Configuration();
        conf.set("mapreduce.framework.name", "local");// 集群的方式运行，非本地运行  yarn,local
        conf.set("mapreduce.app-submission.cross-platform", "true");
        conf.set("mapred.jar","E:\\n_02_大数据技术研究\\n_02_第二个阶段~系统技术学习\\bigdata\\hadoop\\n_51001_hadoop_filesystem\\target\\hadoop-application-wordcount-1.0-SNAPSHOT.jar");

        return conf;
    }
}
