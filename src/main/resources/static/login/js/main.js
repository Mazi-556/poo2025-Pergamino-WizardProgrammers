document.addEventListener('DOMContentLoaded', () => {
    // 1. Buscamos el formulario por la etiqueta (ID) que le pusimos antes
    const loginForm = document.getElementById('loginForm');

    // 2. Le decimos: "Cuando alguien intente enviarte (submit)..."
    loginForm.addEventListener('submit', async (event) => {
        
        // ¡ALTO! Evitamos que el formulario recargue la página (comportamiento por defecto)
        event.preventDefault();

        // 3. Capturamos lo que escribió el usuario
        // OJO: En tu HTML el input de email tiene id="username"
        const email = document.getElementById('username').value; 
        const password = document.getElementById('password').value;

        // 4. Empaquetamos los datos en un objeto (como una caja)
        // Las claves 'email' y 'password' deben coincidir EXACTAMENTE con tu DTO en Java
        const datosParaEnviar = {
            email: email,
            password: password
        };

        try {
            // 1. La variable se llama 'respuesta'
            const respuesta = await fetch('http://localhost:8080/admin/auth', { 
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(datosParaEnviar)
            });

            // 2. Aquí DEBES usar 'respuesta', NO 'response'
            if (respuesta.ok) { 
                // 3. Aquí TAMBIÉN DEBES usar 'respuesta', NO 'response'
                const result = await respuesta.json(); 
                
                localStorage.setItem('jwt_token', result.token);
                window.location.href = "/panel.html"; 

            } else {
                // Si el backend dice 401 (no autorizado), cae acá.
                alert("Error: Usuario o contraseña incorrectos");
            }
        } catch (error) {
            // Si hay un error de código arriba (como 'response is not defined'), cae acá.
            console.error("Error real:", error); // Mira esto en la consola (F12)
            alert("Ocurrió un error (Revisa la consola con F12 para ver el detalle).");
        }
    });
});