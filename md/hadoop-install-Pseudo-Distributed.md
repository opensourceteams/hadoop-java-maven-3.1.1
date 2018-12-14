# Hadoop 3.1.1伪分布式模式安装

## 前置条件
- jdk.1.8.0_191 已安装
- linux 系统(本文选用的centos 7 系统已完装好)

## 技能标签
- 安装完成Hadoop3.1.1伪分布式环境
- 可以进行Hadoop技术开发(HDFS,MapReduce)
- 进行浏览界面管理HDFS和资源管理


##安装步骤

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

```
bin/hadoop
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
 hadoop-daemon.sh start namenode
 hadoop-daemon.sh start datanode
 hadoop-daemon.sh stop namenode
 hadoop-daemon.sh stop datanode
  
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
###启动YARN
- 启动 ResourceManager daemon 和 NodeManager daemon
```
sbin/start-yarn.sh
```

###访问资源管理器
-  ResourceManager: http://localhost:8088/

###停止YARN
- 停止 ResourceManager daemon 和 NodeManager daemon
```
sbin/stop-yarn.sh
```




end
