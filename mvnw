#!/bin/sh

#
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
#

# -----------------------------------------------------------------------------
# Maven Start Up Script
#
# Required ENV vars:
# ------------------
#   JAVA_HOME - location of a JDK home dir
#
# Optional ENV vars
# -----------------
#   MAVEN_OPTS - parameters passed to the Java VM when running Maven
#     e.g. -Xms256m -Xmx512m
#   MAVEN_SKIP_RC - flag to disable loading of mavenrc files
# -----------------------------------------------------------------------------

MAVEN_PROJECTBASEDIR="$(cd "$(dirname "$0")" && pwd)"

# OS specific support.  $var _must_ be set to either true or false.
cygwin=false
darwin=false
case "$(uname)" in
  CYGWIN*) cygwin=true;;
  Darwin*) darwin=true;;
esac

# For Cygwin, ensure paths are in UNIX format before anything is touched
if ${cygwin} ; then
  [ -n "$JAVA_HOME" ] && JAVA_HOME=$(cygpath --unix "$JAVA_HOME")
fi

if [ -z "$JAVA_HOME" ] ; then
  if [ -r /etc/gentoo-release ] ; then
    JAVA_HOME=$(java-config --jre-home)
  fi
fi

# For Cygwin, switch paths to Windows format before running java
if ${cygwin} ; then
  [ -n "$JAVA_HOME" ] && JAVA_HOME=$(cygpath --windows "$JAVA_HOME")
fi

if [ -z "$JAVA_HOME" ]; then
  echo "Error: JAVA_HOME is not defined."
  exit 1
fi

if [ ! -d "$JAVA_HOME" ]; then
  echo "Error: JAVA_HOME is not a valid directory."
  exit 1
fi

# For Cygwin, ensure paths are in UNIX format before anything is touched
if ${cygwin} ; then
  [ -n "$MAVEN_PROJECTBASEDIR" ] && MAVEN_PROJECTBASEDIR=$(cygpath --unix "$MAVEN_PROJECTBASEDIR")
fi

# source ~/.mavenrc if it exists
if [ -z "$MAVEN_SKIP_RC" ]; then
  if [ -f "$HOME/.mavenrc" ]; then
    . "$HOME/.mavenrc"
  fi
fi

# For Cygwin, switch paths to Windows format before running java
if ${cygwin} ; then
  [ -n "$MAVEN_PROJECTBASEDIR" ] && MAVEN_PROJECTBASEDIR=$(cygpath --windows "$MAVEN_PROJECTBASEDIR")
fi

# We use the value of the MAVEN_OPTS environment variable if it is set, otherwise we use the defaults
if [ -z "$MAVEN_OPTS" ]; then
  MAVEN_OPTS="-Xms256m -Xmx512m"
fi

exec "$JAVA_HOME/bin/java" \
  $MAVEN_OPTS \
  -Dmaven.multiModuleProjectDirectory="$MAVEN_PROJECTBASEDIR" \
  -Dmaven.home="${MAVEN_HOME}" \
  -classpath "${MAVEN_PROJECTBASEDIR}/.mvn/wrapper/maven-wrapper.jar" \
  org.apache.maven.wrapper.MavenWrapperMain "$@"

