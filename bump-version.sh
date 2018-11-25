#!/bin/sh
# ./bump-version 下个版本，
# 改版本名，然后提交，
set -e
old=$PWD
cd $(dirname $0)
project=$(pwd)
versionFile="$project/version.properties"

versionName=$1
sed -i "s/self_version\\s*=\\s*.*/self_version=$versionName/" $versionFile

git add $versionFile
git commit -m "Bumped version number to $versionName"

cd $old
