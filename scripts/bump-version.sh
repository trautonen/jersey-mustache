#!/bin/bash

if [ $# -ne 1 ]
then
    echo "Invalid number of arguments"
    echo "USAGE: $0 <version>"
    exit 1
fi

version=$1
script_dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
working_dir="$( pwd )"
files=( "pom.xml" "README.md" )

source $script_dir/functions.sh

if ! ( contains_files ${files[@]} )
then
    working_dir=$working_dir/..
    cd $working_dir
fi

if ! ( contains_files ${files[@]} )
then
    echo "Some of the required files (${files[@]}) not found, aborting version bump"
    exit 1
fi

echo "Updating version to $version in all poms"
mvn versions:set -DnewVersion=$version > /dev/null
mvn versions:commit > /dev/null

echo "Replacing version numbers in readme"
sed -i "s,<version>[^<]*</version>,<version>$version</version>,g" README.md

echo "Committing version changes"
git add ${files[@]}
git commit -m "Updated to version $version."
