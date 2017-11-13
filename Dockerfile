FROM java:8-jdk

WORKDIR /root

RUN apt-get update && apt-get --no-install-recommends install -y maven && rm -rf /var/lib/apt/lists/*

RUN mkdir BPM-SophiaTech

COPY src/ BPM-SophiaTech/src
COPY pom.xml BPM-SophiaTech/pom.xml

WORKDIR /root/BPM-SophiaTech

RUN mvn clean package

ENTRYPOINT ["mvn", "exec:java"]