# :oncoming_automobile: ParkIT

Ahorra tiempo y dinero cuando viajas en coche... Olvida las tediosas vueltas y vueltas a la manzana para encontrar un parking... Relájate, con ParkIT tendrás una plaza libre en tu destino esperándote.

ParkIT es una aplicación web (principalmente para dispositivos móviles) que ayuda a los usuarios a encontrar plazas de aparcamiento libres en tiempo real, mostrándolas por zonas en un mapa interactivo y guiándolos hasta ellas.

## 📂 Contenidos
- [Diseño de la Aplicación](#item1)
- [Instalación](#item2)
- [Uso](#item3)
- [Tecnologías Utilizadas](#item4)
- [Contribución](#item5)
- [Contribuidores](#item6)
- [Licencia](#item7)

<a name="item1"></a>
## 🖥️ Diseño de la Aplicación
La aplicación cuenta con varias vistas que dependiendo de si el usuario es particular o empresa tiene diferentes permisos y funcionalidades (El usuario administrador puede acceder a todas las vistas) para mejorar la navegación y organización. A continuación se detallan las distintas vistas dependiendo del usuario identificado.

### 1. Vistas globales
#### 🔹 Inicio
> **Descripción:** Esta es la pantalla principal donde se muestra una introducción a la aplicación, noticias destacadas y accesos rápidos.
> **URL:** [`/index.html`](./index.html)

#### 🔹 Acerca de
> **Descripción:** Información sobre la aplicacion y su propósito.

#### 🔹 Iniciar Sesion
> **Descripción:** Formulario para que usuarios, empresas o administradores inicien sesión.

#### 🔹 Registrarse
> **Descripción:** Formulario para crear una cuenta de usuario, empresa o administrador.

#### 🔹 Ayuda
> **Descripción:** Documentación de uso, preguntas frecuentes y contacto para soporte.

### 2. Vistas de usuario particular
#### 🔹 Perfil
> **Descripción:** Sección donde el usuario puede ver y editar su información personal, cambiar su contraseña y gestionar sus preferencias.

#### 🔹 Mapa
> **Descripción:** Visualización de plazas de aparcamiento disponibles en tiempo real.

#### 🔹 Reservas
> **Descripción:** El usuario puede gestionar sus reservas activas.

#### 🔹 Historial 
> **Descripción:** Historial de estacionamiento con detalles de tiempo y gasto.

### 3. Vistas de empresa
#### 🔹 Perfil 
> **Descripción:** Administración de la cuenta de empresa, incluyendo estadísticas de uso.

#### 🔹 Parkings
> **Descripción:** Gestión de las plazas de aparcamiento que la empresa tiene registradas.

#### 🔹 Historial
> **Descripción:** Estadísticas y datos de ocupación de los parkings de la empresa.

<a name="item2"></a>
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
<a name="item3"></a>
## 📌 Uso

Explica aquí cómo utilizar la aplicación, con capturas de pantalla si es necesario.

1. Registrarse o iniciar sesión.
2. Buscar una plaza de aparcamiento disponible en el mapa interactivo.
3. Reservar la plaza y dirigirse a la ubicación guiado por la aplicación.
4. Ver el historial de reservas y gestionar el saldo disponible.
5. Empresas pueden agregar y gestionar plazas de aparcamiento.

<a name="item4"></a>
## 🛠 Tecnologías Utilizadas

### Frontend

![HTML](https://img.shields.io/badge/html-%23E34F26.svg?style=for-the-badge&logo=html&logoColor=white) ![CSS](https://img.shields.io/badge/css-%231572B6.svg?style=for-the-badge&logo=css&logoColor=white)  ![Bootstrap](https://img.shields.io/badge/bootstrap-%231572B6.svg?style=for-the-badge&logo=bootstrap&logoColor=white)  ![JavaScript](https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E) 

### Backend
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)

### Base de Datos
- H2
### Notificaciones
- Websockets
### Mapas Interactivos
- API de Google Maps

<a name="item5"></a>
## 🤝 Contribución

1. Haz un fork del repositorio.
2. Crea una rama nueva (`git checkout -b feature-nueva`).
3. Realiza tus cambios y haz un commit (`git commit -m 'Agrega nueva funcionalidad'`).
4. Envía un pull request.
<a name="item6"></a>
## 👥 Contribuidores

Agradecemos a todas las personas que han contribuido a este proyecto:
- [Javier Aceituno Monja](https://github.com/jaceituno16)
- [Alex Guillermo Bonilla Taco](https://github.com/alexboni97)
- [Juan Pablo Fernández de la Torre](https://github.com/juanpf04)
- [Paula López Solla](https://github.com/Paula211)
- [Adrián Rodríguez Margallo](https://github.com/adrizz8)
- [Sergio Sánchez Carrasco](https://github.com/WalterDeRacagua) 

<a name="item7"></a>
## 📜 Licencia

Este proyecto está bajo la licencia [MIT](LICENSE).





