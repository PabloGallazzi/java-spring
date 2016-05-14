#!/bin/sh

if [[ $TRAVIS_PULL_REQUEST != "false" ]]; then
  echo "Skip deployment - Deployment doesn't run on pull requests"
  exit 1
fi

if [[ ! $TRAVIS_BRANCH =~ ^master.*$ ]]; then
  echo "Skip deployment - Deployment runs only on release branch"
  exit 1
fi

gem install rhc
rhc apps --token 932b2746d50b40dec2704669d07e488f315e2df9373212b8a32e0883b389baaa
git remote add openshift -f ssh://5737931a7628e17321000043@tptacsutnfrba-pablogallazzi.rhcloud.com/~/git/tptacsutnfrba.git/
git merge openshift/master -s recursive -X ours -m 'merge'
git push openshift HEAD

exit 0
