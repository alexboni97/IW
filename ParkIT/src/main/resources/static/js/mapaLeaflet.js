// const map = L.map('map').setView([lat == null ? 40.416775 : lat, lon == null ? -3.703790 : lon], 12); // TODO probar distintos zoom, 12 es el que mejor se ve para 5000m de radio
                                                                        // habra que hacer zoom dinamico segun el radio haciendo regla de 3
const map = L.map('map').setView([40.416775, -3.703790], 12)
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors'
}).addTo(map);

//rad = 5000; // radio por defecto de 5000m

const geocoder = L.Control.geocoder({
    placeholder: 'Buscar calle o marcador...',
    defaultMarkGeocode: false,
    collapsed: false
}).on('markgeocode', function (e) {
    const address = e.geocode.name;
    const latitude = e.geocode.center.lat;
    const longitude = e.geocode.center.lng;
    map.setView(e.geocode.center, 16); // TODO probar distintos zoom, 12 es el que mejor se ve para 5000m de radio
    document.getElementById('address').value = address;
    document.getElementById('latitude').value = latitude;
    document.getElementById('longitude').value = longitude;
}).addTo(map);

const geocoderContainer = geocoder.getContainer();
document.getElementById('geocoder-container').appendChild(geocoderContainer);

// Array para almacenar marcadores personalizados
const markers = [];

// Evento para añadir marcadores al hacer click
/*map.on('click', function(e) {
    const marker = L.marker(e.latlng).addTo(map);
    markers.push(marker);
});*/

// Evento para añadir los parkings al mapa
// parkings es un array de objetos que contiene la información de los parkings
// como los guardo en una variable de thymeleaf, puedo acceder a ellos desde aquí

console.log(parkings); // Para verificar que los datos se están pasando correctamente

if (rad != null) { // muestra el rango en el que se ha hecho la busqueda
    var circle = L.circle([lat, lon], {
        color: 'lightblue',
        fillColor: 'lightblue',
        fillOpacity: 0.3,
        radius: rad
    }).addTo(map);    
}

// Añadir un marcador personalizado
var marca = L.icon({
    iconUrl: '/img/marker.png',
    //shadowUrl: '/img/marker_shadow.png',

    iconSize: [60, 60], // size of the icon
    //shadowSize: [50, 64], // size of the shadow
    iconAnchor: [30, 60], // point of the icon which will correspond to marker's location
    //shadowAnchor: [4, 62],  // the same for the shadow
    popupAnchor: [0, -50] // point from which the popup should open relative to the iconAnchor
});

parkings.forEach(parking => {
    const marker = L.marker([parking.latitude, parking.longitude], {icon: marca}).addTo(map);
    const id = parking.id;
    const url = `/user/reserve/${id}`;
    marker.bindPopup(`<b>${parking.name}</b><br>${parking.address}</br><a href="${url}" class="btn btn-outline-secondary">Reservar</a>`);
    markers.push(marker);
});


// Función personalizada para buscar marcadores por nombre
function buscarMarcador(nombre) {
    markers.forEach(marker => {
        if (marker.getPopup() && marker.getPopup().getContent().includes(nombre)) {
            map.setView(marker.getLatLng(), 16);
            marker.openPopup();
        }
    });
}