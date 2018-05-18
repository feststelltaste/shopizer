package com.salesmanager.core.business.configuration;

import com.salesmanager.core.integration.HibernateListenerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.salesmanager.core.model"})
@ComponentScan({"com.salesmanager.core.business.services","com.salesmanager.core.business.utils","com.salesmanager.core.integration"})
@ImportResource("classpath:/spring/shopizer-core-context.xml")
public class CoreApplicationConfiguration {
}
