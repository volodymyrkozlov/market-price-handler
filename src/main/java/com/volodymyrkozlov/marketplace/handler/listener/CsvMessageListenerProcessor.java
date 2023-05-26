package com.volodymyrkozlov.marketplace.handler.listener;

import com.volodymyrkozlov.marketplace.handler.dto.MarketPriceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.stream.Stream;

/**
 * A message listener processor for CSV messages.
 */
@Slf4j
@Component
@RequiredArgsConstructor
class CsvMessageListenerProcessor extends AbstractStringMessageListenerProcessor {
    private final CSVFormat csvFormat;
    private final MarketPriceMarginCalculator marketPriceMarginCalculator;
    private final ExternalServiceClient externalServiceClient;

    /**
     * Converts a CSV string message to a stream of MarketPriceDto objects.
     *
     * @param message The CSV string message to be converted.
     * @return A stream of MarketPriceDto objects.
     * @throws IllegalStateException if an error occurs during parsing the CSV string message.
     */
    @Override
    Stream<MarketPriceDto> convert(String message) {
        var reader = new StringReader(message);
        try {
            return csvFormat.parse(reader)
                    .stream()
                    .map(CsvMessageListenerProcessor::toMarketPriceDto);
        } catch (IOException e) {
            throw new IllegalStateException(
                    String.format("An error occurred during parsing the CSV string message:[%s]", message));
        }
    }

    /**
     * Processes the stream of MarketPriceDto objects representing spot data.
     *
     * @param spotData The stream of MarketPriceDto objects representing spot data.
     * @return A stream of processed MarketPriceDto objects.
     */
    @Override
    Stream<MarketPriceDto> processSpot(Stream<MarketPriceDto> spotData) {
        return spotData.map(marketPriceMarginCalculator::calculateMargin);
    }

    /**
     * Publishes the market price response by sending it to the external service.
     *
     * @param marketPrice The MarketPriceDto object representing the market price.
     */
    @Override
    void publishResponse(MarketPriceDto marketPrice) {
        log.info("{} is published to REST endpoint", marketPrice);
        externalServiceClient.sendMarketPrice(marketPrice);
    }


    private static MarketPriceDto toMarketPriceDto(CSVRecord csvRecord) {
        return new MarketPriceDto(
                /* id= */ Long.valueOf(csvRecord.get(0).trim()),
                /* instrumentName= */ csvRecord.get(1).trim(),
                /* bid= */ new BigDecimal(csvRecord.get(2).trim()),
                /* ask= */ new BigDecimal(csvRecord.get(3).trim()),
                /* timestamp= */ csvRecord.get(4).trim());
    }
}
