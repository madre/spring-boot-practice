#!/bin/bash

echo "copy to SkinBuilder.sh run ==>"
builderDir="skinBuilder"
echo ${builderDir}
if [ -z "$1" ]
then
    sourceZipFile="aaa.zip"
else
    sourceZipFile=$1
fi
echo ${sourceZipFile}

dstZipPath="./uitool/skinbuild/skinsrc/"
dstZipFile="net_debug"

echo ${dstZipPath}${dstZipFile}.zip
echo ${dstZipPath}${dstZipFile}

rm -rf ${dstZipPath}${dstZipFile}
unzip -o ${dstZipPath}${dstZipFile}.zip -d ${dstZipPath}

#buildFile="./hello.sh"
buildFile="./uitool/buildSkin.sh"
bash ${buildFile}

outputPath="./uitool/skinbuild/skinoutputs/net/"

echo ${outputPath}
skinName="name"
mv ${outputPath}2000 ${outputPath}${skinName}
zip -r ${outputPath}${skinName}.zip ${outputPath}${skinName}

skinOutput="./skinOutput/"
if [ ! -d "${skinOutput}" ]; then
    mkdir ${skinOutput}
fi
cp ${outputPath}${skinName}.zip ${skinOutput}

