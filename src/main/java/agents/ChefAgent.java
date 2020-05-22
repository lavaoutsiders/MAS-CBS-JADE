package agents;

import com.google.common.collect.Sets;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetInitiator;
import jade.proto.ContractNetResponder;
import models.TaskEnum;
import org.jetbrains.annotations.NotNull;

import java.util.Vector;

class ChefAgentInitiatorBehaviour extends ContractNetInitiator {
    @Override
    protected void handlePropose(ACLMessage propose, Vector acceptances) {
        super.handlePropose(propose, acceptances);
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

    @Override
    protected void handleAllResultNotifications(Vector resultNotifications) {
        super.handleAllResultNotifications(resultNotifications);
    }

    public ChefAgentInitiatorBehaviour(Agent a, ACLMessage cfp) {
        super(a, cfp);

    }
}

class ChefAgentResponderBehaviour extends ContractNetResponder {
    @Override
    protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException, FailureException, NotUnderstoodException {
        ACLMessage reply = cfp.createReply();

        boolean shouldPropose = false;

        return reply;

    }

    @Override
    protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
        ACLMessage reply = cfp.createReply();
        //TODO
        return reply;
    }

    @Override
    protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
        super.handleRejectProposal(cfp, propose, reject);
    }

    @Override
    protected void handleOutOfSequence(ACLMessage cfp, ACLMessage propose, ACLMessage msg) {
        super.handleOutOfSequence(cfp, propose, msg);
    }

    @Override
    protected void handleOutOfSequence(ACLMessage msg) {
        super.handleOutOfSequence(msg);
    }

    public ChefAgentResponderBehaviour(Agent a, MessageTemplate mt) {
        super(a, mt);
    }
}

public class ChefAgent extends BaseAgent implements IChefAgent {

    public ChefAgent() {
        super(Sets.newHashSet(TaskEnum.KNEED_RICE, TaskEnum.CUT_FISH));
        this.addBehaviour(new ChefAgentResponderBehaviour(this, getContractNetTemplate()));
//        this.addBehaviour(new ChefAgentInitiatorBehaviour(this, ));
    }

    @Override
    public int calculateAgentScore(int x, int y) {
        return 0;
    }
}
