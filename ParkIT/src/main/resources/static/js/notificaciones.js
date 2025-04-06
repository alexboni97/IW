
function renderNoti(msg) {
    const li = document.createElement('li');
            li.className = 'dropdown-item';
            li.innerHTML = `
                        <strong>Mensaje de: ${msg.from}</strong><br>
                        <small>${msg.text}</small><br>
                        <small class="text-muted">${new Date(msg.sent).toLocaleString('es-ES')}</small>
                    `;
    console.log("rendering notification: ", msg);
    return li;
}

let dropdownMenu = document.getElementById('menuDropdownNotis');
const sinNotis = document.createElement('li');
sinNotis.className = 'dropdown-item text-muted';
sinNotis.textContent = 'No hay mensajes';
if(config.user){
    go(config.rootUrl + "/user/received", "GET").then(ms =>{
        if(ms.length === 0) {
            dropdownMenu.appendChild(sinNotis);
        }else {
        ms.forEach(m => dropdownMenu.appendChild(renderNoti(m)))
        }
    
    });
}else if(config.enterprise){
    go(config.rootUrl + "/enterprise/received", "GET").then(ms =>{
        if(ms.length === 0) {
            dropdownMenu.appendChild(sinNotis);
        }else {
        ms.forEach(m => dropdownMenu.appendChild(renderNoti(m)))
        }
    
    });
}

// y aquí pinta mensajes según van llegando
if (ws.receive) {
    const oldFn = ws.receive; // guarda referencia a manejador anterior
    ws.receive = (m) => {
        oldFn(m); // llama al manejador anterior
        if(dropdownMenu.contains(sinNotis)) {
            dropdownMenu.removeChild(sinNotis);
        }
        dropdownMenu.appendChild(renderNoti(m));
        mostrarNuevaNotificacion(m);
    }
}


function obtenerEventoAleatorio() {
    fetch('/evento-aleatorio')
        .then(response => response.json())
        .then(data => {
            if (data.mensaje) {
                console.log(data.mensaje);
            } else {
                mostrarmensajeEvento(data);
            }
        })
        .catch(error => console.error('Error al obtener evento:', error));
}

function mostrarNuevaNotificacion(msg) {
    const container = document.getElementById('notification-container');

    // Crear el elemento de la notificación
    const notification = document.createElement('div');
    notification.className = 'notification';
    notification.innerHTML = `
        <strong>Mensaje de: ${msg.from}</strong><br>
        <small>${msg.text}</small><br>
        <small class="text-muted">${new Date(msg.sent).toLocaleString('es-ES')}</small>
    `;

    container.appendChild(notification);
    container.style.display = 'block';

    setTimeout(() => {
        notification.remove();
        if (container.childElementCount === 0) {
            container.style.display = 'none';
        }
    }, 5000);
}

