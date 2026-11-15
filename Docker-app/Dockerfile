FROM maven:3.9.9-eclipse-temurin-8 AS build
WORKDIR /workspace

COPY pom.xml ./
COPY src ./src

RUN mvn -B -DskipTests clean package

FROM tomcat:8.5-jre8
WORKDIR /usr/local/tomcat

RUN rm -rf webapps/*
COPY --from=build /workspace/target/vprofile-v2.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]

