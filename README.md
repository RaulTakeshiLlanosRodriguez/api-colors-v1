# API-COLORS-V1
![Alt text](image.png)

_API-COLORS-V1 es una aplicación backend desarrollada con Spring para permitir al compañero encargado del frontend obtener los datos de los colores mediante respuestas HTTP._

<!-- ``` -->
## Tecnologías Usadas
_En este proyecto se han utilizado las siguientes tecnologías:_

* [Spring](https://spring.io/) - Framework para realizar el backend
* [Postman](https://www.postman.com/) - Nos permite realizar pruebas de la API.
* [MySQL](https://www.mysql.com/) - Base de datos para almacenar los datos.
* [JUnit](https://junit.org/junit5/) - Conjunto de bibliotecas utilizadas para hacer pruebas unitarias en aplicaciones Java.
* [Mockito](https://site.mockito.org/) - Framework permite la creación de objetos dobles de prueba en pruebas de unidad automatizada para el desarrollo guiado por pruebas
* [Maven](https://nodejs.org/) - Gestor de dependencias.

<!-- ``` -->
## Ejecutar proyecto Spring vía comando CMD
### Clonar repositorio
```shell
git clone https://github.com/RaulTakeshiLlanosRodriguez/api-colors-v1.git
```
### Cambiar propiedades de application.properties con sus credenciales
```shell
spring.datasource.url=jdbc:mysql://localhost:3306/colors
spring.datasource.username=root
spring.datasource.password=12345
```
### Crear Base de datos mediante archivo data.sql
#### [ubicación data.sql](semilla/data.sql)
```shell
--1. CREAR BASE DE DATOS
CREATE DATABASE colors;
```
### Ejecutar proyecto
```shell
mvn spring-boot:run
```
## Poblar Base de Datos
### En el archivo data.sql ejecutamos la sentencia:
```shell
--3. insertar datos
INSERT INTO color(created_at, color, name, pantone_value, updated_at, year)
VALUES
    (CURRENT_TIMESTAMP, 'FF0000', 'Red', '14-4400', CURRENT_TIMESTAMP, 2023)
```
# URL
> [http://localhost:8080/colors?page=0](http://localhost:8080/colors?page=0)

## [Endpoints vía Postman](endpoints/colors.postman_collection.json)
> Ruta: endpoints/colors.postman_collection.json

## [Repositorio en Github](https://github.com/RaulTakeshiLlanosRodriguez/api-colors-v1)
> Ruta: https://github.com/RaulTakeshiLlanosRodriguez/api-colors-v1



