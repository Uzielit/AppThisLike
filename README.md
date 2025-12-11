# ThisLike

> **Proyecto Integrador - Desarrollo de Aplicaciones Móviles**
>
> **Cuatrimestre:** [4/D]
> **Fecha de entrega:** 11 de Diciembre

---

## Equipo de Desarrollo

| Nombre Completo | Rol / Tareas Principales | Usuario GitHub |
| :--- | :--- | :--- |
| Abraham Rendon Uziel Alberto  | BackEnd,Retrofit | @uzielit |
| Muñiz Granda Jetzemany | Sensores,FrontEnd | @usuario2 |
| Zagal Garcia Diego | Logica,Repositorio | @usuario3 |

---

## Descripción del Proyecto

**¿Qué hace la aplicación?**
Presentamos ThisLike que es una aplicación que sirve como red social en donde puedes compartir tus fotos favoritas , asi las demas personas pueden ver lo que subes , con esto logramos conectar momentos que amamos a otras personas .Una aplicación diseñada para todo publico .


**Objetivo:**
Demostrar la implementación de una arquitectura robusta en Android utilizando servicios web y hardware del dispositivo.

---

## Stack Tecnológico y Características

Este proyecto ha sido desarrollado siguiendo estrictamente los lineamientos de la materia:

* **Lenguaje:** Kotlin 100%.
* **Interfaz de Usuario:** Jetpack Compose.
* **Arquitectura:** MVVM (Model-View-ViewModel).
* **Conectividad (API REST):** Retrofit.
    * **GET:** Obtención del Feed principal de publicaciones, filtrado de listas de Guardados y recuperación de perfiles de usuario.
    * **POST:** Para este metodo se usa a la hora de crear un registro de usuarios , y creación de publicaciones.
    * **UPDATE:** Edición de perfil (foto/biografía/credenciales), modificación de posts existentes y gestión de interacciones likes o guardados.
    * **DELETE:** Usamos el borrado en el modulo de edicion de publicacion y tambien en el borrado total de la cuenta 
* **Sensor Integrado:** Camara y Microfono
    * *Uso:*
    * Camara: Permite al usuario capturar fotografías en tiempo real para su foto de perfil o nuevas publicaciones.
    * Microfono :Implementación de grabación de audio comprimido para adjuntar notas de voz a las publicaciones.

---

## Capturas de Pantalla

[Coloca al menos 3 (investiga como agregarlas y se vean en GitHub)]

| Pantalla de Inicio | Operación CRUD | Uso del Sensor |
| :---:| :---: | :---: |
| !![LoginThisLike](https://github.com/user-attachments/assets/adf85f17-380b-4fe6-a73b-c6ed37da6fee) |! ![Crud](https://github.com/user-attachments/assets/155cbaf2-a7b3-4b3a-86a4-5d2810f1d18f)

| ![UsoSensor](https://github.com/user-attachments/assets/b4d58245-0d3d-46f1-a5f1-8293781dea9d) |

---

## Instalación y Releases

El ejecutable firmado (.apk) se encuentra disponible en la sección de **Releases** de este repositorio.

[Liga correctamente tu link de releases en la siguiente sección]

1.  Ve a la sección "Releases" (o haz clic [aquí](link_a_tus_releases)).
2.  Descarga el archivo `.apk` de la última versión.
3.  Instálalo en tu dispositivo Android (asegúrate de permitir la instalación de orígenes desconocidos).
