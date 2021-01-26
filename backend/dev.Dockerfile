FROM openjdk:8u275-jdk

WORKDIR /app

RUN wget https://downloads.apache.org/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
RUN tar -xf apache-maven-3.6.3-bin.tar.gz
RUN mv /app/apache-maven-3.6.3 /usr/local/src/apache-maven
RUN rm apache-maven-3.6.3-bin.tar.gz
ENV M2_HOME=/usr/local/src/apache-maven
ENV MAVEN_HOME=/usr/local/src/apache-maven
ENV PATH=${M2_HOME}/bin:${PATH}

COPY pom.xml .
RUN mvn dependency:go-offline

EXPOSE 8080