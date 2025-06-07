FROM maven:3.8.4-openjdk-17 as build-image
WORKDIR /app/source

# Copiar apenas o pom.xml primeiro
COPY ./pom.xml .
COPY ./mvnw .
COPY ./.mvn ./.mvn

# Baixar todas as dependências
RUN mvn dependency:go-offline

# Agora copiar o código fonte
COPY ./src ./src

# Fazer o build
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
COPY --from=build-image /app/source/target/*.jar /app/api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/api.jar"]