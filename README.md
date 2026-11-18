# CORPALIV - Aplicación móvil

Proyecto de la asignatura **DSY1105 - Desarrollo de Aplicaciones Móviles** (Duoc UC), Evaluación Parcial 4.

## Integrantes

- Javiera Faundez
- Dominga Ruiz

## ¿De qué trata el proyecto?

CORPALIV (Alba Lab) es una organización sin fines de lucro que trabaja por la inclusión social y laboral de personas con discapacidad múltiple.

Hicimos una app móvil para que empresas y personas que quieran ayudar puedan:

- conocer qué hace la organización, sus programas, productos e impacto,
- enviar una solicitud para colaborar (donación, voluntariado, alianza, compra, capacitación u otro),
- seguir en qué estado va su solicitud, usando un código de seguimiento,
- escribir al equipo por el formulario de contacto.

El equipo de CORPALIV (gestores) entra con correo y clave para revisar las solicitudes y cambiarles el estado.

Es un MVP (una primera versión), no una app lista para publicar. Todos los datos que aparecen son **ficticios**, de ejemplo.

> **Estado actual:** la app ya funciona completa con datos guardados en el teléfono (Room y DataStore). La conexión con los microservicios (Retrofit) está en desarrollo; este README se actualiza cuando quede lista.

## Funcionalidades

- **Bienvenida:** pantalla inicial con el logo y qué se puede hacer en la app. Tiene un botón discreto de "Acceso gestores" para el equipo.
- **Inicio:** mensaje de bienvenida, cifras de ejemplo y botones para ir a Colaborar y a Contacto.
- **Quiénes somos, Programas, Productos e Impacto:** información de ejemplo, con tarjetas que se expanden, filtro por categoría e historias e indicadores de impacto.
- **Colaborar:** formulario con nombre, correo o teléfono, mensaje (se puede dictar por voz), tipo de colaboración, monto (solo si es donación), fecha opcional, contacto prioritario y casilla de política de privacidad. Cada campo se valida y muestra un ícono y un mensaje si hay un error. Al enviar, la app entrega un **código de seguimiento** que se puede copiar.
- **Mis solicitudes:** lista de las solicitudes enviadas con su estado (Recibida, En revisión, Contactado, Concretado o Descartado) y el mensaje del equipo. Se puede consultar una solicitud escribiendo su código. También muestra los mensajes enviados por Contacto, con botón para eliminar.
- **Contacto:** formulario con nombre, correo, mensaje y casilla de privacidad, también con validaciones.
- **Perfil:** guarda nombre y correo o teléfono (rellenan solos los formularios) y permite elegir una imagen desde la galería o tomarla con la cámara.
- **Acceso gestores:** login para el equipo de CORPALIV. Una vez dentro, el gestor ve las solicitudes y puede cambiarles el estado y dejar un mensaje.
- **Configuración:** opciones de accesibilidad (por ejemplo lectura en voz alta y alto contraste) y preferencia de novedades. Se abre con el engranaje, al final del menú lateral.
- **Navegación:** menú lateral con grupos e íconos y barra inferior (Inicio, Colaborar, Contacto).
- **Animaciones:** transición suave entre pantallas, mensajes de éxito que aparecen y desaparecen, íconos de error y un indicador de carga al guardar.

## Recursos nativos del teléfono

- Cámara (pide permiso al usuario)
- Galería de imágenes
- Dictado por voz (reconocedor de voz de Android) y lectura en voz alta (Text-to-Speech)

## Tecnologías usadas

**App Android**

- Kotlin
- Jetpack Compose y Material 3
- Arquitectura MVVM (ViewModel, StateFlow)
- Room (base de datos local) para guardar las solicitudes y los mensajes de contacto
- DataStore para guardar preferencias (novedades, accesibilidad y datos del perfil)
- Coil para mostrar las imágenes
- Retrofit para conectar con los microservicios y con la API externa (en desarrollo)

**Backend** (repositorio aparte: _CORPALIV backend_, enlace: **[pendiente]**)

- Java 17 y Spring Boot (Maven)
- Cuatro microservicios, cada uno con su base de datos MySQL
- Spring Data JPA, validaciones y seguridad con JWT
- Swagger (springdoc) para documentar y probar los endpoints
- Pruebas unitarias con JUnit y Mockito

**Organización:** Git y GitHub para el control de versiones y Trello para las tareas (enlace: **[pendiente]**).

## Cómo está organizado el proyecto

Dentro de `app/src/main/java/com/example/aplicacioncorpaliv/`:

- `model/`: los datos de los formularios, los tipos de colaboración, los estados, el código de seguimiento y las **validaciones** (`Validaciones.kt`).
- `data/`: la base de datos Room, los DataStore, los repositorios y la cámara.
- `viewmodel/`: la lógica de cada pantalla.
- `ui/screens/`: las pantallas.
- `ui/componentes/`: el menú lateral, la barra superior y otros componentes.
- `ui/theme/`: los colores y el tema (colores sacados del logo de CORPALIV).

## Microservicios (backend)

