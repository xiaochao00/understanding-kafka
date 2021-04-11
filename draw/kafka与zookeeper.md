zookeeper启动方式：

```shell
#在bin目录下
./zkServer.sh start
```

查看zookeeper的状态：

```
./zkCli.sh 
# 遍历目录
ls /
#得到节点的值
get /kafka/brokers/ids/0
```

kafka 某个broker的启动方式：

```shell
.\bin\window\kafka-server-start.sh .\config\server.properties
```

broker启动后可以在zookeeper上看到信息

```
get /kafka/brokers/ids/0
# 得到回应
{"listener_security_protocol_map":{"PLAINTEXT":"PLAINTEXT"},"endpoints":["PLAINTEXT://localhost:9092"],"jmx_port":-1,"features":{},"host":"localhost","timestamp":"1617894143073","port":9092,"version":5}
```

zkcli进入Kafka目录下

```shell
zkcli -server 127.0.0.1:2181/kafka
```

