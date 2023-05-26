package com.volodymyrkozlov.marketplace.handler.property;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for market price margin.
 */
@Validated
@Getter
@ToString
@EqualsAndHashCode
@ConfigurationProperties(prefix = "market-price-margin")
public class MarketPriceMarginProperties {
    @NotNull
    @Positive
    private Double bidPercentage;
    @NotNull
    @Positive
    private Double askPercentage;

    /**
     * Sets the bid percentage.
     *
     * @param bidPercentage The bid percentage to be set.
     */
    public void setBidPercentage(Double bidPercentage) {
        this.bidPercentage = bidPercentage;
    }

    /**
     * Sets the ask percentage.
     *
     * @param askPercentage The ask percentage to be set.
     */
    public void setAskPercentage(Double askPercentage) {
        this.askPercentage = askPercentage;
    }
}
