package com.volodymyrkozlov.marketplace.handler.dto;

import java.math.BigDecimal;

/**
 * Represents a market price data transfer object (DTO).
 */
public record MarketPriceDto(Long id,
                             String instrumentName,
                             BigDecimal bid,
                             BigDecimal ask,
                             String instant) {
}
