package agents.behaviours;

import agents.BaseAgent;
import agents.ChefAgent;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ResultTreeType;
import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetResponder;
import main.MainController;
import models.*;
import org.jetbrains.annotations.NotNull;
import ui.StatusEnum;

import java.io.Serializable;

public abstract class ContractNetResponderBehaviour extends ContractNetResponder {



    public abstract boolean shouldAcceptProposal();





    public abstract void resumeWork(ACLMessage cfp);


    @Override
    protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
        System.out.println("Offer accepted for " + this.getAgent().getName());
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

    protected abstract void handleWork(ACLMessage accept, Description description);

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

    public ContractNetResponderBehaviour(BaseAgent a, MessageTemplate mt, @NotNull MainController mainController) {
        super(a, mt);
        this.mainController = mainController;
    }

    @Override
    protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException, FailureException, NotUnderstoodException {
        Serializable result = null;
        try {
            result = cfp.getContentObject();
            System.out.println("Agent " + this.getAgent().getName() + " received cfp for order with content " +
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
        System.out.println("PROPOSE AGENT - Agent" + this.getAgent().getName() + " proposed with value " + cost);
        reply.setPerformative(ACLMessage.PROPOSE);

        reply.setContent(String.valueOf(cost));
        this.getMainController().setUIComponentState(myAgent, StatusEnum.RECEIVED_NEW_MESSAGE);
        return reply;

    }

    protected abstract double calculateCost(Description description);

}

