#!/bin/bash

echo "copy to SkinBuilder.sh run ==>"
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
echo "sourceName=${sourceName}"
# basename ${sourceZipFile}

dstZipPath="bash/uitool/skinbuild/skinsrc/"
dstZipFile="net_debug"

echo ${dstZipPath}${dstZipFile}.zip
echo ${dstZipPath}${dstZipFile}
if [ -e ${dstZipPath}${dstZipFile}.zip ]; then
    echo "rm ${dstZipPath}${dstZipFile}.zip"
    rm ${dstZipPath}${dstZipFile}.zip
fi
echo ":mv ${sourceZipFile} ${dstZipPath}${dstZipFile}.zip"
mv ${sourceZipFile} ${dstZipPath}${dstZipFile}.zip

rm -rf ${dstZipPath}${dstZipFile}
unzip -o ${dstZipPath}${dstZipFile}.zip -d ${dstZipPath}

#buildFile="./hello.sh"
buildFile="bash/uitool/buildSkin.sh"
bash ${buildFile}

outputPath="bash/uitool/skinbuild/skinoutputs/net/"

echo ${outputPath}
skinName=${sourceName}
mv ${outputPath}2000 ${outputPath}${skinName}
echo ":zip -r ${outputPath}${skinName}.zip ${outputPath}${skinName}"
zip -r ${outputPath}${skinName}.zip ${outputPath}${skinName}

skinOutput="skinOutput/"
if [ ! -d "${skinOutput}" ]; then
    mkdir ${skinOutput}
fi
echo ":cp ${outputPath}${skinName}.zip ${skinOutput}"
cp ${outputPath}${skinName}.zip ${skinOutput}

