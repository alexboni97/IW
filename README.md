# :oncoming_automobile: ParkIT

Ahorra tiempo y dinero cuando viajas en coche... Olvida las tediosas vueltas y vueltas a la manzana para encontrar un parking... Relájate, con ParkIT tendrás una plaza libre en tu destino esperándote.

ParkIT es una aplicación web (principalmente para dispositivos móviles) que ayuda a los usuarios a encontrar plazas de aparcamiento libres en tiempo real, mostrándolas por zonas en un mapa interactivo y guiándolos hasta ellas.

## 📂 Contenidos
- [Diseño de la Aplicación](#item1)
- [Instalación](#item2)
- [Uso](#item3)
- [Tecnologías Utilizadas](#item4)
- [Material externo](#item5)
- [Contribución](#item6)
- [Contribuidores](#item7)
- [Licencia](#item8)

<a name="item1"></a>
## 🖥️ Diseño de la Aplicación
La aplicación cuenta con varias vistas que dependiendo de si el usuario es particular o empresa tiene diferentes permisos y funcionalidades (El usuario administrador puede acceder a todas las vistas) para mejorar la navegación y organización. A continuación se detallan las distintas vistas dependiendo del usuario identificado.

### 1. Vistas implementadas
#### 🔹 Inicio
> **Descripción:** Esta es la pantalla principal donde se muestra una introducción a la aplicación, noticias destacadas y accesos rápidos.
> **URL:** [`/`](http://localhost:8080/)

#### 🔹 Iniciar Sesion
> **Descripción:** Formulario para que usuarios, empresas o administradores inicien sesión.
> **URL:** [`/login`](http://localhost:8080/login)

#### 🔹 Ayuda
> **Descripción:** Documentación de uso, preguntas frecuentes y contacto para soporte.
> **URL:** [`/help`](http://localhost:8080/help)

### 2. Vistas de usuario particular
#### 🔹 Perfil
> **Descripción:** Sección donde el usuario puede ver y editar su información personal, cambiar su contraseña y gestionar sus preferencias.
> **URL:** [`/user/{id}`](http://localhost:8080/user/2)

#### 🔹 Buscar
> **Descripción:** Visualización de plazas de aparcamiento disponibles en tiempo real.
> **URL:** [`/user/map`](http://localhost:8080/user/map)
> > **Vista Accesible desde esta:** Formulario de reserva de plaza de aparcamiento
> > **URL:** [`/user/reserve`](http://localhost:8080/user/reserve)

#### 🔹 Reservas
> **Descripción:** El usuario puede visualizar sus reservas activas.
> **URL:** [`/user/my-reserves`](http://localhost:8080/user/my-reserves)
> > **Vista Accesible desde esta:** Formulario de modificar reserva de plaza de aparcamiento
> > **URL:** [`/user/modify-reserve`](http://localhost:8080/user/modify-reserve)

### 3. Vistas de empresa
#### 🔹 Perfil 
> **Descripción:** Administración de la cuenta de empresa, incluyendo estadísticas de uso.
> **URL:** [`/entrerprise/{id}`](http://localhost:8080/enterprise/3)

#### 🔹 Ver Parkings
> **Descripción:** Visualizar las plazas de aparcamiento que la empresa tiene registradas.
> **URL:** [`/enterprise/enterprise-parkings`](http://localhost:8080/enterprise/enterprise-parkings)

#### 🔹 Añadir Parking
> **Descripción:** Añadir una zona nueva de aparcamiento en la empresa.
> **URL:** [`/enterprise/add-parking`](http://localhost:8080/enterprise/add-parking)

### 2. Proximamente en Vistas...
#### 🔹 Acerca de
> **Descripción:** Dropdown con las vistas de información sobre la aplicacion y su propósito.

#### 🔹 Registrarse
> **Descripción:** Formulario para crear una cuenta de usuario, empresa o administrador.

#### 🔹 Historial de Usuario
> **Descripción:** Historial de estacionamiento con detalles de tiempo y gasto.

#### 🔹 Historial de empresa
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
   mvn install
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
## 🔎 Material externo

En esta sección hemos incluído enlaces a material externo sobre el que nos hemos apoyado para realizar algunas partes de la web ParkIT 🚘:

1. Users-card: hemos utilizado una [plantilla de Bootstrap](https://startbootstrap.com/theme/personal). Dicha plantilla se puede utilizar y modificar por presentar una licencia MIT.
2. Navbar: Hemos utilizado la documentación que ofrece [Bootstrap](https://getbootstrap.com/docs/5.3/components/navbar/).
3. Extension en VSCode: para el uso de Bootstrap y elementos preconstruidos, [Bootstrap 5 Quick Snippets](https://github.com/anburocky3/bootstrap5-snippets/tree/master)

<a name="item6"></a>
## 🤝 Contribución

1. Haz un fork del repositorio.
2. Crea una rama nueva (`git checkout -b feature-nueva`).
3. Realiza tus cambios y haz un commit (`git commit -m 'Agrega nueva funcionalidad'`).
4. Envía un pull request.
<a name="item7"></a>
## 👥 Contribuidores

Agradecemos a todas las personas que han contribuido a este proyecto:
- [Javier Aceituno Monja](https://github.com/jaceituno16)
- [Alex Guillermo Bonilla Taco](https://github.com/alexboni97)
- [Juan Pablo Fernández de la Torre](https://github.com/juanpf04)
- [Paula López Solla](https://github.com/Paula211)
- [Adrián Rodríguez Margallo](https://github.com/adrizz8)
- [Sergio Sánchez Carrasco](https://github.com/WalterDeRacagua) 

<a name="item8"></a>
## 📜 Licencia

Este proyecto está bajo la licencia [Apache License](LICENSE).





