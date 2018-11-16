#!/bin/bash

# application's name
APP_NAME=dubbo-demo-provider
heap_path=/opt/logs/${APP_NAME}/jvm/heap.hprof
gc_log_path=/opt/logs/${APP_NAME}/jvm/gc.$$.log

JAVA_OPTS="-server -Xms1024m -Xmx1024m -Xmn384m -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=384m
 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${heap_path}
 -XX:-OmitStackTraceInFastThrow -XX:+PrintGCDetails
 -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -Xloggc:${gc_log_path} "

function pid() {
    ps aux | grep java | grep $1 | grep -v grep | awk '{print $2}'
}

PID=`pid ${APP_NAME}`

if [ ! -z "$PID" ]; then
    echo "The application is runing, please stop first! PID is ${PID}"
    exit 1
fi

#PATH=`pwd`
APP_JAR=${APP_NAME}.jar

echo "${APP_NAME} is starting"
nohup java ${JAVA_OPTS} -jar ${APP_JAR} >/dev/null 2>&1 &

sleep 3

echo "Done"