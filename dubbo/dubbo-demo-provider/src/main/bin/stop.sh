#!/bin/bash

# application's name
APP_NAME=dubbo-demo-provider

PID=`ps aux | grep java | grep ${APP_NAME} | grep -v grep | awk '{print $2}'`

if [ -z ${PID} ]; then
    echo "The application '${APP_NAME}' already stopped."
    exit 1
fi

echo "The application's name is ${APP_NAME}, PID is ${PID}"
echo "Closing..."

kill -15 ${PID}

sleep 3

PID2=`ps aux | grep java | grep ${APP_NAME} | grep -v grep | awk '{print $2}'`
if [ -n ${PID2} ]; then
    echo "Forced closing application."
    kill -9 ${PID2}
fi

echo "OK!"

#ps aux|grep java|grep ${APP_NAME} | grep -v grep | awk '{print $2}' | xargs kill -15