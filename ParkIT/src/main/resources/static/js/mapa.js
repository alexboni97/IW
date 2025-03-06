document.addEventListener("DOMContentLoaded", function () {
    const map = L.map('mapa').setView([40.416775, -3.703790], 13); // Posición por defecto (Madrid)

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors'
    }).addTo(map);


    const parkingElement = document.getElementById('selected-parking');
    if (parkingElement) {
        const parking = JSON.parse(parkingElement.textContent || "{}");

        if (parking.latitude && parking.longitude) {
            const marker = L.marker([parking.latitude, parking.longitude]).addTo(map);
            marker.bindPopup(`<b>${parking.name}</b><br>${parking.address}`).openPopup();
            map.setView([parking.latitude, parking.longitude], 16);
        }
    }
});
