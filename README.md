# CORPALIV - Aplicación móvil

Proyecto de la asignatura **DSY1105 - Desarrollo de Aplicaciones Móviles** (Duoc UC), Evaluación Parcial 2.

## Integrantes

- Javiera Faundez
- Dominga Ruiz

## ¿De qué trata el proyecto?

CORPALIV es una organización sin fines de lucro que trabaja por la inclusión social y laboral de personas con discapacidad múltiple.

Hicimos una app móvil para que empresas y personas que quieran ayudar puedan:

- conocer qué hace la organización,
- ver sus programas y productos,
- dejar sus datos para colaborar o para que los contacten.

Es un MVP (una primera versión), no una app lista para publicar. Todos los datos que aparecen son **ficticios**, de ejemplo.

## Funcionalidades

- **Inicio:** logo, mensaje de bienvenida, cifras de ejemplo y botones para ir a Colaborar y a Contacto.
- **Quiénes somos, Programas y Productos:** pantallas con información de ejemplo.
- **Colaborar:** formulario con nombre, mensaje, monto, casilla de aceptar contacto, switch, fecha y tipo de colaboración. Cada campo se valida y muestra un ícono y un mensaje si hay un error.
- **Contacto:** formulario con nombre, correo y mensaje, también con validaciones.
- **Registros:** lista de lo que se guardó en los dos formularios, con botón para eliminar.
- **Novedades:** una opción que se activa o desactiva y queda guardada en el teléfono.
- **Perfil:** permite elegir una imagen desde la galería o tomarla con la cámara.
- **Navegación:** menú lateral, barra inferior y un menú desplegable en la barra superior.
- **Animaciones:** cambio de color del botón en Novedades, mensajes de éxito que aparecen y desaparecen, íconos de error y un indicador de carga al guardar.

## Recursos nativos del teléfono

- Cámara (pide permiso al usuario)
- Galería de imágenes

## Tecnologías usadas

- Kotlin
- Jetpack Compose y Material 3
- Arquitectura MVVM
- Room (base de datos local) para guardar los formularios
- DataStore para guardar la preferencia de Novedades
- Coil para mostrar las imágenes
- Git y GitHub para el control de versiones, y Trello para organizar las tareas

## Cómo está organizado el proyecto

Dentro de `app/src/main/java/com/example/aplicacioncorpaliv/`:

- `model/`: los datos de los formularios.
- `data/`: la base de datos Room, el DataStore y la cámara.
- `viewmodel/`: la lógica y las validaciones.
- `ui/screens/`: las pantallas.
- `ui/componentes/`: el menú lateral, la barra superior y otros componentes.
- `ui/theme/`: los colores y el tema (colores sacados del logo de CORPALIV).

## Cómo ejecutar la app

1. Tener instalado **Android Studio**.
2. Descargar o clonar este repositorio y abrir la carpeta con Android Studio.
3. En `File > Settings > Build, Execution, Deployment > Build Tools > Gradle`, elegir **JDK 17 o 21** en "Gradle JDK" (con Java 25 da error).
4. Esperar que termine el **Sync** de Gradle.
5. Crear o abrir un emulador (Android 7.0 / API 24 o superior) o conectar un celular.
6. Presionar **Run**.

Si ya tenías instalada una versión anterior de la app, desinstálala antes, porque la base de datos cambió.

## Cosas que faltan por mejorar

- Conectar la app a un servidor real (hoy todo se guarda solo en el teléfono).
- La imagen del perfil no queda guardada al cerrar la app.
- Cambiar los datos de ejemplo por información real autorizada por la organización.
src/pages/Recepcionista/FichasPacientes.spec.jsx

## **[Link aquí](http://ec2-100-53-236-176.compute-1.amazonaws.com/)**

