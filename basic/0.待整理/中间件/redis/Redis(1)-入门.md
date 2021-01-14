---
title: Redis(1)-入门
date: 2020-12-30  15:14:11
categories:
- [中间件,Redis]
tags:
- redis
- 入门
---

## 摘要

## 概念

> Remote Dictionary Server （远程字典服务）
>
> 开源，C编写，支持网络，基于内存，可持久化（aof，rdb），可集群，支持事务的KV数据库
>
> 主从同步技术：基于redis会周期性的把更新的数据写入磁盘或者把修改操作写入追加的记录文件
>
> Redis默认有16个数据库
>
> 通过 Redis哨兵（Sentinel）和自动 分区（Cluster）提供高可用性（high availability）

### 数据类型

* String
* Hashes
* Lists
* Sets
* Sorted Sets

### 命令

```bash
# 切换数据库
select 3 
# 查看DB大小 (当前节点,不是集群)
DBSIZE
#查看所有的keys (当前节点,不是集群)
keys *
#清除当前数据库
flushdb
#清除全部数据库
FLUSHALL
```

```bash
set key value 
EXISTS key
move key value
get key
#单位是s
EXPIRE key 1 
ttl key
type key
#追加字符串
APPEND key value 
#自增1（若不是Integer：ERR value is not an integer or out of range）
incr key
decr key 
#
incrby key number
decrby key number
#切片 
getrange key  0 3 
#指定位置替换
setrange key offset value
#没有就创建
setnx key value
#批量
mset k1 v1 k2 v2 k3 v3 
mget k1 k2 k3 
#msetnx 是一个原子性的操作，要么一起成功，要么一起失败！
msetnx k1 v1 k4 v4


set user:1 {name:zhangsan,age:3} # 设置一个user:1 对象 值为 json字符来保存一个对象
mset user:1:name zhangsan user:1:age 2
mget user:1:name user:1:age
#先get再set
getset db redis # 如果不存在值，则返回 nil

```



## 参考

[1].[遇见狂神说](https://www.baidu.com/link?url=NMnuylp18J8GoMbut3O1Ga_bBs8JLlPBYWM0Dwzc5tnSAaOzef6Rw2aJLuGZt0O1&wd=&eqid=a013c4fc0012d8f7000000025ffe591a)

