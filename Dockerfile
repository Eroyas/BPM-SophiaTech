FROM java:8-jdk

WORKDIR /root

RUN apt-get update && apt-get --no-install-recommends install -y maven && rm -rf /var/lib/apt/lists/*

RUN mkdir BPM-SophiaTech

COPY src/ BPM-SophiaTech/src
COPY pom.xml BPM-SophiaTech/pom.xml

WORKDIR /root/BPM-SophiaTech

RUN mvn clean package

#WORKDIR /usr/local/tomee/
#
#RUN apt-get  update \
#      && apt-get --no-install-recommends install -y openjdk-8-jdk \
#      && rm -rf /var/lib/apt/lists/*
#
#
#RUN wget https://github.com/flowable/flowable-engine/releases/download/flowable-6.1.2/flowable-6.1.2.zip\
#      && unzip flowable-6.1.2.zip \
#      && cp ./flowable-6.1.2/wars/flowable-rest.war ./webapps/.
#
#RUN cd ./webapps \
#    && mkdir flowable-rest \
#    && cd flowable-rest \
#    && jar xf ../flowable-rest.war \
#    && cd .. \
#    && rm -rf ./flowable-rest.war \
#    && cd ..
#
#COPY ./target/flowable-sophia-tech-1.0-SNAPSHOT.jar ./webapps/flowable-rest/WEB-INF/lib/custom-classes.jar
#
#HEALTHCHECK --interval=5s CMD curl --fail http://localhost:8080/ || exit 1
#
#EXPOSE 8080