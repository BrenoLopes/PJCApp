FROM openjdk:8u275-jdk

RUN wget https://downloads.apache.org/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
RUN tar -xf apache-maven-3.6.3-bin.tar.gz
RUN mv apache-maven-3.6.3 /usr/local/src/apache-maven
RUN rm apache-maven-3.6.3-bin.tar.gz
ENV M2_HOME=/usr/local/src/apache-maven
ENV MAVEN_HOME=/usr/local/src/apache-maven
ENV PATH=${M2_HOME}/bin:${PATH}

RUN rm -rf ./frontend/node_modules
COPY . /app/

WORKDIR /app/backend
ENTRYPOINT mvn spring-boot:run

EXPOSE 8080
