package delivery;

import enums.DeliveryServiceWorkload;
import exceptions.DeliveryException;
import models.Delivery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import services.DeliveryService;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static enums.DeliveryServiceWorkload.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeliveryCostTests {

    private DeliveryService deliveryService;

    @BeforeEach
    void prepareService(){
        deliveryService = new DeliveryService();
    }

    private static Stream<Arguments> provideArgumentsForPositive() {
        return Stream.of(
                Arguments.of(0, false, false, CRITICAL, new BigDecimal("400")),
                Arguments.of(0, true, true, DEFAULT, new BigDecimal("550")),
                Arguments.of(0, false, true, ABOVE_NORMAL, new BigDecimal("540")),

                Arguments.of(2, false, false, CRITICAL, new BigDecimal("400")),
                Arguments.of(2, true, true, DEFAULT, new BigDecimal("550")),
                Arguments.of(2, true, false, ABOVE_NORMAL, new BigDecimal("400")),

                Arguments.of(10, false, false, CRITICAL, new BigDecimal("400")),
                Arguments.of(10, true, true, DEFAULT, new BigDecimal("600")),
                Arguments.of(10, false, true, HIGH, new BigDecimal("700")),

                Arguments.of(30, false, false, CRITICAL, new BigDecimal("480")),
                Arguments.of(30, true, true, DEFAULT, new BigDecimal("700")),
                Arguments.of(30, true, false, HIGH, new BigDecimal("560")),

                Arguments.of(31, false, false, CRITICAL, new BigDecimal("640")),
                Arguments.of(31, true, false, DEFAULT, new BigDecimal("500"))
        );
    }

    @ParameterizedTest(name = "Расчета доставки (позитивные). Расстояние:{0}, большой размер:{1}, хрупкий груз:{2}, загруженность доставки:{3}")
    @MethodSource("provideArgumentsForPositive")
    void calculateDeliveryCostPositive(Integer distance, Boolean isLarge, Boolean isFragile,
                                              DeliveryServiceWorkload workload, BigDecimal expectedSum) {
        var deliveryParameters = buildDelivery(distance, isLarge, isFragile);
        var actualSum = deliveryService.calculateDeliveryCost(deliveryParameters, workload);
        assertEquals(actualSum.stripTrailingZeros(), expectedSum.stripTrailingZeros());
    }

    @Test
    @DisplayName("Проверка кейса, когда хрупкий груз доставляется больше, чем на 30км")
    void checkExceptionWhenIsFragileMoreThan30() {
        var deliveryParameters = buildDelivery(31, true, true);
        var exception = assertThrows(DeliveryException.class, () -> deliveryService.calculateDeliveryCost(deliveryParameters, HIGH));
        assertThat(exception.getMessage(), containsString("Хрупкий груз не доставляется на расстояние >30км"));
    }

    private Delivery buildDelivery(Integer distance, Boolean isLarge, Boolean isFragile) {
        return Delivery.builder()
                .distanceKm(distance)
                .isLargeSize(isLarge)
                .isFragile(isFragile)
                .build();
    }
}
