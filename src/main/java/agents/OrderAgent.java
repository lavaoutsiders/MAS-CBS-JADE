package agents;

import agents.behaviours.ContractNetInitiatorBehaviour;
import com.google.common.collect.Sets;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetResponder;
import main.MainController;
import models.*;
import org.jetbrains.annotations.NotNull;
import ui.StatusEnum;
import utils.DFUtils;

import java.io.IOException;
import java.util.Objects;
import java.util.Vector;

class OrderAgentInitiatorBehaviour extends ContractNetInitiatorBehaviour {

    public OrderAgentInitiatorBehaviour(Agent a, ACLMessage cfp) {
        super(a, cfp);
    }

    @Override
    protected void handlePropose(ACLMessage propose, Vector acceptances) {
        ((BaseAgent) this.getAgent()).getMainController().printLogLine("PROPOSE - Agent " + propose.getSender().getName() + " proposed with value: " + propose.getContent());
    }

    @Override
    protected void handleAllResultNotifications(Vector resultNotifications) {
        super.handleAllResultNotifications(resultNotifications);
    }

    @Override
    protected void handleRefuse(ACLMessage refuse) {
        ((BaseAgent) this.getAgent()).getMainController().printLogLine("REFUSE - Agent " + refuse.getSender().getName() + " refused the task :( ");
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        if (inform.getContent().equals("Dispatched")) {
            ((BaseAgent) this.getAgent()).getMainController().printLogLine("INFORM DISPATCHED - Agent " + inform.getSender().getName() + " dispatched the task");
            return;
        }
        ((BaseAgent) this.getAgent()).getMainController().printLogLine("INFORM - Agent " +  inform.getSender().getName() + " successfully performed the task");
    }


}

public class OrderAgent extends BaseAgent implements IOrderAgent {



    public OrderAgent(@NotNull MainController mainController, @NotNull Coordinate coordinate) {
        super(mainController, Sets.newHashSet(TaskEnum.RECEIVE_ORDER),
                coordinate);
        this.addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                MessageTemplate template = MessageTemplate.and(
                    getContractNetTemplate(ACLMessage.INFORM),
                    new MessageTemplate((MessageTemplate.MatchExpression) aclMessage -> {
                        try {
                            return aclMessage != null && (aclMessage.getContentObject() != null && (aclMessage.getContentObject() instanceof OrderDescription));
                        } catch (UnreadableException e) {
                            e.printStackTrace();
                            return false;
                        }
                    } )
                );

                ACLMessage msg = blockingReceive(template, 40);
                if (msg == null) return;
                OrderDescription finishedOrder = null;
                try {
                    if (!(msg.getContentObject() instanceof OrderDescription)){
                        return;
                    }
                    finishedOrder = (OrderDescription) msg.getContentObject();
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
                if (finishedOrder == null){
                    return;
                }

                getMainController().setUIComponentState(OrderAgent.this, StatusEnum.IDLE);
            }
        });
    }


    public void submitNewOrder(ItemsEnum itemsEnum) {
        ACLMessage message = new ACLMessage(ACLMessage.CFP);
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        message.setReplyByDate(getReplyDeadline());
        try {
            message.setContentObject(new OrderDescription(itemsEnum, OrderAgent.this.getCoordinate()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Objects.requireNonNull(
                DFUtils.searchDF(TaskEnum.START_ORDER.toString(), OrderAgent.this))
                .forEach(message::addReceiver);
        this.addBehaviour(new OrderAgentInitiatorBehaviour(this, message));

        System.out.println("Order agent submitted a new order with item " + itemsEnum.toString());

    }

}
