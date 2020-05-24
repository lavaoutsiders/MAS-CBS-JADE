package agents.behaviours;

import agents.BaseAgent;
import agents.ChefAgent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import main.MainController;
import models.*;
import org.jetbrains.annotations.NotNull;

public class ChefAgentOrderResponderBehaviour extends ChefAgentResponderBehaviour {

    public ChefAgentOrderResponderBehaviour(BaseAgent a, MessageTemplate mt, @NotNull MainController mainController) {
        super(a, mt, mainController);
    }

    @Override
    public boolean shouldAcceptProposal() {
        return true; // TODO Make it effective
    }

    @Override
    public void resumeWork(ACLMessage cfp) {

    }

    @Override
    protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
        ACLMessage reply = super.handleAcceptProposal(cfp, propose, accept);
        try {
            if (cfp.getContentObject() instanceof Description) {
                this.handleWork(accept, (Description) cfp.getContentObject());
            }
        } catch (UnreadableException e) {
            e.printStackTrace();
        }


        return reply;
    }

    @Override
    protected void handleWork(ACLMessage accept, Description description) {
        if (!(description instanceof OrderDescription)) return;
        long waitDuration;
        ChefAgent chefAgent = (ChefAgent) this.getAgent();
        TaskWorker taskWorker = new TaskWorker(new OrderDescription(((OrderDescription) description).getItem(), ((ChefAgent) this.getAgent()).getCoordinate()),
                this);

        taskWorker.executeTask();
    }

    @Override
    public double calculateCost(@NotNull Description orderDescription) {
        if (!(this.getAgent() instanceof ChefAgent) || !(orderDescription instanceof OrderDescription)) {
            return Double.POSITIVE_INFINITY; // Impossible...
        }
        return ((ChefAgent) this.getAgent()).getCoordinate()
                .euclideanDistance(orderDescription.getCoordinate())
                + ((ChefAgent) this.getAgent()).calculateMainTaskCost(((OrderDescription) orderDescription).getItem()); // TODO
    }

}
