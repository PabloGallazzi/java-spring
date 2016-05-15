#!/usr/bin/env bash

if [[ $TRAVIS_PULL_REQUEST != "false" ]]; then
  echo "Skip deployment - Deployment doesn't run on pull requests"
  exit 0
fi

if [[ ! $TRAVIS_BRANCH =~ ^master.*$ ]]; then
  echo "Skip deployment - Deployment runs only on master branch"
  exit 0
fi

HTTP_STATUS_CODE=$(curl -sL -w "%{http_code}\\n" -X POST "http://190.192.142.145/deployer" -H "Content-Type: application/json" -d '{"commit":'${TRAVIS_COMMIT}',"key":"5737931a7628e17321000043"}')

if [[ ${HTTP_STATUS_CODE} != "204" ]]; then
  echo "Deployment failed, try deploying with the deploy.sh file in the root directory"
  exit 0
fi

echo "Deployment OK! Wait a few minutes for the server to start."

exit 0