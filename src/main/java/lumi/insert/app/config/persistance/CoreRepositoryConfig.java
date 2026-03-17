package lumi.insert.app.config.persistance;

import org.springframework.boot.jpa.EntityManagerFactoryBuilder; 
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean; 

import com.zaxxer.hikari.HikariDataSource;

public class CoreRepositoryConfig {
    
    LocalContainerEntityManagerFactoryBean coreEntityManagerFactoryBean(EntityManagerFactoryBuilder builder, HikariDataSource dataSource){
        return builder
        .dataSource(dataSource)
        .packages("lumi.insert.app.core.entity")
        .persistenceUnit("primary")
        .build();
    }
    
}
