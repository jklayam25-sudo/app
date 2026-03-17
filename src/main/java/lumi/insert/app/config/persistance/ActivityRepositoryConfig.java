package lumi.insert.app.config.persistance;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder; 
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean; 

import com.zaxxer.hikari.HikariDataSource;

public class ActivityRepositoryConfig {
    
    LocalContainerEntityManagerFactoryBean activityEntityManagerFactoryBean(EntityManagerFactoryBuilder builder, @Qualifier("activity-hikari-ds") HikariDataSource dataSource){
        return builder
        .dataSource(dataSource)
        .packages("lumi.insert.app.activitycore.entity")
        .persistenceUnit("activity")
        .build();
    }
    
}
