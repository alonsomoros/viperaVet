# 🧩 API Training – Spring Boot

## 📑 Índice
- [Acerca del proyecto](#acerca-del-proyecto)
- [Objetivo](#objetivo)
- [Métodos soportados](#métodos-soportados)
- [Base de datos](#base-de-datos)
- [Seguridad](#seguridad)
- [API externa](#api-externa)
- [Caché](#caché)
- [Validaciones](#validaciones)
- [Registro de logs](#registro-de-logs)
- [Manejo de errores y excepciones](#manejo-de-errores-y-excepciones)
- [Documentación](#documentación)
- [Circuit Breaker](#circuit-breaker)
- [Uso](#uso)
- [Comandos Docker](#comandos-docker)

---

## 🧠 Acerca del proyecto
Este proyecto es una **API RESTful** desarrollada con **Spring Boot**
Sigue una arquitectura por capas (**Controlador**, **Servicio**, **Repositorio**) y aplica buenas prácticas de desarrollo como seguridad, validaciones, caché y manejo centralizado de errores.

---

## 🎯 Objetivo
El objetivo del proyecto es desarrollar una **API funcional con Spring Boot** que incluya los siguientes aspectos:

1. Controlador con servicios tipo **POST**, **GET**, **PUT** o **PATCH** con acceso a una base de datos.  
2. Mecanismos de **seguridad** implementados con Spring Security.  
3. **Consumo de una API externa** usando `RestTemplate`, `FeignClient` o `WebClient`.  
4. **Sistema de caché** para mejorar el rendimiento.  
5. **Validaciones** y **registro de logs**.  
6. **Manejo de errores y excepciones** personalizado.  
7. **Documentación OpenAPI** (Swagger UI) con **springdoc-openapi**.  
8. Implementación del patrón **Circuit Breaker** con **Resilience4j**.

---

## ⚙️ Métodos soportados

| Método HTTP | Endpoint | Descripción | Respuesta exitosa |
|--------------|-----------|--------------|-------------------|
| **POST** | `/auth/register` | Crea un nuevo usuario. Requiere los datos en el cuerpo de la solicitud. | **201 Created** / **400 Bad Request** / **409 Conflict**   |
| **POST** | `/auth/login` | Hace Login con un usuario ya existente. Necesita el token para autenticarse | **200 Ok** / **401 Unauthorized**  |
| **GET** | `/users/` | Obtiene todos los usuarios existentes. | **200 OK** / **404 Not Found** |
| **GET** | `/users/{id}` | Obtiene un usuario por su DNI. | **200 OK** / **404 Not Found** |
| **DELETE** | `/users/{id}` | Elimina un usuario por su ID. | **200 OK** / **404 Not Found** |
| **GET** | `/users/x` | Obtiene una lista de países desde una **API externa**. | **200 OK** |

> 🛰️ El último endpoint realiza una llamada a una API externa utilizando `RestTemplate`.

---

## 🗄️ Base de datos
- **Motor de base de datos:** MySQL  
- Se utiliza **Spring Data JPA** y **Jakarta Persistence** para el mapeo objeto-relacional (ORM) y la gestión de entidades.
- **Diagrama de Entidad-Relación:**
![Diagrama Entidad-Relación](./src/main/resources/static/Vet_ER_Diagram.png)


---

## 🔐 Seguridad
La seguridad está implementada con **Spring Security**.  
Todos los endpoints requieren autenticación previa.

**Credenciales de prueba:**
Usuario: user / 
Contraseña: password

---

## 🌍 API externa
La aplicación consume una API externa en la siguiente URL:
(en proceso)

Esta integración se realiza mediante **RestTemplate** (o alternativamente `FeignClient` o `WebClient`).

---

## ⚡ Caché
(en proceso)

---

## ✅ Validaciones
La API cuenta con validaciones estándar y personalizadas.

- **Validaciones estándar:** `@NotNull`, etc. 
- **Validaciones personalizadas:** Validación de Email internamente.  

Las solicitudes con datos inválidos generan respuestas de error estructuradas, gestionadas por el manejador global de excepciones.

---

## 🧾 Registro de logs
(En proceso)

---

## 🚨 Manejo de errores y excepciones
El manejo de errores se realiza de forma centralizada mediante un `@ControllerAdvice`.

**Excepciones personalizadas:**
- ``EmailTakenException`` → se lanza cuando el correo electrónico ya está registrado.

- ``EmailNotFoundException`` → se lanza cuando no se encuentra un correo electrónico en la base de datos.

- ``InvalidEmailException`` → se lanza cuando el formato del correo electrónico es inválido.

- ``UsernameTakenException`` → se lanza cuando el nombre de usuario ya está registrado.

- ``UsernameNotFoundException`` → se lanza cuando no se encuentra un nombre de usuario en la base de datos.

- ``InvalidUsernameException`` → se lanza cuando el formato del nombre de usuario no cumple las reglas definidas.

- ``IdNotFoundException`` → se lanza cuando no se encuentra un recurso por su ID en la base de datos.

- ``WeakPasswordException`` → se lanza cuando la contraseña no cumple los criterios mínimos de seguridad.

- ``UserCreationException`` → se lanza cuando ocurre un error inesperado durante la creación de un usuario.

- ``RuntimeException`` → captura cualquier otra excepción no controlada.

Las respuestas de error se devuelven en formato JSON, con un mensaje claro y el código HTTP correspondiente.

---

## 📘 Documentación
La documentación de la API se genera automáticamente con **Springdoc OpenAPI** y es accesible desde un navegador web.

- **Interfaz Swagger UI:**  
  👉 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

Esta interfaz permite **probar los endpoints** de manera interactiva.

---

## 🔁 Circuit Breaker
(en proceso)

---

## 🚀 Uso
La aplicación puede ejecutarse de forma local o dentro de un contenedor Docker.

### Requisitos previos
- **Docker** y **Docker Compose** instalados  
- **Java 17** o superior  
- **Maven** o **Gradle**

---

## 🐳 Comandos Docker

```bash
# Iniciar la base de datos y la aplicación
docker-compose up

# Detener los contenedores
docker-compose down
