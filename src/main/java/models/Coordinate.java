package models;

import java.io.Serializable;

public class Coordinate implements Serializable {

    private final int x;
    private final int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public double euclideanDistance(Coordinate coordinate){
        return Math.sqrt(Math.pow(this.x - coordinate.getX(), 2) + Math.pow(this.y - coordinate.getY(),2));
    }

    @Override
    public String toString() {
        return "x: " + this.getX() + ", y: " + this.getY();
    }
}
