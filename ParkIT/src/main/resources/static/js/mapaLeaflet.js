//esperar que este cargado el documento
document.addEventListener('DOMContentLoaded', function () {
    // Obtener referencias a los inputs de latitud y longitud
    let latitudeInput = document.getElementById('latitude');
    let longitudeInput = document.getElementById('longitude');
    let rangeInput = 3000;
    let latlng = [40.416775, -3.703790];
    
    // Inicializar el mapa
    let map = L.map('map');
    let lat ;
    let lon ;
    let circleFind;
    let iconoMiUbicacion = L.icon({
        iconUrl: '/img/mi-ubicacion.png',
        iconSize: [40, 40],
        iconAnchor: [20, 40],
        popupAnchor: [0, 40]
    });

    function onLocationFound(e) {
        latlng = e.latlng;

        L.marker(e.latlng, { icon: iconoMiUbicacion }).addTo(map)
            .bindTooltip("Tu Ubicación").openTooltip();
        if(circleFind) {
            map.removeLayer(circleFind);
        }

        circleFind = L.circle(latlng, rangeInput).addTo(map);
    }

    if (latitudeInput?.value == '' || longitudeInput?.value  == '') {
        map.locate({ setView: true, maxZoom: 13 });
        map.on('locationfound', onLocationFound);
    } else {
        lat = parseFloat(latitudeInput?.value || 40.416775);
        lon = parseFloat(longitudeInput?.value || -3.703790);
        map.setView([lat, lon], 13);
        if(circleFind){
            map.removeLayer(circleFind);
        }
        //circleFind=L.circle([lat, lon], 30000).addTo(map);
    }

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors'
    }).addTo(map);
    
    let geocoder = L.Control.geocoder({
        placeholder: 'Buscar calle...',
        defaultMarkGeocode: false,
        collapsed: false
    }).on('markgeocode', function (e) {
        const fullAddress = e.geocode.name; // Dirección completa
        const latitude = e.geocode.center.lat;
        const longitude = e.geocode.center.lng;

        // Extraer componentes de la dirección
        const addressComponents = fullAddress.split(",").map(item => item.trim());

        let calle = addressComponents.length == 3 ? '' : addressComponents[0]; // Primera parte (nombre de la calle)
        let ciudad = addressComponents.length == 3 ? addressComponents[addressComponents.length - 3] : ''; // Penúltimo elemento (ciudad)
        ciudad = addressComponents.length > 4 ? addressComponents[addressComponents.length - 4] : ciudad; // Penúltimo elemento (ciudad)
        let codigoPostal = addressComponents.length > 1 ? addressComponents[addressComponents.length - 2].match(/\d{4,5}/) : ''; // Último elemento (CP)
        let pais = addressComponents.length > 2 ? addressComponents[addressComponents.length - 1] : ''; // último elemento (ciudad)

        codigoPostal = codigoPostal ? codigoPostal[0] : ''; // Obtener el CP si existe

        // Actualizar los campos en el formulario
        // document.getElementById('fullAddress').value = fullAddress;
        // document.getElementById('calle').value = calle;
        // document.getElementById('ciudad').value = ciudad;
        // document.getElementById('codigoPostal').value = codigoPostal;
        document.getElementById('latitude').value = latitude;
        document.getElementById('longitude').value = longitude;

        // // Mover el mapa a la ubicación seleccionada
        // map.setView(e.geocode.center, 16);
        // if(circleFind){
        //     map.removeLayer(circleFind);
        // }
        // circleFind=L.circle(e.geocode.center, 30000).addTo(map);
    }).addTo(map);

    const geocoderContainer = geocoder.getContainer();
    document.getElementById('buscador').appendChild(geocoderContainer);


    // Array para almacenar marcadores personalizados
    const markers = [];


    // Evento para añadir los parkings al mapa
    // parkings es un array de objetos que contiene la información de los parkings
    // como los guardo en una variable de thymeleaf, puedo acceder a ellos desde aquí

    console.log(parkings); // Para verificar que los datos se están pasando correctamente

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
        const marker = L.marker([parking.latitude, parking.longitude], { icon: marca }).addTo(map);
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

    document.getElementById('customRange3').addEventListener('input', function () {
        rangeInput = this.value;

        document.getElementById('rangeValue').innerHTML = rangeInput;

        if(circleFind){
            map.removeLayer(circleFind);
        }
        
        console.log(latlng);
        console.log(rangeInput);
        
        circleFind = L.circle(latlng, parseInt(rangeInput)).addTo(map);
    });
});