package lumi.insert.app.config.persistance;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration; 
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableJpaRepositories(
    basePackages = "lumi.insert.app.activitycore.repository",  
    entityManagerFactoryRef = "activityEntityManagerFactory",
    transactionManagerRef = "activityTransactionManager"
)
public class ActivityRepositoryConfig {
     
    @Bean("activityEntityManagerFactory")
    LocalContainerEntityManagerFactoryBean activityEntityManagerFactoryBean(EntityManagerFactoryBuilder builder, @Qualifier("activity-hikari-ds") HikariDataSource dataSource){
        return builder
        .dataSource(dataSource)
        .packages("lumi.insert.app.activitycore.entity")
        .persistenceUnit("activity")
        .build();
    }
 
    @Bean("activityTransactionManager")
    PlatformTransactionManager activityTransactionManager(@Qualifier("activityEntityManagerFactory") EntityManagerFactory core){
        return new JpaTransactionManager(core);
    }
    
}
