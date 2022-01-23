package enums;

public enum DeliveryServiceWorkload {
    CRITICAL("1.6"),
    HIGH("1.4"),
    ABOVE_NORMAL("1.2"),
    DEFAULT("1");

    DeliveryServiceWorkload(String factor) {
        this.factor = factor;
    }

    private final String factor;

    public String getWorkloadFactor() {
        return this.factor;
    }
}
