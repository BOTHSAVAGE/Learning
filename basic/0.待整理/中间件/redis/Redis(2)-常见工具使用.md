---
title: Redis(2)-常见工具使用
date: 2020-12-30  15:14:11
categories:
- [中间件,Redis]
tags:
- redis
- 工具
---

## 摘要

## 工具介绍

### Redis Desktop Manager

### Redis-benchmark

> 压力测试工具

#### 可选参数

![image-20210113102943652](https://gitee.com/BothSavage/PicGo/raw/master/image-20210113102943652.png)

#### 测试

```bash
# 测试：100个并发连接 100000请求 
redis-benchmark -h localhost -p 6379 -c 100 -n 100000
```

#### 解析

![image-20210113103100957](https://gitee.com/BothSavage/PicGo/raw/master/image-20210113103100957.png)

## 参考

