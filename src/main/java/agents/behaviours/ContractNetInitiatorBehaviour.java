package agents.behaviours;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

import java.util.Optional;
import java.util.Spliterator;
import java.util.Vector;
import java.util.stream.StreamSupport;

public class ContractNetInitiatorBehaviour extends ContractNetInitiator {
    public ContractNetInitiatorBehaviour(Agent a, ACLMessage cfp) {
        super(a, cfp);
    }

    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {

        Spliterator<ACLMessage> iterator = responses.spliterator();
        Optional<ACLMessage> bestOffer = StreamSupport.stream(iterator, false)
                .filter(msg-> msg.getPerformative() == ACLMessage.PROPOSE)
                .min((msg1, msg2) -> (int) (Double.parseDouble(msg1.getContent()) - Double.parseDouble(msg2.getContent())));
        if (! bestOffer.isPresent()) {
            System.out.println("!!! No agent is able to handle the order.");
            // TODO: add to queue
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
