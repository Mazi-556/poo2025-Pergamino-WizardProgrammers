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
            // 5. Usamos 'fetch' (el mensajero) para enviar los datos a Spring Boot
            // Asumimos que tu backend corre en el puerto 8080
            const respuesta = await fetch('http://localhost:8080/admin/auth', {
                method: 'POST', // Método HTTP
                headers: {
                    'Content-Type': 'application/json' // Le avisamos al backend que le enviamos JSON
                },
                body: JSON.stringify(datosParaEnviar) // Convertimos la "caja" de JS a texto JSON
            });

            if (response.ok) {
                const result = await response.json();
                
                //Aca se guarda el token en el localStorage. Esto hace que el usuario no tenga que abrir la consola para obtener el
                //token
                localStorage.setItem('jwt_token', result.token);

                //Luego de guardar el token, redirijimos a una pagina. (en este caso [pagina que se va a usar]) //TODO
                window.location.href = "/panel.html"; 

            } else {
                alert("Error: Usuario o contraseña incorrectos");
            }
        } catch (error) {
            console.error("Error de conexión:", error);
            alert("No se pudo conectar con el servidor (¿Está encendido el Backend?).");
        }
    });
});