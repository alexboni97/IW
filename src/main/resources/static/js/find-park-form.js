const now = new Date();
const startDate = now.toISOString().split('T')[0];
const startTime = now.toTimeString().split(':').slice(0, 2).join(':');

now.setMinutes(Math.ceil(now.getMinutes() / 15) * 15); // Round up to the next quarter-hour
const startDatePlus30 = now.toISOString().split('T')[0];
const startTimePlus30 = now.toTimeString().split(':').slice(0, 2).join(':');

now.setHours(now.getHours() + 1); // Add 1 hours
const endDate = now.toISOString().split('T')[0];
const endTime = now.toTimeString().split(':').slice(0, 2).join(':');

document.getElementById('startDate').value = startDatePlus30;
document.getElementById('startTime').value = startTimePlus30;
document.getElementById('endDate').value = endDate;
document.getElementById('endTime').value = endTime;

//TODO añadir onchange para que si se cambia la fecha de inicio, se cambie la fecha de fin y viceversa