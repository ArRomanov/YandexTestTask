package models;

import lombok.Builder;
import lombok.Value;

@Builder(toBuilder = true)
@Value
public class Delivery {
    private Integer distanceKm;
    private Boolean isLargeSize;
    private Boolean isFragile;
}
