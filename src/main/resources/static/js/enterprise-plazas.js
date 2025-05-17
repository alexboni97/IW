document.addEventListener('DOMContentLoaded', function () {

    // Update the state every 5 seconds
    setInterval(fetchSpots, 5000); 

    function fetchSpots() {
        for(var i=0; i < spots.length; i++) {
            var spot = spots[i];
            var state = document.getElementById("state-"+spot.id);
            var reserves = document.getElementById("reserves-"+spot.id);
            var today = new Date().toISOString().slice(0, 10);

            state.innerHTML = "prueba";
        }
    }

});