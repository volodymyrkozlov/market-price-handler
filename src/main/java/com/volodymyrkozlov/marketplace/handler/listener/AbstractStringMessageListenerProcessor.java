package com.volodymyrkozlov.marketplace.handler.listener;

import com.volodymyrkozlov.marketplace.handler.dto.MarketPriceDto;

import java.util.stream.Stream;

/**
 * An abstract class representing a message listener processor for string messages.
 */
abstract class AbstractStringMessageListenerProcessor implements MessageListener<String> {

    /**
     * Processes the incoming string message.
     *
     * @param message The string message to be processed.
     */
    @Override
    public void onMessage(String message) {
        var convertedMessage = this.convert(message);

        this.processSpot(convertedMessage).forEach(this::publishResponse);
    }

    /**
     * Converts the string message to a stream of MarketPriceDto objects.
     *
     * @param message The string message to be converted.
     * @return A stream of MarketPriceDto objects.
     */
    abstract Stream<MarketPriceDto> convert(String message);

    /**
     * Processes the stream of MarketPriceDto objects representing spot data.
     *
     * @param spotData The stream of MarketPriceDto objects representing spot data.
     * @return A stream of processed MarketPriceDto objects.
     */
    abstract Stream<MarketPriceDto> processSpot(Stream<MarketPriceDto> spotData);

    /**
     * Publishes the market price response.
     *
     * @param marketPrice The MarketPriceDto object representing the market price.
     */
    abstract void publishResponse(MarketPriceDto marketPrice);
}
