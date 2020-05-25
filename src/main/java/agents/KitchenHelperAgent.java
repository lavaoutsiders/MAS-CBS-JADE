package agents;

import com.google.common.collect.Sets;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;
import main.MainController;
import main.MainControllerImpl;
import models.Coordinate;
import models.TaskEnum;
import org.jetbrains.annotations.NotNull;

class KitchenHelperResponderBehaviour extends ContractNetResponder {

    @Override
    protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException, FailureException, NotUnderstoodException {
        return super.handleCfp(cfp);
    }

    @Override
    protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
        return super.handleAcceptProposal(cfp, propose, accept);
    }

    public KitchenHelperResponderBehaviour(Agent a, MessageTemplate mt) {
        super(a, mt);
    }
}

public class KitchenHelperAgent extends BaseAgent {
    public KitchenHelperAgent(@NotNull MainController mainController, @NotNull Coordinate coordinate) {
        super(mainController, Sets.newHashSet(TaskEnum.WASH_DISH),
                coordinate);
    }


}
