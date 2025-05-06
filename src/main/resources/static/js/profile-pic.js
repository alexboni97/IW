document.addEventListener("DOMContentLoaded", () => {
    const avatarForm = document.getElementById("avatarform");
    const avatarPreview = document.getElementById("avatar");
    const profilePic = document.getElementById("profilePic"); // Imagen del perfil grande
    


    avatarForm.addEventListener("submit", (event) => {
        event.preventDefault(); // Evita que el formulario recargue la página

        const formData = new FormData(avatarForm);

        fetch(avatarForm.action, {
            method: "POST",
            body: formData
        })
        .then(response => {
            if (!response.ok) {
                throw new Error("Error al actualizar la foto de perfil");
            }
            return response.json(); // Suponiendo que el servidor devuelve un JSON con la URL de la nueva imagen
        })
        .then(data => { 

            avatarPreview.src = data.newPicUrl; // Evita el caché
            // Actualiza la imagen del perfil grande
            profilePic.src = data.newPicUrl; // Evita el caché
        })
        .catch(error => {
            console.error("Error:", error);
            alert("Hubo un problema al actualizar la foto de perfil.");
        });
    });
});