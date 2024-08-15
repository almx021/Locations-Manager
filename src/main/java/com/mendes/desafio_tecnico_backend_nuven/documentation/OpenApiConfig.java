package com.mendes.desafio_tecnico_backend_nuven.documentation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

/**
 * Configuration class responsible for initializing the API documentation with API information and Author's contact information.
 * 
 * <p> The generated documentation can be found {@see <a href="http://localhost:8080/swagger-ui/index.html">here</a>} while the api is running.
 * 
 * @see http://localhost:8080/v3/api-docs
 */
@Configuration
public class OpenApiConfig {
        private Contact Contact() {
        Contact myContact = new Contact();
        myContact.setName("André Lucas");
        myContact.setEmail("alm021@hotmail.com");
        myContact.setUrl("https://github.com/almx021/");
        return myContact;

    }

    @Bean
    public OpenAPI SpringOpenAPI() {
    return new OpenAPI()
            .info(new Info().title("Locations Manager Rest API")
            .description("API that allows the user to create and manage locations")
            .version("v1.0")
            .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0.html"))
            .contact(Contact()));
    }
    
}
