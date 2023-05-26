package com.volodymyrkozlov.marketplace.handler.listener;

import com.volodymyrkozlov.marketplace.handler.dto.MarketPriceDto;
import com.volodymyrkozlov.marketplace.handler.property.MarketPriceMarginProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class MarketPriceMarginCalculatorTest {
    private MarketPriceMarginCalculator marketPriceMarginCalculator;

    @BeforeEach
    void init() {
        var marketPriceMarginProperties = new MarketPriceMarginProperties();
        marketPriceMarginProperties.setAskPercentage(0.1);
        marketPriceMarginProperties.setBidPercentage(0.1);

        this.marketPriceMarginCalculator = new MarketPriceMarginCalculator(marketPriceMarginProperties);
    }

    // Ask and bid values are pretty long. They can be formatted for sure if needed. There are no requirements in the
    // task to format bid and ask values. It also possible that the Web UI can format it into the desirable format.
    @Test
    void calculateMargin() {
        // given
        var marketPriceDto =
                new MarketPriceDto(106L, "EUR/USD", new BigDecimal("1.1000"), new BigDecimal("1.2000"), "12:01:02:002");

        // when
        var processMarketPriceDto = marketPriceMarginCalculator.calculateMargin(marketPriceDto);

        // then
        assertThat(processMarketPriceDto.id()).isEqualTo(106L);
        assertThat(processMarketPriceDto.instrumentName()).isEqualTo("EUR/USD");
        assertThat(processMarketPriceDto.bid()).isEqualTo(new BigDecimal("1.0988999999999999999771016501171061463537625968456268310546875000"));
        assertThat(processMarketPriceDto.ask()).isEqualTo(new BigDecimal("1.2012000000000000000249800180540660221595317125320434570312500000"));
        assertThat(processMarketPriceDto.instant()).isEqualTo("12:01:02:002");
    }
}
