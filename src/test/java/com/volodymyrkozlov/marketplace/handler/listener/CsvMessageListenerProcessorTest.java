package com.volodymyrkozlov.marketplace.handler.listener;

import com.volodymyrkozlov.marketplace.handler.dto.MarketPriceDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;


@SpringBootTest(properties = {"spring.config.name=application-test"})
class CsvMessageListenerProcessorTest {
    private static final String SPOT_PRICE_FEED = """
            106, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001
            107, EUR/JPY, 119.60,119.90,01-06-2020 12:01:02:002
            108, GBP/USD, 1.2500,1.2560,01-06-2020 12:01:02:002
            109, GBP/USD, 1.2499,1.2561,01-06-2020 12:01:02:100
            110, EUR/JPY, 119.61,119.91,01-06-2020 12:01:02:110
            """;

    @Autowired
    private CsvMessageListenerProcessor csvMessageListenerProcessor;
    @MockBean
    private ExternalServiceClient externalServiceClient;

    // Ask and bid values are pretty long. They can be formatted for sure if needed. There are no requirements in the
    // task to format bid and ask values. It also possible that the Web UI can format it into the desirable format.
    @Test
    void onMessage_processesFeed_publishesMessageToRestEndpoint() {
        // when
        csvMessageListenerProcessor.onMessage(SPOT_PRICE_FEED);

        // then
        var marketPriceDtoEurUsd = new MarketPriceDto(
                106L,
                "EUR/USD",
                new BigDecimal("1.0988999999999999999771016501171061463537625968456268310546875000"),
                new BigDecimal("1.2012000000000000000249800180540660221595317125320434570312500000"),
                "01-06-2020 12:01:01:001");
        var marketPriceDtoEurJpy = new MarketPriceDto(
                107L,
                "EUR/JPY",
                new BigDecimal("119.48039999999999999751032486727808645810000598430633544921875000"),
                new BigDecimal("120.01990000000000000249592013723543004743987694382667541503906250"),
                "01-06-2020 12:01:02:002");
        var marketPriceDtoGbpUsd = new MarketPriceDto(
                108L,
                "GBP/USD",
                new BigDecimal("1.2487499999999999999739791478603478935838211327791213989257812500"),
                new BigDecimal("1.2572560000000000000261457522299224365269765257835388183593750000"),
                "01-06-2020 12:01:02:002");
        var marketPriceDtoGbpUsdSecond = new MarketPriceDto(
                109L,
                "GBP/USD",
                new BigDecimal("1.2486500999999999999739812295285190657523344270884990692138671875"),
                new BigDecimal("1.2573561000000000000261478338980936086954898200929164886474609375"),
                "01-06-2020 12:01:02:100");
        var marketPriceDtoEurJpySecond = new MarketPriceDto(
                110L,
                "EUR/JPY",
                new BigDecimal("119.49038999999999999751011670046096924124867655336856842041015625"),
                new BigDecimal("120.02991000000000000249612830405254726429120637476444244384765625"),
                "01-06-2020 12:01:02:110");

        verify(externalServiceClient).sendMarketPrice(marketPriceDtoEurUsd);
        verify(externalServiceClient).sendMarketPrice(marketPriceDtoEurJpy);
        verify(externalServiceClient).sendMarketPrice(marketPriceDtoGbpUsd);
        verify(externalServiceClient).sendMarketPrice(marketPriceDtoGbpUsdSecond);
        verify(externalServiceClient).sendMarketPrice(marketPriceDtoEurJpySecond);
    }

    @Configuration
    @EnableAutoConfiguration
    @ComponentScan("com.volodymyrkozlov.marketplace.handler")
    static class CsvMessageListenerTestConfig {

    }
}
