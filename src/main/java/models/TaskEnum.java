package models;

import jade.domain.FIPAAgentManagement.ServiceDescription;
import org.jetbrains.annotations.NotNull;

public enum  TaskEnum {

    CUT_FISH(10),
    KNEED_RICE(5),
    MAKE_SUSHI(8),
    WASH_DISH(15),
    RECEIVE_ORDER(5);
    private final int duration;

    TaskEnum(int seconds) {
        this.duration = seconds * 1000;
    }

    public int getDuration() {
        return duration;
    }

    public ServiceDescription toServiceDescription() {
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType(this.toString());
        return serviceDescription;
    }
}
