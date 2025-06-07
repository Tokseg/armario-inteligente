FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copiar apenas os arquivos necessários
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src src

# Instalar Maven e compilar a aplicação
RUN apk add --no-cache maven && \
    chmod +x mvnw && \
    ./mvnw clean package -DskipTests

# Expor a porta da aplicação
EXPOSE 8080

# Comando para executar a aplicação
CMD ["java", "-jar", "target/armario-inteligente-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=docker"] 