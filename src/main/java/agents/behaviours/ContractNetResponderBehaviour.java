package agents.behaviours;

import agents.BaseAgent;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetResponder;
import main.MainController;
import models.Description;
import org.jetbrains.annotations.NotNull;
import ui.StatusEnum;

import java.io.Serializable;

public abstract class ContractNetResponderBehaviour extends ContractNetResponder {



    public boolean shouldAcceptProposal() {
        return ! ((BaseAgent) this.getAgent()).getWorkingStatus();
    }





    public abstract void resumeWork(ACLMessage cfp);


    @Override
    protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {

        ((BaseAgent) this.getAgent()).getMainController().printLogLine("Offer accepted for " + this.getAgent().getName());
        ACLMessage reply = accept.createReply();
        try {
            if (cfp.getContentObject() instanceof Description) {
                this.handleWork(accept, ((Description) cfp.getContentObject()));
                reply.setPerformative(ACLMessage.INFORM);
            }
        } catch (UnreadableException e) {
            e.printStackTrace();
        }


        return reply;
    }

    public void workForDuration(ACLMessage cfp, long duration) {
        this.getMainController().setUIComponentState(this.getAgent(), StatusEnum.WORKING);
        this.getAgent().addBehaviour(new WakerBehaviour(this.getAgent(), duration) {
            @Override
            protected void onWake() {
                resumeWork(cfp);
            }
        });
    }

    protected abstract void handleWork(ACLMessage accept, Description description);

    @Override
    protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
        ((BaseAgent) this.getAgent()).getMainController().printLogLine("Offer reject :( for " + this.getAgent().getName() );
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

    public ContractNetResponderBehaviour(BaseAgent a, MessageTemplate mt, @NotNull MainController mainController) {
        super(a, mt);
        this.mainController = mainController;
    }

    @Override
    protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException, FailureException, NotUnderstoodException {
        Serializable result = null;
        try {
            result = cfp.getContentObject();
            ((BaseAgent) this.getAgent()).getMainController().printLogLine("Agent " + this.getAgent().getName() + " received cfp for order with content " +
                    (result == null ? cfp.getContent() : result.toString()) );
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
        ACLMessage reply = cfp.createReply();
        Description description;

        if (! ( result instanceof Description) || !shouldAcceptProposal()) {
            reply.setPerformative(ACLMessage.REFUSE);
            return reply;
        }
        description = ((Description) result);
        double cost = calculateCost(description);
//        ((BaseAgent) this.getAgent()).getMainController().printLogLine("PROPOSE AGENT - Agent" + this.getAgent().getName() + " proposed with value " + cost);
        reply.setPerformative(ACLMessage.PROPOSE);

        reply.setContent(String.valueOf(cost));
        this.getMainController().setUIComponentState(myAgent, StatusEnum.RECEIVED_NEW_MESSAGE);
        return reply;

    }

    protected abstract double calculateCost(Description description);

}

