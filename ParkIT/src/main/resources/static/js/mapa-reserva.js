document.addEventListener("DOMContentLoaded", function () {
    const map = L.map('mapa').setView([40.416775, -3.703790], 13); // Posición por defecto (Madrid)

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors'
    }).addTo(map);



    if (parking.latitude && parking.longitude) {
        const marker = L.marker([parking.latitude, parking.longitude]).addTo(map);
        marker.bindPopup(`<b>${parking.name}</b><br>${parking.address}`).openPopup();
        map.setView([parking.latitude, parking.longitude], 16);
    }

    function calcularPrecio(){
        let errorMsg = document.getElementById("errorMsg");

        let fechaInicio=document.getElementById("ini-f").value;
        let fechaFin=document.getElementById("fin-f").value;
        let horaInicio=document.getElementById("ini-h").value;
        let horaFin=document.getElementById("fin-h").value;

        // if (!fechaInicio || !fechaFin || !horaInicio || !horaFin) {
        //     errorMsg.textContent = "⚠️ Todos los campos de fecha y hora son obligatorios.";
        //     document.getElementById("totalPrecio").textContent = "0.00";
        //     return;
        // }

        let inicio = new Date(`${fechaInicio}T${horaInicio}:00`);
        let fin = new Date(`${fechaFin}T${horaFin}:00`);
        let diferenciaMilisegundos = fin - inicio;
        if (diferenciaMilisegundos <= 0) {
            errorMsg.textContent = "⚠️ La fecha y hora de fin deben ser posteriores a la de inicio.";
            document.getElementById("fin-f").style.border = "2px solid red";
            document.getElementById("fin-h").style.border = "2px solid red";
            document.getElementById("totalPrecio").textContent = "0.00";
            return;
        }else{
            errorMsg.textContent = " ";
            document.getElementById("fin-f").style.border = "";
            document.getElementById("fin-h").style.border = "";
        }
        let horasTotales = diferenciaMilisegundos / (1000 * 60 * 60);
        let precioTotal = horasTotales * precioPorHora;

        document.getElementById("totalPrecio").textContent = precioTotal.toFixed(2);
        document.getElementById("totalPrice").value = precioTotal.toFixed(2);
        
    }
    document.getElementById("ini-f").addEventListener("input",calcularPrecio);
    document.getElementById("fin-f").addEventListener("input",calcularPrecio);
    document.getElementById("ini-h").addEventListener("input",calcularPrecio);
    document.getElementById("fin-h").addEventListener("input",calcularPrecio);
    
    calcularPrecio();
});

document.querySelector("form").addEventListener("submit", function(event) {
    let vehicleSelect = document.getElementById("vehiculo");
    let errorDiv = document.getElementById("errorNotification");

    if (vehicleSelect.value === "Selecciona 1" || vehicleSelect.value === "") {
        event.preventDefault();
        errorDiv.textContent = "Debes seleccionar un vehículo antes de reservar.";
        errorDiv.classList.remove("d-none");
    }
});
