# Etapa 1: Construção
FROM maven:3.9.4-eclipse-temurin-21 AS build

# Define o diretório de trabalho
WORKDIR /app