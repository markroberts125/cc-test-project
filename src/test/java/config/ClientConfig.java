package config;

import internal.RequestHandler;
import internal.client.Client;
import internal.client.RestAssuredClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

    @Bean
    Client client(){
        return new RestAssuredClient();
    }

    @Bean(destroyMethod = "retireToken")
    RequestHandler requestHandler(Client client){
        return new RequestHandler(client);
    }
}
