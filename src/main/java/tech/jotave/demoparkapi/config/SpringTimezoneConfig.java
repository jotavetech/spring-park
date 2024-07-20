package tech.jotave.demoparkapi.config;

import jakarta.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class SpringTimezoneConfig {

    // após a aplicação iniciar o método construtor vai ser executado (timezone)
    @PostConstruct
    public void timezoneConfig() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
    }
}
