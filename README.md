# Sistema de Reserva de Entradas de Cine

## Descripción del Proyecto
Este repositorio contiene una aplicación web SPA (Single Page Application). El sistema simula una plataforma completa para un cine, permitiendo consultar la cartelera, comprar entradas de forma online y gestionar la entrega física en la taquilla.

## Roles de Usuario
El sistema distingue entre dos perfiles principales, cada uno con permisos diferentes:
* **Espectador:** Puede registrarse libremente, consultar las sesiones, comprar un máximo de 10 localidades por sesión usando una tarjeta bancaria y revisar su historial personal de compras.
* **Taquillero:** Utiliza credenciales preasignadas por el cine. Su función principal es validar las compras y registrar la entrega de las entradas físicas a los espectadores.

## Funcionalidades Principales
* **Cartelera dinámica:** Visualización de películas disponibles para el día actual y los 6 días siguientes, ocultando automáticamente las sesiones que ya han comenzado.
* **Detalles completos:** Información extendida de cada película (resumen, duración) y de cada sesión (sala, precio, butacas disponibles).
* **Gestión de usuarios:** Registro, inicio de sesión y modificación de perfil.
* **Sistema de compra:** Proceso de reserva de entradas generando un identificador único de compra.
* **Historial de operaciones:** Listado cronológico de las compras realizadas por el espectador.
* **Internacionalización:** Adaptación automática de textos, formatos de fecha y símbolos monetarios.

## Arquitectura y Pruebas
El proyecto está estructurado en módulos claramente diferenciados:
* **Backend:** Lógica de negocio y acceso a base de datos relacional, verificado mediante pruebas de integración automatizadas.
* **Frontend:** Interfaz de usuario interactiva consumiendo los servicios del backend.
* **Testing E2E (Trabajo Tutelado):** Conjunto de pruebas End-To-End desarrolladas con Java, JUnit 5 y Selenium WebDriver. Estas pruebas automatizan el navegador para verificar flujos completos, como el login o la compra de entradas, simulando el comportamiento de un usuario real.
