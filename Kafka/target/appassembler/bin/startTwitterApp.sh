#!/bin/sh
# ----------------------------------------------------------------------------
#  Copyright 2001-2006 The Apache Software Foundation.
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
# ----------------------------------------------------------------------------
#
#   Copyright (c) 2001-2006 The Apache Software Foundation.  All rights
#   reserved.


# resolve links - $0 may be a softlink
PRG="$0"

while [ -h "$PRG" ]; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

PRGDIR=`dirname "$PRG"`
BASEDIR=`cd "$PRGDIR/.." >/dev/null; pwd`



# OS specific support.  $var _must_ be set to either true or false.
cygwin=false;
darwin=false;
case "`uname`" in
  CYGWIN*) cygwin=true ;;
  Darwin*) darwin=true
           if [ -z "$JAVA_VERSION" ] ; then
             JAVA_VERSION="CurrentJDK"
           else
             echo "Using Java version: $JAVA_VERSION"
           fi
		   if [ -z "$JAVA_HOME" ]; then
		      if [ -x "/usr/libexec/java_home" ]; then
			      JAVA_HOME=`/usr/libexec/java_home`
			  else
			      JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Versions/${JAVA_VERSION}/Home
			  fi
           fi       
           ;;
esac

if [ -z "$JAVA_HOME" ] ; then
  if [ -r /etc/gentoo-release ] ; then
    JAVA_HOME=`java-config --jre-home`
  fi
fi

# For Cygwin, ensure paths are in UNIX format before anything is touched
if $cygwin ; then
  [ -n "$JAVA_HOME" ] && JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
  [ -n "$CLASSPATH" ] && CLASSPATH=`cygpath --path --unix "$CLASSPATH"`
fi

