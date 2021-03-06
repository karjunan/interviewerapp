package com.softvision.config;

import com.softvision.controller.EmailController;
import com.softvision.controller.InterviewController;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;

@Configuration
@ApplicationPath("/interview")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(InterviewController.class);
        register(EmailController.class);
    }
    
   /* @Bean
    public TemplateEngine getTemplateEngine() {
    	
    	return new TemplateEngine();
    }*/
}
