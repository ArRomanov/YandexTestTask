package services;

import enums.DeliveryServiceWorkload;
import exceptions.DeliveryException;
import lombok.SneakyThrows;
import models.Delivery;

import java.math.BigDecimal;

import static enums.DeliveryServiceWorkload.*;


public class DeliveryService {
    public BigDecimal calculateDeliveryCost(Delivery deliveryParameters, DeliveryServiceWorkload workload) {
        var deliverySum = new BigDecimal(0);
        var minDeliverySum = new BigDecimal(400);

        deliverySum = deliverySum.add(calculateDistanceSum(deliveryParameters));
        deliverySum = deliverySum.add(calculateSumBySize(deliveryParameters));
        deliverySum = deliverySum.add(calculateSumByFragile(deliveryParameters));
        deliverySum = deliverySum.multiply(getWorkloadFactor(workload));
        return deliverySum.compareTo(minDeliverySum) < 0 ? minDeliverySum : deliverySum;
    }

    @SneakyThrows
    private BigDecimal calculateDistanceSum(Delivery delivery) {
        var distance = delivery.getDistanceKm();

        if (distance > 30 && delivery.getIsFragile())
            throw new DeliveryException("Хрупкий груз не доставляется на расстояние >30км");
        // TODO Уточнить, включены ли в стоимость граничные значения
        if (distance <= 2)
            return new BigDecimal(50);
        if (distance <= 10)
            return new BigDecimal(100);
        if (distance <= 30)
            return new BigDecimal(200);

        return new BigDecimal(300);
    }

    private BigDecimal calculateSumBySize(Delivery delivery) {
        var isLarge = delivery.getIsLargeSize();
        return isLarge ? new BigDecimal(200) : new BigDecimal(100);
    }

    private BigDecimal calculateSumByFragile(Delivery delivery) {
        var isFragile = delivery.getIsFragile();
        return isFragile ? new BigDecimal(300) : new BigDecimal(0);
    }

    private BigDecimal getWorkloadFactor(DeliveryServiceWorkload workload) {
        switch (workload) {
            case CRITICAL:
                return new BigDecimal(CRITICAL.getWorkloadFactor());
            case HIGH:
                return new BigDecimal(HIGH.getWorkloadFactor());
            case ABOVE_NORMAL:
                return new BigDecimal(ABOVE_NORMAL.getWorkloadFactor());
            default:
                return new BigDecimal(DEFAULT.getWorkloadFactor());
        }
    }
}
