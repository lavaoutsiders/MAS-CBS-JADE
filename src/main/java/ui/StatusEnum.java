package ui;

import java.awt.*;

public enum StatusEnum {
    RECEIVED_NEW_MESSAGE("R", Color.GREEN),
    WORKING("W", Color.orange),
    IDLE("A", Color.LIGHT_GRAY);


    StatusEnum(String value, Color color) {
        this.value = value;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public String getValue() {
        return value;
    }

    private final String value;
    private final Color color;
}
