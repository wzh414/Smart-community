#!/usr/bin/env bash
#停止运行中的容器
docker stop community
#删除原本的镜像文件
docker rmi -f registry.cn-hangzhou.aliyuncs.com/weiweiwi/web:latest
#docker rmi -f registry.cn-hangzhou.aliyuncs.com/weiweiwi/web:0.0.1-SNAPSHOT

mvn clean source:jar package -DskipTests && \
cd web && \
mvn dockerfile:build && \
#mvn dockerfile:tag@tag-version && \
#mvn dockerfile:push@push-latest && \
#mvn dockerfile:push@push-version && \
sh ../run.sh