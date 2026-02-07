package com.lezhin.webtoonservice.storage.db.core.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import jakarta.persistence.EntityManagerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import javax.sql.DataSource

@Configuration
internal class CoreDataSourceConfig {
    @Bean
    @ConfigurationProperties(prefix = "storage.datasource.core")
    fun coreHikariConfig(): HikariConfig = HikariConfig()

    @Bean(name = ["coreDataSource"])
    fun coreDataSource(
        @Qualifier("coreHikariConfig") hikariConfig: HikariConfig,
    ): HikariDataSource = HikariDataSource(hikariConfig)

    @Bean(name = ["coreEntityManagerFactory"])
    fun coreEntityManagerFactory(
        @Qualifier("coreDataSource") dataSource: DataSource,
    ): LocalContainerEntityManagerFactoryBean =
        LocalContainerEntityManagerFactoryBean().apply {
            setDataSource(dataSource)
            setPackagesToScan("com.lezhin.webtoonservice.storage.db.core")
            setJpaVendorAdapter(HibernateJpaVendorAdapter())
        }

    @Bean(name = ["coreTransactionManager"])
    fun coreTransactionManager(
        @Qualifier("coreEntityManagerFactory") entityManagerFactory: EntityManagerFactory,
    ): JpaTransactionManager = JpaTransactionManager(entityManagerFactory)
}
