package models;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class OrderDescription implements Serializable, Description {
    private final ItemsEnum item;
    private final Coordinate coordinate;

    public OrderDescription(@NotNull ItemsEnum item, @NotNull Coordinate coordinate) {
        this.item = item;
        this.coordinate = coordinate;
    }

    public ItemsEnum getItem() {
        return item;
    }

    @Override
    public Coordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public String toString() {
        return "Order description - item : " + item.toString() + " coordinate " + coordinate.toString();
    }
}
