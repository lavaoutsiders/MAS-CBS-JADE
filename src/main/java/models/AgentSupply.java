package models;

import exceptions.InSufficientSupplyException;
import exceptions.OversupplyException;

public class AgentSupply {
    private int cleanDishes;
    private int maxCleanDishes = 100;

    private int nbSlicedFish;
    private int maxSlicedFish = 200;

    private int riceRolls;
    private int maxRiceRolls = 100;

    private int soups;
    private int maxSoups = 10;

    public int getRiceRolls() {
        return riceRolls;
    }

    public int getCleanDishes() {
        return cleanDishes;
    }

    public int getNbSlicedFish() {
        return nbSlicedFish;
    }

    public int getSoups() {
        return soups;
    }

    public boolean hasRiceRoll() {
        return this.riceRolls > 0;
    }

    public boolean hasCleanDish() {
        return this.cleanDishes > 0;
    }

    public boolean hasSlicedFish() {
        return this.nbSlicedFish > 0;
    }

    public boolean hasSoup() {
        return this.soups > 0;
    }

    public void incrementBeverage() throws OversupplyException {
        if (this.riceRolls + 1 > this.maxRiceRolls) {
            throw new OversupplyException();
        }
        this.riceRolls++;
    }

    public void incrementCleanDishes() throws OversupplyException {
        if (this.cleanDishes + 1 > this.maxCleanDishes) {
            throw new OversupplyException();
        }
        this.cleanDishes++;
    }

    public void incrementSoup() throws OversupplyException {
        if (this.soups + 1 > this.maxSoups) {
            throw new OversupplyException();
        }
        this.soups++;
    }

    public void incrementSlicedFish() throws OversupplyException {
        if (this.nbSlicedFish + 1 > this.maxSlicedFish) {
            throw new OversupplyException();
        }
        this.soups++;
    }

    public void decrementBeverage() throws InSufficientSupplyException {
        if (this.riceRolls - 1 < 0 ) {
            throw new InSufficientSupplyException();
        }
        this.riceRolls--;
    }

    public void decrementSoup() throws InSufficientSupplyException {
        if (this.soups - 1 < 0) {
            throw new InSufficientSupplyException();
        }
        this.soups--;
    }

    public void decrementCleanDishes() throws InSufficientSupplyException {
        if (this.cleanDishes - 1 < 0) {
            throw new InSufficientSupplyException();
        }
        this.cleanDishes--;
    }

    public void decrementSlicedFish() throws InSufficientSupplyException {
        if (this.nbSlicedFish - 1 < 0) {
            throw new InSufficientSupplyException();
        }
        this.nbSlicedFish--;
    }
}