# If a specific java binary isn't specified search for the standard 'java' binary
if [ -z "$JAVACMD" ] ; then
  if [ -n "$JAVA_HOME"  ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
      # IBM's JDK on AIX uses strange locations for the executables
      JAVACMD="$JAVA_HOME/jre/sh/java"
    else
      JAVACMD="$JAVA_HOME/bin/java"
    fi
  else
    JAVACMD=`which java`
  fi
fi

if [ ! -x "$JAVACMD" ] ; then
  echo "Error: JAVA_HOME is not defined correctly." 1>&2
  echo "  We cannot execute $JAVACMD" 1>&2
  exit 1
fi

if [ -z "$REPO" ]
then
  REPO="$BASEDIR"/repo
fi

CLASSPATH="$BASEDIR"/etc:"$REPO"/org/apache/kafka/kafka_2.10/0.8.2.1/kafka_2.10-0.8.2.1.jar:"$REPO"/com/yammer/metrics/metrics-core/2.2.0/metrics-core-2.2.0.jar:"$REPO"/org/slf4j/slf4j-api/1.7.2/slf4j-api-1.7.2.jar:"$REPO"/org/scala-lang/scala-library/2.10.4/scala-library-2.10.4.jar:"$REPO"/org/apache/kafka/kafka-clients/0.8.2.1/kafka-clients-0.8.2.1.jar:"$REPO"/net/jpountz/lz4/lz4/1.2.0/lz4-1.2.0.jar:"$REPO"/org/xerial/snappy/snappy-java/1.1.1.6/snappy-java-1.1.1.6.jar:"$REPO"/org/apache/zookeeper/zookeeper/3.4.6/zookeeper-3.4.6.jar:"$REPO"/org/slf4j/slf4j-log4j12/1.6.1/slf4j-log4j12-1.6.1.jar:"$REPO"/log4j/log4j/1.2.16/log4j-1.2.16.jar:"$REPO"/jline/jline/0.9.94/jline-0.9.94.jar:"$REPO"/io/netty/netty/3.7.0.Final/netty-3.7.0.Final.jar:"$REPO"/net/sf/jopt-simple/jopt-simple/3.2/jopt-simple-3.2.jar:"$REPO"/com/101tec/zkclient/0.3/zkclient-0.3.jar:"$REPO"/org/scribe/scribe/1.3.7/scribe-1.3.7.jar:"$REPO"/com/fasterxml/jackson/core/jackson-core/2.4.2/jackson-core-2.4.2.jar:"$REPO"/com/fasterxml/jackson/core/jackson-databind/2.4.2/jackson-databind-2.4.2.jar:"$REPO"/com/fasterxml/jackson/core/jackson-annotations/2.4.0/jackson-annotations-2.4.0.jar:"$REPO"/org/codehaus/mojo/appassembler-maven-plugin/1.6/appassembler-maven-plugin-1.6.jar:"$REPO"/org/codehaus/mojo/appassembler/appassembler-model/1.6/appassembler-model-1.6.jar:"$REPO"/net/java/dev/stax-utils/stax-utils/20060502/stax-utils-20060502.jar:"$REPO"/stax/stax/1.1.1-dev/stax-1.1.1-dev.jar:"$REPO"/commons-io/commons-io/1.4/commons-io-1.4.jar:"$REPO"/org/codehaus/plexus/plexus-utils/3.0.15/plexus-utils-3.0.15.jar:"$REPO"/org/codehaus/plexus/plexus-interpolation/1.19/plexus-interpolation-1.19.jar:"$REPO"/org/apache/maven/maven-plugin-api/2.0/maven-plugin-api-2.0.jar:"$REPO"/org/apache/maven/maven-project/2.2.1/maven-project-2.2.1.jar:"$REPO"/org/apache/maven/maven-settings/2.2.1/maven-settings-2.2.1.jar:"$REPO"/org/apache/maven/maven-profile/2.2.1/maven-profile-2.2.1.jar:"$REPO"/org/apache/maven/maven-model/2.2.1/maven-model-2.2.1.jar:"$REPO"/org/apache/maven/maven-artifact-manager/2.2.1/maven-artifact-manager-2.2.1.jar:"$REPO"/org/apache/maven/maven-repository-metadata/2.2.1/maven-repository-metadata-2.2.1.jar:"$REPO"/org/apache/maven/wagon/wagon-provider-api/1.0-beta-6/wagon-provider-api-1.0-beta-6.jar:"$REPO"/backport-util-concurrent/backport-util-concurrent/3.1/backport-util-concurrent-3.1.jar:"$REPO"/org/apache/maven/maven-plugin-registry/2.2.1/maven-plugin-registry-2.2.1.jar:"$REPO"/org/apache/maven/maven-artifact/2.2.1/maven-artifact-2.2.1.jar:"$REPO"/org/apache/maven/shared/maven-mapping/1.0/maven-mapping-1.0.jar:"$REPO"/stax/stax-api/1.0.1/stax-api-1.0.1.jar:"$REPO"/org/codehaus/plexus/plexus-container-default/1.0-alpha-9-stable-1/plexus-container-default-1.0-alpha-9-stable-1.jar:"$REPO"/junit/junit/3.8.1/junit-3.8.1.jar:"$REPO"/classworlds/classworlds/1.1-alpha-2/classworlds-1.1-alpha-2.jar:"$REPO"/Kafka/Kafka/0.0.1-SNAPSHOT/Kafka-0.0.1-SNAPSHOT.jar

ENDORSED_DIR=
if [ -n "$ENDORSED_DIR" ] ; then
  CLASSPATH=$BASEDIR/$ENDORSED_DIR/*:$CLASSPATH
fi

if [ -n "$CLASSPATH_PREFIX" ] ; then
  CLASSPATH=$CLASSPATH_PREFIX:$CLASSPATH
fi

# For Cygwin, switch paths to Windows format before running java
if $cygwin; then
  [ -n "$CLASSPATH" ] && CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
  [ -n "$JAVA_HOME" ] && JAVA_HOME=`cygpath --path --windows "$JAVA_HOME"`
  [ -n "$HOME" ] && HOME=`cygpath --path --windows "$HOME"`
  [ -n "$BASEDIR" ] && BASEDIR=`cygpath --path --windows "$BASEDIR"`
  [ -n "$REPO" ] && REPO=`cygpath --path --windows "$REPO"`
fi

exec "$JAVACMD" $JAVA_OPTS -server \
  -classpath "$CLASSPATH" \
  -Dapp.name="startTwitterApp.sh" \
  -Dapp.pid="$$" \
  -Dapp.repo="$REPO" \
  -Dapp.home="$BASEDIR" \
  -Dbasedir="$BASEDIR" \
  kafka.Main \
  "$@"
