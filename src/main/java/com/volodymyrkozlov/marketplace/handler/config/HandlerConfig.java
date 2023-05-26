package com.volodymyrkozlov.marketplace.handler.config;

import com.volodymyrkozlov.marketplace.handler.property.MarketPriceMarginProperties;
import org.apache.commons.csv.CSVFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Configuration class for the handler.
 */
@Configuration
@Import(MarketPriceMarginProperties.class)
public class HandlerConfig {

    @Bean
    public CSVFormat csvFormat() {
        return CSVFormat.DEFAULT.builder().build();
    }
}
