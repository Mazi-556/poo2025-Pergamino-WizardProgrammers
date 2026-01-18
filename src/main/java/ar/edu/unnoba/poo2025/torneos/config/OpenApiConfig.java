package ar.edu.unnoba.poo2025.torneos.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

//Esto es Swagger. Basicamente es una API que genera una pagina web interactiva que muestra todos los endpoints del proyecto.
//No sirve para mucho, solamente lo queria probar.
//Si queres hacerlo, inicia el proyecto y abri http://localhost:8080/swagger-ui/index.html
 

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Torneos API",
        version = "1.0",
        description = "Documentación para Wizard Programmers"
    )
)
@SecurityScheme(
    name = "bearerAuth", // Nombre del esquema
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
public class OpenApiConfig {
}