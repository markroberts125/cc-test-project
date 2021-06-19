package config;

import internal.RequestHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

    @Bean
    RequestHandler requestHandler(){
        return new RequestHandler();
    }
}
