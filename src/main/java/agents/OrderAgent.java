package agents;

import com.google.common.collect.Sets;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;
import models.TaskEnum;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

class OrderAgentInitiatorBehaviour extends ContractNetInitiator {

    public OrderAgentInitiatorBehaviour(Agent a, ACLMessage cfp) {
        super(a, cfp);
    }

    @Override
    protected void handlePropose(ACLMessage propose, Vector acceptances) {
        super.handlePropose(propose, acceptances);
    }

    @Override
    protected void handleAllResultNotifications(Vector resultNotifications) {
        super.handleAllResultNotifications(resultNotifications);
    }

    @Override
    protected void handleRefuse(ACLMessage refuse) {
        super.handleRefuse(refuse);
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        super.handleInform(inform);
    }

    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {
        super.handleAllResponses(responses, acceptances);
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
