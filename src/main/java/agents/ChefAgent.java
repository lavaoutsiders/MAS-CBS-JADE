package agents;

import com.google.common.collect.Sets;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetInitiator;
import jade.proto.ContractNetResponder;
import main.MainController;
import models.AgentSupply;
import models.Coordinate;
import models.TaskEnum;
import org.jetbrains.annotations.NotNull;
import ui.StatusEnum;

import java.io.Serializable;
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

    private double calculateCost(@NotNull  Coordinate opponentCoordinate) {
        if (!(this.getAgent() instanceof ChefAgent)) {
            return Double.POSITIVE_INFINITY; // Impossible...
        }
        return ((ChefAgent) this.getAgent()).getCoordinate()
                .euclideanDistance(opponentCoordinate) * 10 + 100000; // TODO
    }

    private boolean shouldAcceptProposal() {
        return true; // TODO: Make it effective
    }

    @Override
    protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException, FailureException, NotUnderstoodException {
        System.out.println("Chef agent " + myAgent.getName() + " received cfp for order with content " + cfp.getContent());
        ACLMessage reply = cfp.createReply();
        Coordinate coordinate;
        try {
            Serializable object = cfp.getContentObject();

            if (! ( object instanceof Coordinate)) {
                reply.setPerformative(ACLMessage.REFUSE);
                return reply;
            }
            coordinate = (Coordinate) object;
        } catch (UnreadableException e) {
            reply.setPerformative(ACLMessage.REFUSE);
            return reply;
        }
        double cost = calculateCost(coordinate);

        // TODO !!! Check when to refuse
        if (!shouldAcceptProposal()) {
            reply.setPerformative(ACLMessage.REFUSE);
            return reply;
        }

        reply.setPerformative(ACLMessage.PROPOSE);
        reply.setContent(String.valueOf(cost));
        this.mainController.setUIComponentState(myAgent, StatusEnum.RECEIVED_NEW_MESSAGE);
        return reply;

    }

    @Override
    protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
        System.out.println("Offer accepted for " + this.getAgent().getName());
        ACLMessage reply = cfp.createReply();

        //TODO
        return reply;
    }

    @Override
    protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
        System.out.println("Offer reject :( for " + this.getAgent().getName() );
    }

    @Override
    protected void handleOutOfSequence(ACLMessage cfp, ACLMessage propose, ACLMessage msg) {
        super.handleOutOfSequence(cfp, propose, msg);
    }

    @Override
    protected void handleOutOfSequence(ACLMessage msg) {
        super.handleOutOfSequence(msg);
    }

    private MainController mainController;

    public MainController getMainController() {
        return mainController;
    }

    public ChefAgentResponderBehaviour(BaseAgent a, MessageTemplate mt, @NotNull MainController mainController) {
        super(a, mt);
        this.mainController = mainController;
    }
}

public class ChefAgent extends BaseAgent implements IChefAgent {
    private AgentSupply agentSupply;

    public ChefAgent(@NotNull MainController mainController, @NotNull Coordinate coordinate) {
        super(mainController,Sets.newHashSet(TaskEnum.START_ORDER, TaskEnum.KNEED_RICE, TaskEnum.CUT_FISH),
                coordinate);
        this.addBehaviour(new ChefAgentResponderBehaviour(this, getContractNetTemplate(), mainController));
        this.agentSupply = new AgentSupply();
    }

    @Override
    public int calculateAgentScore(int x, int y) {
        return 0;
    }
}
