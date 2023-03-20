package eu.senla.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@EnableAspectJAutoProxy
@Configuration
@ComponentScan(basePackages = {"eu.senla.controller"})
public class ServletConfiguration implements WebMvcConfigurer {
}
