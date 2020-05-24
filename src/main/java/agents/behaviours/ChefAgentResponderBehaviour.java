package agents.behaviours;

import agents.BaseAgent;
import agents.ChefAgent;
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
import models.Coordinate;
import models.Description;
import models.OrderDescription;
import models.TaskEnum;
import org.jetbrains.annotations.NotNull;
import ui.StatusEnum;

import java.io.Serializable;

public abstract class ChefAgentResponderBehaviour extends ContractNetResponder {



    public abstract boolean shouldAcceptProposal();



    public void startWork(TaskEnum task, ACLMessage cfp){
        this.getAgent().addBehaviour(new WakerBehaviour(this.getAgent(), task.getDuration()) {
            @Override
            protected void onWake() {
                if (this.getAgent() instanceof BaseAgent) {
                    ChefAgentResponderBehaviour.this.resumeWork(cfp);
                }
            }
        });
    }

    public abstract void resumeWork(ACLMessage cfp);


    @Override
    protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
        System.out.println("Offer accepted for " + this.getAgent().getName());
        this.mainController.setUIComponentState(this.getAgent(), StatusEnum.WORKING);
        return cfp.createReply();
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

    @Override
    protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException, FailureException, NotUnderstoodException {
        try {
            System.out.println("Chef agent " + this.getAgent().getName() + " received cfp for order with content " + cfp.getContentObject().toString());
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
        ACLMessage reply = cfp.createReply();
        Description description;
        try {
            Serializable object = cfp.getContentObject();

            if (! ( object instanceof Description)) {
                reply.setPerformative(ACLMessage.REFUSE);
                return reply;
            }
            description = ((Description) object);
        } catch (UnreadableException e) {
            reply.setPerformative(ACLMessage.REFUSE);
            return reply;
        }
        double cost = calculateCost(description);

        // TODO !!! Check when to refuse
        if (!shouldAcceptProposal()) {
            reply.setPerformative(ACLMessage.REFUSE);
            return reply;
        }

        reply.setPerformative(ACLMessage.PROPOSE);
        reply.setContent(String.valueOf(cost));
        this.getMainController().setUIComponentState(myAgent, StatusEnum.RECEIVED_NEW_MESSAGE);
        return reply;

    }

    protected abstract double calculateCost(Description description);

}

