#!/bin/bash
# Script triggering the building process

PATH_TO_SCRIPT=${0%/*}

# Check if the path that called the script is the same as the directory containing the script
if [ "$PATH_TO_SCRIPT" != "." ]; then
    cd "$PATH_TO_SCRIPT"
    echo "Changing directory to $PATH_TO_SCRIPT"
fi

echo "Building Additional Pylons..."
mvn clean compile assembly:single
if [ "$?" = "0" ]; then
    echo "OK"
else
    echo "FAILURE"
    exit 1
fi

echo "Building More Supply Depot..."
docker build -t bpm-stf .
if [ "$?" = "0" ]; then
    echo "OK"
else
    echo "FAILURE"
    exit 1
fi

cd "$OLDPWD"
echo "BUILD SUCCEEDED"