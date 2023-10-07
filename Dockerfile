FROM benchinitiativecontainer.azurecr.io/tools/ci/beneficio/java-base:0.0.1 as build-env

ARG DB_USERNAME
ARG DB_PASSWORD
ARG DB_URL
ARG SONAR_TOKEN

ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}
ENV DB_URL=${DB_URL} 

WORKDIR /App

COPY . /App/

RUN sed -i 's/\r$//' mvnw # all my homies hate CRLF

RUN chmod +x mvnw &&\
    ./mvnw package -DskipTests
RUN ./mvnw clean verify sonar:sonar \
    -Dsonar.projectKey=beneficio-notifications \
    -Dsonar.projectName='beneficio-notifications' \
    -Dsonar.host.url=https://sonar.tools.bench.az.am-isd.com/ \
    -Dsonar.token=${SONAR_TOKEN}

FROM benchinitiativecontainer.azurecr.io/tools/ci/beneficio/java-base:0.0.1
WORKDIR /App

COPY --from=build-env /App/target/notifications-service-0.0.1-SNAPSHOT.jar /App/

RUN useradd notifications-service &&\
    chmod 755 /App &&\
    chown -R notifications-service /App

USER notifications-service
ENTRYPOINT [ "java", "-jar", "notifications-service-0.0.1-SNAPSHOT.jar" ]
