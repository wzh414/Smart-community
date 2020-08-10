#!/usr/bin/env bash
# 加了 --rm 参数，当容器 stop 或 docker重启时，容器就会被删除。
docker run -d -p 443:443 --name community --rm registry.cn-hangzhou.aliyuncs.com/weiweiwi/web
