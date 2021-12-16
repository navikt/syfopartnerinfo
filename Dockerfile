FROM navikt/java:11
COPY init.sh /init-scripts/init.sh
COPY build/libs/app.jar app.jar
ENV JAVA_OPTS="-Dlogback.configurationFile=logback-remote.xml"
