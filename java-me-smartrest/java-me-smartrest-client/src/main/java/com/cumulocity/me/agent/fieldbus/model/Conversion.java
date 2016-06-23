package com.cumulocity.me.agent.fieldbus.model;

public class Conversion {
    public static final Conversion DEFAULT = new Conversion(1, 1, 0, 0);

    private final int multiplier;
    private final int divisor;
    private final int offset;
    private final int decimalPlaces;

    public Conversion(int multiplier, int divisor, int offset, int decimalPlaces) {
        this.multiplier = multiplier;
        this.divisor = divisor;
        this.offset = offset;
        this.decimalPlaces = decimalPlaces;
    }

    public int getDecimalPlaces() {
        return decimalPlaces;
    }

    public int getDivisor() {
        return divisor;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public int getOffset() {
        return offset;
    }
}
