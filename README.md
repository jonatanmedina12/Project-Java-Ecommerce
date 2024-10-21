# Sistema de Gestión de Productos, Inventarios y Órdenes

## Descripción del Proyecto

Este proyecto es una aplicación Spring Boot que implementa un sistema de gestión para productos, inventarios y órdenes. Está diseñado para proporcionar una solución robusta y escalable para negocios que necesitan gestionar su inventario y procesar pedidos de clientes.

## Características Principales

- Gestión de Productos: CRUD completo para productos.
- Control de Inventario: Seguimiento en tiempo real de los niveles de stock.
- Procesamiento de Órdenes: Creación y gestión de pedidos de clientes.
- Autenticación y Autorización: Sistema de login seguro utilizando JWT.
- Reportes: Generación de informes sobre productos activos, ventas top y clientes frecuentes.
- Descuentos Automáticos: Aplicación de descuentos basados en reglas predefinidas.
- Auditoría: Seguimiento de cambios en entidades críticas.

## Tecnologías Utilizadas

- Java 17
- Spring Boot 3.3.4
- Spring Security para autenticación y autorización
- JWT para manejo de tokens
- SQL Server como base de datos
- JPA/Hibernate para persistencia de datos
- Maven para gestión de dependencias

## Estructura del Proyecto

```
src
├── main
│   ├── java
│   │   └── com.example.demo
│   │       ├── config
│   │       ├── controller
│   │       ├── dto
│   │       ├── entity
│   │       ├── repository
│   │       ├── security
│   │       ├── service
│   │       └── DemoApplication.java
│   └── resources
│       └── application.properties
└── test
    └── java
        └── com.example.demo
```

## Configuración y Ejecución

1. Asegúrate de tener Java 17 y Maven instalados.
2. Configura tu base de datos SQL Server en `application.properties`.
3. Ejecuta `mvn clean install` para compilar y descargar dependencias.
4. Inicia la aplicación con `mvn spring-boot:run`.

## API Endpoints

- `/api/auth/login`: Autenticación de usuarios
- `/api/productos/**`: Gestión de productos
- `/api/inventarios/**`: Control de inventario
- `/api/ordenes/**`: Procesamiento de órdenes
- `/api/usuarios/**`: Gestión de usuarios

## Funcionalidades Detalladas

### Gestión de Productos
- Crear, leer, actualizar y eliminar productos
- Búsqueda de productos por diversos criterios

### Control de Inventario
- Actualización automática del inventario al procesar órdenes
- Alertas de stock bajo (por implementar)

### Procesamiento de Órdenes
- Creación de nuevas órdenes
- Aplicación automática de descuentos:
    - 10% de descuento en un rango de tiempo parametrizado
    - 50% de descuento para pedidos aleatorios
    - 5% adicional para clientes frecuentes

### Reportes
- Productos activos
- Top 5 de productos más vendidos
- Top 5 de clientes frecuentes

### Seguridad
- Autenticación basada en JWT
- Roles de usuario para control de acceso

## Pruebas

Ejecuta `mvn test` para correr las pruebas unitarias.

## Uso de Docker

Este proyecto está configurado para ser ejecutado en un contenedor Docker. A continuación, se detallan los pasos para construir y ejecutar la aplicación usando Docker.

### Prerequisitos

- Docker instalado en tu sistema. Puedes descargarlo de [Docker's official website](https://www.docker.com/get-started).

### Dockerfile

El proyecto incluye un Dockerfile en la raíz del directorio. Este archivo define cómo se construirá la imagen Docker de la aplicación.

```dockerfile
# Usa una imagen base con Java 17
FROM openjdk:17-jdk-slim

# Directorio de trabajo en el contenedor
WORKDIR /app

# Copia el archivo JAR de tu aplicación al contenedor
COPY target/*.jar app.jar

# Expone el puerto en el que tu aplicación escucha
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java","-jar","/app/app.jar"]
```

### Construir la imagen Docker

Para construir la imagen Docker de tu aplicación, ejecuta el siguiente comando en la raíz de tu proyecto:

```bash
docker build -t ecommerce-app .
```

Este comando construirá una imagen Docker con el nombre `ecommerce-app`.

### Ejecutar el contenedor

Una vez que la imagen esté construida, puedes ejecutar la aplicación en un contenedor con el siguiente comando:

```bash
docker run -p 8080:8080 ecommerce-app
```

Esto iniciará la aplicación y la hará accesible en `http://localhost:8080`.

### Docker Compose


Para una configuración más compleja que incluya servicios adicionales como una base de datos, puedes usar Docker Compose. Aquí tienes un ejemplo de `docker-compose.yml`:

```yaml
version: '3.8'

services:
  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - db
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://db:3306/ecommerce?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: rootpassword

  db:
    image: mysql:8.0
    environment:
      MYSQL_DATABASE: ecommerce
      MYSQL_ROOT_PASSWORD: rootpassword
    volumes:
      - mysql-data:/var/lib/mysql

volumes:
  mysql-data:
```

Para ejecutar la aplicación con Docker Compose:

```bash
mvn clean install -U

mvn clean package -DskipTests -X

docker-compose up --build

```

Este comando construirá la imagen de la aplicación (si es necesario) e iniciará tanto la aplicación como la base de datos MySQL.

### Notas adicionales

- Asegúrate de que tu aplicación esté configurada para usar las variables de entorno definidas en el `docker-compose.yml` para la conexión a la base de datos.
- Para entornos de producción, considera usar secretos de Docker para manejar información sensible como contraseñas.
- Ajusta la configuración de Docker y Docker Compose según las necesidades específicas de tu entorno y aplicación.

## Video
- https://youtu.be/iZeokLvRZbU

## Licencia

Distribuido bajo la Licencia MIT. Ver `LICENSE` para más información.