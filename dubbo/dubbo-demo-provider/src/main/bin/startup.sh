#!/bin/bash

# application's name
APP_NAME=dubbo-demo-provider
LOG_DIR=/opt/logs
HEAP_PATH=${LOG_DIR}/${APP_NAME}/jvm/heap.hprof
GC_LOG_PATH=${LOG_DIR}/${APP_NAME}/jvm/gc.log
JMX_PROT=22222

JAVA_OPTS="-server -Xms1024m -Xmx1024m -Xmn384m -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=384m
 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${HEAP_PATH}
 -XX:-OmitStackTraceInFastThrow -XX:+PrintGCDetails
 -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -verbose:gc -Xloggc:${GC_LOG_PATH}
 -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M
 -Djava.lang.Integer.IntegerCache.high=1024
 -Dfile.encoding=UTF-8
 -Djava.net.preferIPv4Stack=true
 -Dcom.sun.management.jmxremote
 -Dcom.sun.management.jmxremote.ssl=false
 -Dcom.sun.management.jmxremote.authenticate=false
 -Dcom.sun.management.jmxremote.port=${JMX_PROT}
 -Dapp.id=${APP_NAME} "

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