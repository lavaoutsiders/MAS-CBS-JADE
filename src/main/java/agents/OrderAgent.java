package agents;

import com.google.common.collect.Sets;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;
import models.ItemsEnum;
import models.TaskEnum;
import org.jetbrains.annotations.NotNull;
import utils.DFUtils;

import java.util.*;

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

    public void submitNewOrder(ItemsEnum itemsEnum) {
        ACLMessage message = new ACLMessage(ACLMessage.CFP);
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        message.setReplyByDate(getReplyDeadline());
        message.setContent(itemsEnum.toString());

        Objects.requireNonNull(
                DFUtils.searchDF(TaskEnum.START_ORDER.toString(), OrderAgent.this))
                .forEach(message::addReceiver);
        send(message);
        System.out.println("Order agent submitted a new order with item " + itemsEnum.toString());
    }
}
