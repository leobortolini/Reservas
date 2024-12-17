# Etapa 1: Construção
FROM maven:3.9.4-eclipse-temurin-21 AS build

# Define o diretório de trabalho
WORKDIR /app

# Copia os arquivos do projeto para o container
COPY pom.xml .
COPY src ./src

# Baixa as dependências e constrói o projeto
RUN mvn clean package -DskipTests

# Etapa 2: Execução
FROM eclipse-temurin:21-jdk

# Define o diretório de trabalho
WORKDIR /app

# Copia o JAR construído na etapa anterior para o container
COPY --from=build /app/target/Reservas-0.0.1-SNAPSHOT.jar app.jar

# Expor a porta que a aplicação utiliza
EXPOSE 8081

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]