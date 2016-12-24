#!/usr/bin/env bash

mkdir deploy
cd deploy
git clone https://github.com/PabloGallazzi/java-spring.git
cd tp-tacs
git remote add openshift -f ssh://5737931a7628e17321000043@tptacsutnfrba-pablogallazzi.rhcloud.com/~/git/tptacsutnfrba.git/
git merge openshift/master -s recursive -X ours -m 'merge'
git push openshift HEAD
cd ..
cd ..
rm -rf deploy
exit 0
