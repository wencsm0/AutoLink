## java:8-alpine（145） java8:latest （500m）
FROM  amazoncorretto:21

# 维者信息
MAINTAINER mawj
EXPOSE  8081
COPY target/lib /config/lib
COPY  target/AutoLink-0.0.1-SNAPSHOT-exec.jar  /config/AutoLink.jar

CMD ["java","-Dloader.path=/config/lib","-jar","/config/AutoLink.jar"]