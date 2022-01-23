package models;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder(toBuilder = true)
@Value
public class Delivery {
    @NonNull
    Integer distanceKm;
    @NonNull
    Boolean isLargeSize;
    @NonNull
    Boolean isFragile;
}
