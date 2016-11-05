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

CLASSPATH="$BASEDIR"/etc:"$REPO"/org/apache/hbase/hbase-common/0.98.16-hadoop2/hbase-common-0.98.16-hadoop2.jar:"$REPO"/org/apache/hbase/hbase-annotations/0.98.16-hadoop2/hbase-annotations-0.98.16-hadoop2.jar:"$REPO"/com/google/guava/guava/12.0.1/guava-12.0.1.jar:"$REPO"/com/google/code/findbugs/jsr305/1.3.9/jsr305-1.3.9.jar:"$REPO"/commons-logging/commons-logging/1.1.1/commons-logging-1.1.1.jar:"$REPO"/commons-codec/commons-codec/1.7/commons-codec-1.7.jar:"$REPO"/commons-lang/commons-lang/2.6/commons-lang-2.6.jar:"$REPO"/commons-collections/commons-collections/3.2.1/commons-collections-3.2.1.jar:"$REPO"/commons-io/commons-io/2.4/commons-io-2.4.jar:"$REPO"/com/google/protobuf/protobuf-java/2.5.0/protobuf-java-2.5.0.jar:"$REPO"/org/mortbay/jetty/jetty-util/6.1.26/jetty-util-6.1.26.jar:"$REPO"/org/apache/hadoop/hadoop-common/2.2.0/hadoop-common-2.2.0.jar:"$REPO"/org/apache/hadoop/hadoop-annotations/2.2.0/hadoop-annotations-2.2.0.jar:"$REPO"/commons-cli/commons-cli/1.2/commons-cli-1.2.jar:"$REPO"/org/apache/commons/commons-math/2.1/commons-math-2.1.jar:"$REPO"/xmlenc/xmlenc/0.52/xmlenc-0.52.jar:"$REPO"/commons-httpclient/commons-httpclient/3.1/commons-httpclient-3.1.jar:"$REPO"/commons-net/commons-net/3.1/commons-net-3.1.jar:"$REPO"/org/mortbay/jetty/jetty/6.1.26/jetty-6.1.26.jar:"$REPO"/com/sun/jersey/jersey-core/1.9/jersey-core-1.9.jar:"$REPO"/com/sun/jersey/jersey-json/1.9/jersey-json-1.9.jar:"$REPO"/org/codehaus/jettison/jettison/1.1/jettison-1.1.jar:"$REPO"/com/sun/xml/bind/jaxb-impl/2.2.3-1/jaxb-impl-2.2.3-1.jar:"$REPO"/javax/xml/bind/jaxb-api/2.2.2/jaxb-api-2.2.2.jar:"$REPO"/javax/activation/activation/1.1/activation-1.1.jar:"$REPO"/org/codehaus/jackson/jackson-jaxrs/1.8.3/jackson-jaxrs-1.8.3.jar:"$REPO"/org/codehaus/jackson/jackson-xc/1.8.3/jackson-xc-1.8.3.jar:"$REPO"/com/sun/jersey/jersey-server/1.9/jersey-server-1.9.jar:"$REPO"/asm/asm/3.1/asm-3.1.jar:"$REPO"/tomcat/jasper-compiler/5.5.23/jasper-compiler-5.5.23.jar:"$REPO"/tomcat/jasper-runtime/5.5.23/jasper-runtime-5.5.23.jar:"$REPO"/commons-el/commons-el/1.0/commons-el-1.0.jar:"$REPO"/net/java/dev/jets3t/jets3t/0.6.1/jets3t-0.6.1.jar:"$REPO"/commons-configuration/commons-configuration/1.6/commons-configuration-1.6.jar:"$REPO"/commons-digester/commons-digester/1.8/commons-digester-1.8.jar:"$REPO"/commons-beanutils/commons-beanutils/1.7.0/commons-beanutils-1.7.0.jar:"$REPO"/commons-beanutils/commons-beanutils-core/1.8.0/commons-beanutils-core-1.8.0.jar:"$REPO"/org/slf4j/slf4j-api/1.7.5/slf4j-api-1.7.5.jar:"$REPO"/org/slf4j/slf4j-log4j12/1.7.5/slf4j-log4j12-1.7.5.jar:"$REPO"/org/codehaus/jackson/jackson-core-asl/1.8.8/jackson-core-asl-1.8.8.jar:"$REPO"/org/apache/avro/avro/1.7.4/avro-1.7.4.jar:"$REPO"/com/thoughtworks/paranamer/paranamer/2.3/paranamer-2.3.jar:"$REPO"/org/xerial/snappy/snappy-java/1.0.4.1/snappy-java-1.0.4.1.jar:"$REPO"/com/jcraft/jsch/0.1.42/jsch-0.1.42.jar:"$REPO"/org/apache/commons/commons-compress/1.4.1/commons-compress-1.4.1.jar:"$REPO"/org/tukaani/xz/1.0/xz-1.0.jar:"$REPO"/org/apache/hadoop/hadoop-mapreduce-client-core/2.2.0/hadoop-mapreduce-client-core-2.2.0.jar:"$REPO"/org/apache/hadoop/hadoop-yarn-common/2.2.0/hadoop-yarn-common-2.2.0.jar:"$REPO"/org/apache/hadoop/hadoop-yarn-api/2.2.0/hadoop-yarn-api-2.2.0.jar:"$REPO"/com/google/inject/guice/3.0/guice-3.0.jar:"$REPO"/javax/inject/javax.inject/1/javax.inject-1.jar:"$REPO"/aopalliance/aopalliance/1.0/aopalliance-1.0.jar:"$REPO"/com/sun/jersey/jersey-test-framework/jersey-test-framework-grizzly2/1.9/jersey-test-framework-grizzly2-1.9.jar:"$REPO"/com/sun/jersey/jersey-test-framework/jersey-test-framework-core/1.9/jersey-test-framework-core-1.9.jar:"$REPO"/javax/servlet/javax.servlet-api/3.0.1/javax.servlet-api-3.0.1.jar:"$REPO"/com/sun/jersey/jersey-client/1.9/jersey-client-1.9.jar:"$REPO"/com/sun/jersey/jersey-grizzly2/1.9/jersey-grizzly2-1.9.jar:"$REPO"/org/glassfish/grizzly/grizzly-http/2.1.2/grizzly-http-2.1.2.jar:"$REPO"/org/glassfish/grizzly/grizzly-framework/2.1.2/grizzly-framework-2.1.2.jar:"$REPO"/org/glassfish/gmbal/gmbal-api-only/3.0.0-b023/gmbal-api-only-3.0.0-b023.jar:"$REPO"/org/glassfish/external/management-api/3.0.0-b012/management-api-3.0.0-b012.jar:"$REPO"/org/glassfish/grizzly/grizzly-http-server/2.1.2/grizzly-http-server-2.1.2.jar:"$REPO"/org/glassfish/grizzly/grizzly-rcm/2.1.2/grizzly-rcm-2.1.2.jar:"$REPO"/org/glassfish/grizzly/grizzly-http-servlet/2.1.2/grizzly-http-servlet-2.1.2.jar:"$REPO"/org/glassfish/javax.servlet/3.1/javax.servlet-3.1.jar:"$REPO"/com/sun/jersey/contribs/jersey-guice/1.9/jersey-guice-1.9.jar:"$REPO"/com/google/inject/extensions/guice-servlet/3.0/guice-servlet-3.0.jar:"$REPO"/com/github/stephenc/findbugs/findbugs-annotations/1.3.9-1/findbugs-annotations-1.3.9-1.jar:"$REPO"/log4j/log4j/1.2.17/log4j-1.2.17.jar:"$REPO"/junit/junit/4.11/junit-4.11.jar:"$REPO"/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar:"$REPO"/org/apache/hbase/hbase-client/0.98.16-hadoop2/hbase-client-0.98.16-hadoop2.jar:"$REPO"/org/apache/hbase/hbase-protocol/0.98.16-hadoop2/hbase-protocol-0.98.16-hadoop2.jar:"$REPO"/io/netty/netty/3.6.6.Final/netty-3.6.6.Final.jar:"$REPO"/org/apache/zookeeper/zookeeper/3.4.6/zookeeper-3.4.6.jar:"$REPO"/org/cloudera/htrace/htrace-core/2.04/htrace-core-2.04.jar:"$REPO"/org/codehaus/jackson/jackson-mapper-asl/1.8.8/jackson-mapper-asl-1.8.8.jar:"$REPO"/org/jruby/jcodings/jcodings/1.0.8/jcodings-1.0.8.jar:"$REPO"/org/jruby/joni/joni/2.1.2/joni-2.1.2.jar:"$REPO"/com/yammer/metrics/metrics-core/2.2.0/metrics-core-2.2.0.jar:"$REPO"/org/apache/hadoop/hadoop-auth/2.2.0/hadoop-auth-2.2.0.jar:"$REPO"/HBase/HBase/0.0.1-SNAPSHOT/HBase-0.0.1-SNAPSHOT.jar

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
  -Dapp.name="hbaseApp.sh" \
  -Dapp.pid="$$" \
  -Dapp.repo="$REPO" \
  -Dapp.home="$BASEDIR" \
  -Dbasedir="$BASEDIR" \
  hbase.Main \
  "$@"
