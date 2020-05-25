package agents.behaviours;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

import java.util.Vector;

class ChefAgentInitiatorBehaviour extends ContractNetInitiatorBehaviour {
    @Override
    protected void handlePropose(ACLMessage propose, Vector acceptances) {
        System.out.println("PROPOSE TASKHELP - Agent " + propose.getSender().getName()
                + " proposed with value " + propose.getContent());
    }



    @Override
    protected void handleRefuse(ACLMessage refuse) {

        System.out.println("REFUSE TASKHELP - " + refuse.getSender().getName() + " refused to help the task");
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        super.handleInform(inform);
    }


    @Override
    protected void handleAllResultNotifications(Vector resultNotifications) {
        super.handleAllResultNotifications(resultNotifications);
    }

    public ChefAgentInitiatorBehaviour(Agent a, ACLMessage cfp) {
        super(a, cfp);

    }
}
