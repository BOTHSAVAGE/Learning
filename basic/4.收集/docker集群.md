```
docker  pull  redis:5.0.5
```

```
docker create --name redis-node1 -v /data/redis-data/node1:/data -p 7000:6379 redis:5.0.5 --cluster-enabled yes --cluster-config-file nodes-node-1.conf

docker create --name redis-node2 -v /data/redis-data/node2:/data -p 7001:6379 redis:5.0.5 --cluster-enabled yes --cluster-config-file nodes-node-2.conf

docker create --name redis-node3 -v /data/redis-data/node3:/data -p 7002:6379 redis:5.0.5 --cluster-enabled yes --cluster-config-file nodes-node-3.conf
```

```
# 这里以进入 node1 为例
docker exec -it redis-node1 /bin/bash

# 接着执行组建集群命令（请根据自己的ip信息进行拼接）
redis-cli --cluster create 172.17.0.2:6379  172.17.0.3:6379  172.17.0.4:6379 --cluster-replicas 0
```

```
redis-cli -c
```





```
docker create --name redis-node1 --net host -v C:\Users\T440p\Desktop\redisData/node1:/data redis:5.0.5 --cluster-enabled yes --cluster-config-file nodes-node-1.conf --port 7000

docker create --name redis-node2 --net host -v C:\Users\T440p\Desktop\redisData/node2:/data redis:5.0.5 --cluster-enabled yes --cluster-config-file nodes-node-2.conf --port 7001

docker create --name redis-node3 --net host -v C:\Users\T440p\Desktop\redisData/node3:/data redis:5.0.5 --cluster-enabled yes --cluster-config-file nodes-node-3.conf --port 7002
```



```
# 启动命令
docker start redis-node1 redis-node2 redis-node3

# 进入某一个容器
docker exec -it redis-node1 /bin/bash
```





```
redis-cli --cluster create 127.0.0.1:7000  127.0.0.1:7001  127.0.0.1:7002 --cluster-replicas 0

```



```
 #这个是进入某个容器后执行的命令
 redis-cli -h 127.0.0.1 -p 7000
```





## 参考

https://www.cnblogs.com/niceyoo/p/13011626.htmlsw