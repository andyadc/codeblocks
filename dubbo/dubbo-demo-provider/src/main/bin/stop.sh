#!/bin/bash

# application's name
APP_NAME=dubbo-demo-provider

function pid() {
    ps aux | grep java | grep $1 | grep -v grep | awk '{print $2}'
}

PID=`pid ${APP_NAME}`

if [ -z ${PID} ]; then
    echo "The application '${APP_NAME}' has stopped or does not exist."
    exit 1
fi

echo "The application's name is ${APP_NAME}, PID is ${PID}"
echo "Closing..."

kill -15 ${PID}

sleep 3

PID=`pid ${APP_NAME}`

if [ -z ${PID} ]; then
    echo "Done"
    exit 1
else
    echo "Forced closing application!"
    kill -9 ${PID}
fi

echo "Done"

#ps aux|grep java|grep ${APP_NAME} | grep -v grep | awk '{print $2}' | xargs kill -15