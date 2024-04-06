# Intrucciones de construccion

El proyecto se encuentra desarrollado con `Spring boot 2.5.14`, `gradle 7.1` y `Java 11`.

Se exponen dos endpoint, uno llamado `/sign-up` y otro llamado `/login` securtizado con `Spring boot security` y `Jwt token` utilizando `HS256` como algoritmo criptográfico

Se utiliza `H2` como base de datos para `dev` y `test` junto con Spring data para la realizacion de operaciones CRUD

El proyecto esta respaldado con test con `mockito` y `jupiter` y este abarca a la capa de controller, service y repository con un coverage de 87%
![](.\img\coverage.png)

se utiliza la dependencia `lombok` para minimizar la repeticion de codigo

# Como se ejecuta el proyecto

se debe ejecutar los siguientes comandos: 

`./gradlew build` para instalar dependencias

`./gradlew startServer` para ejecutar el proyecto

`./gradlew testAndCoverageReport` para correr los test y generar el coverage

# Diagrama de componentes

![](.\img\diagrama de componente.png)

# Diagrama de secuencia

![](.\img\diagrama de secuencia login.png)

![](.\img\diagrama de secuencia sign up.png)
