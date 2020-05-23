package agents;

import com.google.common.collect.Sets;
import com.google.common.collect.Streams;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;
import main.MainController;
import main.MainControllerImpl;
import models.Coordinate;
import models.ItemsEnum;
import models.TaskEnum;
import org.jetbrains.annotations.NotNull;
import utils.DFUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.StreamSupport;

class OrderAgentInitiatorBehaviour extends ContractNetInitiator {

    public OrderAgentInitiatorBehaviour(Agent a, ACLMessage cfp) {
        super(a, cfp);
    }

    @Override
    protected void handlePropose(ACLMessage propose, Vector acceptances) {
        System.out.println("PROPOSE - Agent " + propose.getSender().getName() + " proposed with value: " + propose.getContent());
    }

    @Override
    protected void handleAllResultNotifications(Vector resultNotifications) {
        super.handleAllResultNotifications(resultNotifications);
    }

    @Override
    protected void handleRefuse(ACLMessage refuse) {
        System.out.println("REFUSE - Agent " + refuse.getSender().getName() + " refused the task :( ");
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        System.out.println("INFORM - Agent " + inform.getSender().getName() + " successfully performed the task");
    }

    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {

        Spliterator<ACLMessage> iterator = responses.spliterator();
        Optional<ACLMessage> bestOffer = StreamSupport.stream(iterator, false)
                .filter(msg-> msg.getPerformative() == ACLMessage.PROPOSE)
                .min((msg1, msg2) -> (int) (Double.parseDouble(msg1.getContent()) - Double.parseDouble(msg2.getContent())));
        if (! bestOffer.isPresent()) {
            System.out.println("! No agent is able to handle the order.");
            return;
        }
        ACLMessage bestReply = bestOffer.get().createReply();
        bestReply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
        acceptances.add(bestReply);
        iterator = responses.spliterator();
        iterator.forEachRemaining(aclMessage -> {
            if (aclMessage != bestOffer.get()) {
                ACLMessage reply = aclMessage.createReply();
                reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                acceptances.add(reply);
            }
        });

    }
}

public class OrderAgent extends BaseAgent implements IOrderAgent {



    public OrderAgent(@NotNull MainController mainController, @NotNull Coordinate coordinate) {
        super(mainController, Sets.newHashSet(TaskEnum.RECEIVE_ORDER),
                coordinate);

    }

    public void submitNewOrder(ItemsEnum itemsEnum) {
        ACLMessage message = new ACLMessage(ACLMessage.CFP);
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        message.setReplyByDate(getReplyDeadline());
        message.setContent(itemsEnum.toString());
        try {
            message.setContentObject(OrderAgent.this.getCoordinate());
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
