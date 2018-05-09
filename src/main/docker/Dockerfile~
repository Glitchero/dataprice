FROM java:8
VOLUME /tmp
ADD dataprice.jar dataprice.jar
RUN bash -c 'touch /dataprice.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/dataprice.jar"]
