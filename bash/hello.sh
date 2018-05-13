#!/bin/bash

echo "hello.sh run ==>"

pwd

builderDir="skinBuilder"
echo ${builderDir}
if [ -z "$1" ]
then
    sourceZipFile="bash/data-dir/target.zip"
else
    sourceZipFile=$1
fi
if [ -z "$2" ]
then
    sourceName="target"
else
    sourceName=$2
fi
echo "sourceZipFile=${sourceZipFile}"
basename ${sourceZipFile}
echo "sourceName=${sourceName}"

