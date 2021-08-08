package com.kota65535.config;


import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.convert.DataAccessStrategy;
import org.springframework.data.jdbc.core.convert.DefaultDataAccessStrategy;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.jdbc.core.convert.SqlGeneratorSource;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.repository.config.DialectResolver;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@EnableJdbcRepositories(
    jdbcOperationsRef = "jdbcOperationsDb3",
    transactionManagerRef = "transactionManagerDb3",
    dataAccessStrategyRef = "dataAccessStrategyDb3",
    basePackages = {
        "com.kota65535.repository.three"
    }
)
@EnableAutoConfiguration(
    exclude = {DataSourceAutoConfiguration.class, JdbcRepositoriesAutoConfiguration.class}
)
@Configuration
@ConfigurationPropertiesScan
public class Db3Config {

  @Bean
  @Qualifier("db3")
  public NamedParameterJdbcOperations jdbcOperationsDb3(
      @Qualifier("db3") DataSource dataSourceDb3
  ) {
    return new NamedParameterJdbcTemplate(dataSourceDb3);
  }

  @Bean
  @Qualifier("db3")
  @ConfigurationProperties(prefix = "spring.datasources.three")
  public HikariDataSource dataSourceDb3() {
    return DataSourceBuilder.create().type(HikariDataSource.class).build();
  }

  @Bean
  @Qualifier("db3")
  public DataAccessStrategy dataAccessStrategyDb3(
      @Qualifier("db3") NamedParameterJdbcOperations operations,
      JdbcConverter jdbcConverter,
      JdbcMappingContext context
  ) {
    return new DefaultDataAccessStrategy(
        new SqlGeneratorSource(context, jdbcConverter,
            DialectResolver.getDialect(operations.getJdbcOperations())),
        context, jdbcConverter, operations);
  }

  @Bean
  @Qualifier("db3")
  public PlatformTransactionManager transactionManagerDb3(
      @Qualifier("db3") final DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }
}
