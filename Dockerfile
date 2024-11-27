FROM gcr.io/distroless/java21
WORKDIR /app
COPY build/libs/app.jar app.jar
ENV JDK_JAVA_OPTIONS=" -XX:MaxRAMPercentage=75 -Dlogback.configurationFile=logback.xml -Dhttp.proxyHost=webproxy.nais -Dhttps.proxyHost=webproxy.nais -Dhttp.proxyPort=8088 -Dhttps.proxyPort=8088 -Dhttp.nonProxyHosts=localhost|127.0.0.1|10.254.0.1|*.local|*.adeo.no|*.nav.no|*.aetat.no|*.devillo.no|*.oera.no|*.nais.io|*.aivencloud.com|*.intern.dev.nav.no"
ENV TZ="Europe/Oslo"
EXPOSE 8080
USER nonroot
CMD [ "app.jar" ]
