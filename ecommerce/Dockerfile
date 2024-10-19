# Usa una imagen base con Java 17
FROM openjdk:17-jdk-slim

# Directorio de trabajo en el contenedor
WORKDIR /app

# Listar contenido del directorio actual
RUN ls -la

# Listar contenido del directorio target (si existe)
RUN ls -la target || echo "El directorio target no existe"

# Copia el archivo JAR de tu aplicación al contenedor
COPY target/*.jar app.jar

# Expone el puerto en el que tu aplicación escucha
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java","-jar","/app/app.jar"]