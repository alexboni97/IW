const now = new Date();

now.setMinutes(Math.ceil(now.getMinutes() / 15) * 15); // Round up to the next quarter-hour
const startDate = now.toISOString().split('T')[0];
const startTime = now.toTimeString().split(':').slice(0, 2).join(':');

now.setHours(now.getHours() + 1); // Add 1 hour
const endDate = now.toISOString().split('T')[0];
const endTime = now.toTimeString().split(':').slice(0, 2).join(':');

if (!document.getElementById('startDate').value) {
    document.getElementById('startDate').value = startDate;
}
if (!document.getElementById('startTime').value) {
    document.getElementById('startTime').value = startTime;
}
if (!document.getElementById('endDate').value) {
    document.getElementById('endDate').value = endDate;
}
if (!document.getElementById('endTime').value) {
    document.getElementById('endTime').value = endTime;
}

//TODO añadir onchange para que si se cambia la fecha de inicio, se cambie la fecha de fin y viceversa

document.getElementById('startDate').addEventListener('change', function() {
    if(this.value > document.getElementById('endDate').value) 
        document.getElementById('endDate').value = this.value; 
});

document.getElementById('endDate').addEventListener('change', function() {
    if(this.value < document.getElementById('startDate').value) 
        document.getElementById('startDate').value = this.value; 
});

//FIXME: Cambiar para que actualice bien la hora de fin al cambiar la hora de inicio y la fecha de fin si es necesario
document.getElementById('startTime').addEventListener('change', function() {
    const startTime = this.value.split(':');
    const startHour = parseInt(startTime[0], 10);
    const startMinutes = parseInt(startTime[1], 10);

    const startDateInput = document.getElementById('startDate');
    const endDateInput = document.getElementById('endDate');
    const endTimeInput = document.getElementById('endTime');

    let endDate = new Date(startDateInput.value);
    endDate.setHours(startHour + 1, startMinutes);

    const currentEndDate = new Date(endDateInput.value + 'T' + endTimeInput.value);

    if (endDate > currentEndDate) {
        if (endDate.getDate() !== new Date(startDateInput.value).getDate()) {
            endDateInput.value = endDate.toISOString().split('T')[0];
        }
        endTimeInput.value = endDate.toTimeString().split(':').slice(0, 2).join(':');
    }
});

document.getElementById('endTime').addEventListener('change', function() {
    const endTime = this.value.split(':');
    const endHour = parseInt(endTime[0], 10);
    const endMinutes = parseInt(endTime[1], 10);

    const startDateInput = document.getElementById('startDate');
    const startTimeInput = document.getElementById('startTime');
    const endDateInput = document.getElementById('endDate');

    let startDate = new Date(startDateInput.value);
    startDate.setHours(endHour - 1, endMinutes);

    const currentStartDate = new Date(startDateInput.value + 'T' + startTimeInput.value);

    if (startDate < currentStartDate) {
        if (startDate.getDate() !== new Date(endDateInput.value).getDate()) {
            startDateInput.value = startDate.toISOString().split('T')[0];
        }
        startTimeInput.value = startDate.toTimeString().split(':').slice(0, 2).join(':');
    }
});