package com.volodymyrkozlov.marketplace.handler.listener;

import com.volodymyrkozlov.marketplace.handler.dto.MarketPriceDto;
import com.volodymyrkozlov.marketplace.handler.property.MarketPriceMarginProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Represents a market price margin calculator.
 */
@Component
@RequiredArgsConstructor
class MarketPriceMarginCalculator {
    private final MarketPriceMarginProperties marketPriceMarginProperties;

    /**
     * Calculates the margin for a market price DTO.
     *
     * @param marketPriceDto The market price DTO to calculate the margin for.
     * @return The market price DTO with the calculated margin.
     */
    public MarketPriceDto calculateMargin(MarketPriceDto marketPriceDto) {
        var ask = this.calculateAskMargin(marketPriceDto);
        var bid = this.calculateBidMargin(marketPriceDto);

        return new MarketPriceDto(
                marketPriceDto.id(),
                marketPriceDto.instrumentName(),
                bid,
                ask,
                marketPriceDto.instant());
    }

    private BigDecimal calculateBidMargin(MarketPriceDto marketPriceDto) {
        var bid = marketPriceDto.bid();

        return bid.subtract(calculateMargin(bid, marketPriceMarginProperties.getBidPercentage()));
    }

    private BigDecimal calculateAskMargin(MarketPriceDto marketPriceDto) {
        var ask = marketPriceDto.ask();

        return ask.add(calculateMargin(ask, marketPriceMarginProperties.getAskPercentage()));
    }

    private static BigDecimal calculateMargin(BigDecimal value,
                                              Double percentage) {
        return value.multiply(new BigDecimal(percentage / 100.0));
    }
}
