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

    private void handleWork(@NotNull ACLMessage accept, @NotNull ItemsEnum item) {
        long waitDuration;
        ChefAgent chefAgent = (ChefAgent) this.getAgent();
        TaskWorker taskWorker = new TaskWorker(new OrderDescription(item, ((ChefAgent) this.getAgent()).getCoordinate()),
                this);

        taskWorker.executeTask();

    }

    @Override
    protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
        ACLMessage reply = super.handleAcceptProposal(cfp, propose, accept);
        try {
            if (cfp.getContentObject() instanceof OrderDescription) {
                this.handleWork(accept, ((OrderDescription) cfp.getContentObject()).getItem());
            }
        } catch (UnreadableException e) {
            e.printStackTrace();
        }


        return reply;
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
