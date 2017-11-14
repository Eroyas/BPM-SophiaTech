FROM java:8-jdk

WORKDIR /root

COPY ./target/flowable-sophia-tech-1.0-SNAPSHOT-jar-with-dependencies.jar .

ENTRYPOINT ["java", "-jar", "flowable-sophia-tech-1.0-SNAPSHOT-jar-with-dependencies.jar"]