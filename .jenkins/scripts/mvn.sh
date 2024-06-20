#!/bin/bash -e

# Allows to run Maven build, with additional parameters specified using environmental variables

MAVEN_CLI_ARGS="${MAVEN_CLI_ARGS:-} ${@}"
VERBOSE=true
FILE=false

while [ $# -gt 0 ]; do
  case "$1" in
    -f | --file ) FILE=true; shift ;;
    -q | --quiet ) VERBOSE=false; shift ;;
    * ) shift ;;
  esac
done

if [ -n "${ADMIN_CREDENTIALS}" ]; then
  MAVEN_CLI_ARGS="--define \"cumulocity.management.username=${ADMIN_CREDENTIALS_USR}\" ${MAVEN_CLI_ARGS}"
  MAVEN_CLI_ARGS="--define \"cumulocity.management.password=${ADMIN_CREDENTIALS_PSW}\" ${MAVEN_CLI_ARGS}"
fi

if [ -n "${VERSION}" ]; then
  MAVEN_CLI_ARGS="--define revision=${VERSION} ${MAVEN_CLI_ARGS}"
  if [ -n "${CHANGE_VERSION}" ]; then
    MAVEN_CLI_ARGS="--define changelist=${CHANGE_VERSION} ${MAVEN_CLI_ARGS}"
  else
    MAVEN_CLI_ARGS="--define changelist= ${MAVEN_CLI_ARGS}"
  fi
else
  if [ -n "${CHANGE_VERSION}" ]; then
    MAVEN_CLI_ARGS="--define changelist=${CHANGE_VERSION} ${MAVEN_CLI_ARGS}"
  fi
fi

MAVEN_PROFILES="${MAVEN_PROFILES:-ci}"
MAVEN_CLI_ARGS="--activate-profiles $MAVEN_PROFILES ${MAVEN_CLI_ARGS}"

if [ -n "$WORKSPACE" ]; then
  MAVEN_CLI_ARGS="--define \"maven.repo.local=${WORKSPACE}/.m2/repository\" ${MAVEN_CLI_ARGS}"
fi

MVN_SETTINGS="${MVN_SETTINGS:-$HOME/.m2/settings.xml}"
MAVEN_CLI_ARGS="--settings $MVN_SETTINGS ${MAVEN_CLI_ARGS}"

if [ $VERBOSE = true ]; then
  MAVEN_CLI_ARGS="--show-version --errors ${MAVEN_CLI_ARGS}"
  set -x
fi

# https://maven.apache.org/configure.html
export MAVEN_OPTS="-Xms256m -Xmx512m -XX:MetaspaceSize=96m -XX:MaxMetaspaceSize=128m ${MAVEN_OPTS}"

if [ $FILE = true ]; then # run specified file only
  ./mvnw --batch-mode ${MAVEN_CLI_ARGS}
else # run all contained sub-projects
  for project in '.' 'microservice' 'lpwan-backend' 'cumulocity-sdk'; do
  if [ -d "$project" ]; then
    ./mvnw --batch-mode --file ${project} ${MAVEN_CLI_ARGS}
  fi
done
fi
