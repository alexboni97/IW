# 📌 ParkIT

Breve descripción de la aplicación, su objetivo y funcionalidades principales.
Ahorra tiempo y dinero cuando viajas en coche... Olvida las tediosas vueltas y vueltas a la manzana para encontrar un parking... Relájate, con ParkIT tendrás una plaza libre en tu destino esperándote.

ParkIT es una aplicación web (principalmente para dispositivos móviles) que ayuda a los usuarios a encontrar plazas de aparcamiento libres en tiempo real, mostrándolas por zonas en un mapa interactivo y guiándolos hasta ellas.

## 📂 Contenidos
- [Pestañas de la Aplicación](#pestañas-de-la-aplicación)
- [Instalación](#instalación)
- [Uso](#uso)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Contribución](#contribución)
- [Licencia](#licencia)

## 🖥️ Pestañas de la Aplicación

La aplicación cuenta con varias pestañas para mejorar la navegación y organización de funcionalidades:

### 🔹 Inicio
> **Descripción:** Esta es la pantalla principal donde se muestra una introducción a la aplicación, noticias destacadas o accesos rápidos a otras secciones.
> **URL:** [`/index.html`](./index.html)

### 🔹 Acerca de
> **Descripción:** Un panel de control donde el usuario puede visualizar información clave mediante gráficos, estadísticas y resúmenes.

### 🔹 Iniciar Sesion
> **Descripción:** Donde el usuario, empresa o admin puede loguearse y acceder a sus funcionalidades.

### 🔹 Registrarse
> **Descripción:** Donde el usuario, empresa o admin puede registrarse para acceder a mas funcionalidades de la web.

### 🔹 Perfil Usuario
> **Descripción:** Sección donde el usuario puede ver y editar su información personal, cambiar su contraseña y gestionar sus preferencias.
- **Reserva:** Reservar una plaza de aparcamiento. **URL:** [`/user/reserve.html`](./user/reserve.html)
- **Buscar:** Buscar plazas de aparcamietno disponibles. **URL:** [`/user/reserve.html`](./user/reserve.html)

### 🔹 Perfil Empresa
> **Descripción:** Sección donde el usuario puede ver y editar su información personal, cambiar su contraseña y gestionar sus preferencias.
- **Reserva:** Reservar una plaza de aparcamiento. **URL:** [`/user/reserve.html`](./user/reserve.html)
- **Buscar:** Buscar plazas de aparcamietno disponibles. **URL:** [`/user/reserve.html`](./user/reserve.html)

### 🔹 Perfil Admin
> **Descripción:** Sección donde el usuario puede ver y editar su información personal, cambiar su contraseña y gestionar sus preferencias.
- **Reserva:** Reservar una plaza de aparcamiento. **URL:** [`/user/reserve.html`](./user/reserve.html)
- **Buscar:** Buscar plazas de aparcamietno disponibles. **URL:** [`/user/reserve.html`](./user/reserve.html)

### 🔹 Historial de pagos/cobros
> **Descripción:** Muestra informes generados con filtros avanzados, permitiendo exportar datos en diferentes formatos como PDF o Excel.

### 🔹 Ayuda
> **Descripción:** Contiene documentación sobre el uso de la aplicación, preguntas frecuentes y un formulario de contacto.

## 🚀 Instalación

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/alexboni97/IW.git
   ```
2. Acceder al directorio:
   ```bash
   cd IW/ParkIT
   ```
3. Instalar dependencias:
   ```bash
   mvn install  # Si es una aplicación con Spring Boot
   ```
4. Ejecutar la aplicación:
   ```bash
   mvn spring-boot:run  # Para backend
   ```

## 📌 Uso

Explica aquí cómo utilizar la aplicación, con capturas de pantalla si es necesario.

1. Registrarse o iniciar sesión.
2. Buscar una plaza de aparcamiento disponible en el mapa interactivo.
3. Reservar la plaza y dirigirse a la ubicación guiado por la aplicación.
4. Ver el historial de reservas y gestionar el saldo disponible.
5. Empresas pueden agregar y gestionar plazas de aparcamiento.

## 🛠 Tecnologías Utilizadas

- Frontend: HTML, CSS, Bootstrap
- Backend: Java (Spring Boot)
- Base de Datos: H2
- Notificaciones: Websockets
- Mapas Interactivos: API de Google Maps

## 🤝 Contribución

1. Haz un fork del repositorio.
2. Crea una rama nueva (`git checkout -b feature-nueva`).
3. Realiza tus cambios y haz un commit (`git commit -m 'Agrega nueva funcionalidad'`).
4. Envía un pull request.

## 👥 Contribuidores

Agradecemos a todas las personas que han contribuido a este proyecto:
- [JAVIER ACEITUNO MONJA](https://github.com/jaceituno16)
- [ALEX GUILLERMO BONILLA TACO](https://github.com/alexboni97)
- [JUAN PABLO FERNÁNDEZ DE LA TORRE](https://github.com/juanpf04)
- [PAULA LÓPEZ SOLLA](https://github.com/Paula211)
- [ADRIÁN RODRÍGUEZ MARGALLO](https://github.com/adrizz8)
- [SERGIO SÁNCHEZ CARRASCO](https://github.com/WalterDeRacagua) 

## 📜 Licencia

Este proyecto está bajo la licencia [MIT](LICENSE).