| Servicio | Puerto | Base de datos | Qué hace |
|---|---|---|---|
| `ms-admin` | 9093 | `bd_admin` | Login, usuarios y roles. Es el único que firma los tokens JWT |
| `ms-contenidos` | 9091 | `bd_contenidos` | Programas, historias e indicadores de impacto |
| `ms-vinculacion` | 9092 | `bd_vinculacion` | Solicitudes de colaboración, tipos y seguimiento |
| `ms-catalogo` | 9094 | `bd_catalogo` | Categorías, productos y servicios |

Cada servicio valida el token por su cuenta. La documentación de cada uno está en `http://localhost:<puerto>/doc/swagger-ui.html`.

## Endpoints

Los que dicen **token** requieren el encabezado `Authorization: Bearer <token>`.

**ms-admin (9093)**

| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| POST | `/auth/login` | público | Entrega el token (correo y clave) |
| GET, POST | `/usuarios` | token de ADMIN | Lista y crea usuarios |
| GET, PUT, DELETE | `/usuarios/{id}` | token de ADMIN | Consulta, edita y elimina un usuario |
| GET | `/roles` | token de ADMIN | Lista de roles |

**ms-contenidos (9091)**

| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| GET | `/programas`, `/programas/{id}` | público | Programas publicados |
| GET | `/historias`, `/historias/{id}` | público | Historias de impacto |
| GET | `/indicadores`, `/indicadores/{id}` | público | Indicadores de impacto |
| GET | `/programas/{id}/exists` | público | Lo usa ms-vinculacion |
| GET | `/programas/admin`, `/historias/admin`, `/indicadores/admin` | token | Lista completa, incluye borradores |
| POST | `/programas`, `/historias`, `/indicadores` | token | Crear |
| PUT, DELETE | `/programas/{id}`, `/historias/{id}`, `/indicadores/{id}` | token | Editar y eliminar |

**ms-vinculacion (9092)**

| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| POST | `/solicitudes` | público | Crea una solicitud y devuelve su código de seguimiento |
| GET | `/solicitudes/seguimiento/{codigo}` | público | Estado de una solicitud (sin datos personales) |
| GET | `/tipos-colaboracion` | público | Tipos de colaboración |
| GET | `/solicitudes` (filtro opcional `?estado=`), `/solicitudes/{id}`, `/solicitudes/{id}/historial` | token | Consulta para el gestor |
| PATCH | `/solicitudes/{id}/estado` | token | Cambia el estado y deja el historial |
| DELETE | `/solicitudes/{id}` | token | Elimina la solicitud y su historial |

**ms-catalogo (9094)**

| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| GET | `/productos`, `/productos/{id}`, `/categorias`, `/categorias/{id}` | público | Catálogo |
| GET | `/productos/{id}/exists` | público | Lo usa ms-vinculacion |
| GET | `/productos/admin` | token | Lista completa del catálogo |
| POST | `/productos`, `/categorias` | token | Crear |
| PUT, DELETE | `/productos/{id}`, `/categorias/{id}` | token | Editar y eliminar |

## Cómo ejecutar la app

1. Tener instalado **Android Studio**.
2. Descargar o clonar este repositorio y abrir la carpeta con Android Studio.
3. En `File > Settings > Build, Execution, Deployment > Build Tools > Gradle`, elegir **JDK 21** en "Gradle JDK" (con Java 25 da error).
4. Esperar que termine el **Sync** de Gradle.
5. Crear o abrir un emulador (Android 7.0 / API 24 o superior) o conectar un celular.
6. Presionar **Run**.

Si ya tenías instalada una versión anterior de la app, desinstálala antes, porque la base de datos cambió.

**Cuenta de prueba del gestor (ficticia):** `gestor@ejemplo.cl` / `Gestor2026`.

## Cómo ejecutar el backend

1. Instalar **JDK 17** y **MySQL** (usuario `root` sin clave, o cambiar los datos en el `application.properties` de cada servicio).
2. Clonar el repositorio _CORPALIV backend_. Cada servicio es un proyecto Maven independiente.
3. Abrir una terminal en la carpeta de cada servicio y ejecutar (en Windows `mvnw.cmd spring-boot:run`):

```bash
./mvnw spring-boot:run
```

4. Orden recomendado: `ms-admin`, `ms-contenidos`, `ms-catalogo` y por último `ms-vinculacion` (este consulta a los dos anteriores). Las bases de datos y los datos de ejemplo se crean solos al iniciar.
5. Probar en Swagger: primero `POST /auth/login` en ms-admin con `gestor@ejemplo.cl` / `Gestor2026`, copiar el token y pegarlo en el botón **Authorize** de cada servicio.
6. Para el emulador, la app llama a `http://10.0.2.2:<puerto>` (así el emulador llega al computador).

Para ejecutar las pruebas de un servicio: `./mvnw test`.

## APK firmado

- Archivo del APK: **[pendiente]**
- Archivo `.jks` (en el repositorio): **[pendiente]**
- Captura de la generación del APK firmado: **[pendiente]**

## Cosas que faltan por mejorar

- Terminar la conexión de la app con los microservicios (hoy todo se guarda solo en el teléfono).
- Mostrar en la app los datos de una API externa.
- La imagen del perfil no queda guardada al cerrar la app.
- Cambiar los datos de ejemplo por información real autorizada por la organización.
