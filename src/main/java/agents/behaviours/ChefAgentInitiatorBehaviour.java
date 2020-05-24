package agents.behaviours;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

import java.util.Vector;

class ChefAgentInitiatorBehaviour extends ContractNetInitiator {
    @Override
    protected void handlePropose(ACLMessage propose, Vector acceptances) {
        System.out.println("PROPOSE TASKHELP - Agent " + propose.getSender().getName()
                + " proposed with value " + propose.getContent());
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
        System.out.println("Handling all responses");
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
