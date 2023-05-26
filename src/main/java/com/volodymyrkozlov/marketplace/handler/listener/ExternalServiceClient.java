package com.volodymyrkozlov.marketplace.handler.listener;

import com.volodymyrkozlov.marketplace.handler.dto.MarketPriceDto;

/**
 * Represents an external service client.
 */
interface ExternalServiceClient {

    /**
     * Sends a market price DTO to the external service.
     *
     * @param marketPriceDto The market price DTO to be sent.
     */
    void sendMarketPrice(MarketPriceDto marketPriceDto);
}
