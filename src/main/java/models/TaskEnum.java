package models;

import jade.domain.FIPAAgentManagement.ServiceDescription;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public enum  TaskEnum implements Serializable {

    WARM_SOUP(15),
    PLATING(1),
    CUT_FISH(10),
    KNEED_RICE(5),
    MAKE_SUSHI(8),
    WASH_DISH(15),
    RECEIVE_ORDER(5),
    START_ORDER(1);
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
        serviceDescription.setName(this.toString());
        return serviceDescription;
    }
}
