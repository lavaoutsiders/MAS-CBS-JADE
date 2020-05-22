package agents;

import com.google.common.collect.Sets;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import models.TaskEnum;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

class OrderAgentInitiatorBehaviour extends CyclicBehaviour {

    public void action() {

    }
}

public class OrderAgent extends BaseAgent implements IOrderAgent {


    public OrderAgent() {
        super(Sets.newHashSet(TaskEnum.RECEIVE_ORDER));
    }

    public void sendNewOrder() {
        System.out.println("Order agent submitted a new order");
    }
}
