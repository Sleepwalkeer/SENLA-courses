package eu.senla.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@WebAppConfiguration
@Configuration
@ComponentScan(basePackages = {"eu.senla.controller"})
public class ServletConfigurationTest implements WebMvcConfigurer {

}
