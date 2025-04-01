document.addEventListener("DOMContentLoaded", function () {
  document.querySelectorAll(".accordion-collapse").forEach(function (accordion) {
    accordion.addEventListener("shown.bs.collapse", function (event) {
      const mapaDiv = event.target.querySelector(".map-container");
      console.log(mapaDiv);

      if (mapaDiv && !mapaDiv.dataset.loaded) {
        const address = mapaDiv.getAttribute("data-address");

        if (address) {
          fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(address)}`)
            .then(response => response.json())
            .then(data => {
              if (data.length > 0) {
                const lat = parseFloat(data[0].lat);
                const lng = parseFloat(data[0].lon);

                // Crear un nuevo mapa con ID único
                console.log("aaaaaaaaaaaaa");
                const map = L.map(mapaDiv.id).setView([lat, lng], 15);
                console.log(mapaDiv.id);
                console.log("aaaaaaaaaaaaa");

                L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
                  attribution: "© OpenStreetMap contributors",
                }).addTo(map);
                console.log("bbbbbbbbbbbb");

                // Agregar un marcador con popup
                L.marker([lat, lng]).addTo(map)
                  .bindPopup(`<strong>Ubicación del Parking</strong><br>${address}`)
                  .openPopup();
                  console.log("ccccccccccccccccc");

                setTimeout(() => {
                  map.invalidateSize();
                }, 300);

                mapaDiv.dataset.loaded = "true"; // Marcarlo como cargado
              } else {
                console.error("No se encontraron coordenadas para:", address);
              }
            })
            .catch(error => console.error("Error obteniendo coordenadas:", error));
        }
      }
    });
  });
});
