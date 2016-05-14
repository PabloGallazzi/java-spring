#!/bin/sh

if [[ $TRAVIS_PULL_REQUEST != "false" ]]; then
  echo "Skip deployment - Deployment doesn't run on pull requests"
  exit 1
fi

if [[ ! $TRAVIS_BRANCH =~ ^release.*$ ]]; then
  echo "Skip deployment - Deployment runs only on release branch"
  exit 1
fi

git remote add openshift -f ssh://5737931a7628e17321000043@tptacsutnfrba-pablogallazzi.rhcloud.com/~/git/tptacsutnfrba.git/
git merge openshift/master -s recursive -X ours -m 'merge'
git push openshift HEAD

exit 0
