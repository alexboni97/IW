document.addEventListener("DOMContentLoaded", function () {
  document.querySelectorAll(".accordion-collapse").forEach(function (accordion) {
    accordion.addEventListener("shown.bs.collapse", function (event) {
      const mapaDiv = event.target.querySelector(".map-container");
      console.log(mapaDiv);
      let errordiv = event.target.querySelector(".error");

      if (mapaDiv && !mapaDiv.dataset.loaded) {
        const address = mapaDiv.getAttribute("data-address");

        if (address) {
          fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(address)}`)
            .then(response => response.json())
            .then(data => {
              console.log(data);
              if (data.length > 0) {
                console.log(data);
                const lat = parseFloat(data[0].lat);
                const lng = parseFloat(data[0].lon);

                // Crear un nuevo mapa con ID único
                
                const map = L.map(mapaDiv.id).setView([lat, lng], 15);
                

                L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
                  attribution: "© OpenStreetMap contributors",
                }).addTo(map);
                

                // Agregar un marcador con popup
                L.marker([lat, lng]).addTo(map)
                  .bindPopup(`<strong>Ubicación del Parking</strong><br>${address}`)
                  .openPopup();
                  
                

                setTimeout(() => {
                  map.invalidateSize();
                }, 300);

                mapaDiv.dataset.loaded = "true"; // Marcarlo como cargado
              } else {
                const map = L.map(mapaDiv.id).setView([40.404711, -3.710862], 15);
                L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
                  attribution: "© OpenStreetMap contributors",
                }).addTo(map);
                mapaDiv.dataset.loaded = "true"; // Marcarlo como cargado
                
                document.getElementById(errordiv.id).innerHTML = "Dirección no encontrada";
                document.getElementById(errordiv.id).className = "alert alert-danger";
                
                console.error("No se encontraron coordenadas para:", address);
                
                // map.on('movestart', function(){
                //   document.getElementById(errordiv.id).style.display = 'block';
                // });

                // var customDivIcon = L.divIcon({
                //   html: '<div class="alert alert-danger">Dirección no encontrada</div>',
                //   iconSize: [150, 40], // Ajusta el tamaño del icono según el contenido
                //   iconAnchor: [75, 20]  // Ajusta la posición del icono
                // });
          
                // // Agregar el marcador con el divIcon
                // L.marker([40.404711, -3.710862], { icon: customDivIcon }).addTo(map);

                // L.popup().setLatLng([40.404711, -3.710862])
                // .setContent(`<div class="alert alert-danger">Dirección no encontrada</div>`)
                // .openOn(map);
                // L.bindPopup("Dirección no encontrada").openPopup().addTo(map);
              }
            })
            .catch(error => console.error("Error obteniendo coordenadas:", error));
        }
      }
    });
  });
});
