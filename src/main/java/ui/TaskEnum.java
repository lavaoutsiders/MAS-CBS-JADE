package ui;

public enum  TaskEnum {

    CUT_FISH(10),
    KNEED_RICE(5),
    MAKE_SUSHI(8),
    WASH_DISH(15);

    private final int duration;

    TaskEnum(int seconds) {
        this.duration = seconds * 1000;
    }

    public int getDuration() {
        return duration;
    }
}
