# Hadoop 3.1.1伪分布式模式安装

## 更多资源
github: https://github.com/opensourceteams/hadoop-java-maven-3.1.1

## 视频
- Hadoop 3.1.1伪分布式模式安装(bilibili视频) : https://www.bilibili.com/video/av38149957/
- Hadoop 3.1.1伪分布式模式安装(youtube视频) : https://youtu.be/plVkuyJSNF8

<iframe width="800" height="500"  src="//player.bilibili.com/player.html?aid=38149957&cid=67061213&page=1" scrolling="no" border="0" frameborder="no" framespacing="0" allowfullscreen="true"> </iframe>

## 前置条件
- jdk.1.8.0_191 已安装
- linux 系统(本文选用的centos 7 系统已完装好)

## 技能标签
- 学会安装Hadoop3.1.1版本的伪分布式模式
- 可以进行Hadoop技术开发(包括HDFS,MapReduce等)
- HDFS启动停止命令，yarn启动停止命令
- 官网自带WorldCount示例运行
- 进行管理界面管理NamenNode管理和ResourceManager管理
- 对Hadoop集群环境操作(一台机器也可以做集群，只是节点只有一个，很多功能都可以操作)


## 安装步骤

### 安装ssh
- 一般安装系统后都已自带ssh服务，就可以跳过,直接在终端执行ssh命令，有这个命令就可以
- 如果没有需要安装如下服务
```
yum install ssh
yum install pdsh

```

### 下载Hadoop安装包
- 官网下载地址:https://hadoop.apache.org/releases.html
- 本文下载版本: hadoop-3.1.1.tar.gz
- http://apache.01link.hk/hadoop/common/hadoop-3.1.1/hadoop-3.1.1.tar.gz 
- 解压压缩包

```aidl
tar -zxvf /hadoop-3.1.1.tar.gz  -C /opt/module/bigdata
```

## 配置

### hadoop-env.sh
- 编辑etc/hadoop/hadoop-env.sh
- 调置JAVA_HOME环境变量

```
# set to the root of your Java installation
  export JAVA_HOME=/opt/module/jdk/jdk1.8.0_191
```

### 执行命令 Hadoop
- 确认hadoop命令是否可以正常执行
- 查看当前版本命令
```
bin/hadoop version
```
## 独立模式
- 本地模式

### 执行官方自带示例

```
mkdir input
  $ cp etc/hadoop/*.xml input
  $ bin/hadoop jar share/hadoop/mapreduce/hadoop-mapreduce-examples-3.1.1.jar grep input output 'dfs[a-z.]+'
  $ cat output/*

```

## 伪分布式模式

### 配置环境变量
- 配置在本地用户下 ~/.bashrc 

```
export HADOOP_HOME=/opt/module/bigdata/hadoop-3.1.1
export PATH=$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$PATH

```

### 配置core-site.xml
- 配置文件 etc/hadoop/core-site.xml

```
<configuration>
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://localhost:9000</value>
    </property>
</configuration>
```

### 配置hdfs-site.xml
- etc/hadoop/hdfs-site.xml:
```
<configuration>
    <property>
        <name>dfs.replication</name>
        <value>1</value>
    </property>
</configuration>

```

### 配置 ssh免密登录
- 验证是否已经配置 ssh

```
ssh localhost
```
- 如果需要输入密码验证，则执行以下
```
ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa
  $ cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
  $ chmod 0600 ~/.ssh/authorized_keys
```
- 也可以执行以下

```
ssh-keygen 
ssh-copy-id 远程ip地址
```

### 格式化namenode

```
 bin/hdfs namenode -format
```

### 启动namenode和datanode
```
sbin/start-dfs.sh
```

- 可配置日志输出目录
```
$HADOOP_LOG_DIR directory (defaults to $HADOOP_HOME/logs).
```
### 访问namenode
- NameNode - http://localhost:9870/

### HDFS上新建目录
```
bin/hdfs dfs -mkdir /user
$ bin/hdfs dfs -mkdir /user/<username>
```
### 上传本机文件到HDFS上
```
bin/hdfs dfs -mkdir input
$ bin/hdfs dfs -put etc/hadoop/*.xml input
```

### 运行示例
```
bin/hadoop jar share/hadoop/mapreduce/hadoop-mapreduce-examples-3.1.1.jar grep input output 'dfs[a-z.]+'
```

### 查看示例输出结果(先下到本地再看)
```
 bin/hdfs dfs -get output output
 $ cat output/*
```
### 查看HDFS上的文件内容
```
 bin/hdfs dfs -cat output/*
```

### 停止namemode和datanode
```
sbin/stop-dfs.sh
```

### hadoop-daemon.sh命令
```
 hdfs --daemon start namenode
 hdfs --daemon start datanode
 hdfs --daemon stop namenode
 hdfs --daemon stop datanode

  
```

### HDFS命令操作

- 在HDFS上新建目录
```aidl
hdfs dfs -mkdir -p /home/liuwen/data
```

- 上传本地文件到HDFS
```aidl
hdfs dfs -put /opt/temp/a.txt  /home/liuwen/data
```

- 查看HDFS文件
```aidl
hdfs dfs -text  /home/liuwen/data/a.txt
```


### YARN 配置伪分布式模式
#### 配置文件mapred-site.xml
- etc/hadoop/mapred-site.xml
```
<configuration>
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
</configuration>

<configuration>
    <property>
        <name>mapreduce.application.classpath</name>
        <value>$HADOOP_MAPRED_HOME/share/hadoop/mapreduce/*:$HADOOP_MAPRED_HOME/share/hadoop/mapreduce/lib/*</value>
    </property>
</configuration>

```
#### 配置文件mapred-site.xml
- etc/hadoop/yarn-site.xml:
```
<configuration>
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
    <property>
        <name>yarn.nodemanager.env-whitelist</name>
        <value>JAVA_HOME,HADOOP_COMMON_HOME,HADOOP_HDFS_HOME,HADOOP_CONF_DIR,CLASSPATH_PREPEND_DISTCACHE,HADOOP_YARN_HOME,HADOOP_MAPRED_HOME</value>
    </property>
</configuration>

```
### 启动YARN
- 启动 ResourceManager daemon 和 NodeManager daemon
```
sbin/start-yarn.sh
```

### 访问资源管理器
-  ResourceManager: http://localhost:8088/

###停止YARN
- 停止 ResourceManager daemon 和 NodeManager daemon
```
sbin/stop-yarn.sh
```


### WorldCount官网示例运行
- 配置环境变量直接运行 hadoop命令
- worldcount标签，examples自带运行对应的程序
- 输入数据源
- 输出数据源

```

hadoop jar $HADOOP_HOME/share/hadoop/mapreduce/hadoop-mapreduce-examples-3.1.1.jar wordcount  /opt/data/a.txt  /opt/temp/output/output_2


```



end

