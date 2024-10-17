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


## Licencia

Distribuido bajo la Licencia MIT. Ver `LICENSE` para más información.