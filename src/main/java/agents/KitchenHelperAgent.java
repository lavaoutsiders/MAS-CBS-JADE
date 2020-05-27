package agents;

import agents.behaviours.ContractNetResponderBehaviour;
import com.google.common.collect.Sets;
import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import main.MainController;
import models.*;
import org.jetbrains.annotations.NotNull;

class KitchenHelperResponderBehaviour extends ContractNetResponderBehaviour {

    public KitchenHelperResponderBehaviour(KitchenHelperAgent a, MessageTemplate mt, @NotNull MainController mainController) {
        super(a, mt, mainController);
    }

    @Override
    protected double calculateCost(Description description) {
        if (!(description instanceof TaskDescription)) {
            return Double.POSITIVE_INFINITY;
        }
        return description.getCoordinate().euclideanDistance(this.getAgent().getCoordinate())
                + TaskEnum.WASH_DISH.getDuration();
    }

    @Override
    public boolean shouldAcceptProposal() {
        return true;
    }

    @Override
    public KitchenHelperAgent getAgent() {
        return (KitchenHelperAgent) this.myAgent;
    }

    @Override
    public void resumeWork(ACLMessage cfp) {
        ACLMessage reply = cfp.createReply();
        reply.setPerformative(ACLMessage.INFORM);
    }


    @Override
    protected void handleWork(ACLMessage accept, Description description) {
        this.getAgent().addBehaviour(new WakerBehaviour( this.getAgent(), TaskEnum.WARM_SOUP.getDuration()) {
            @Override
            protected void onWake() {
                resumeWork(accept);
            }
        });
    }

}



public class KitchenHelperAgent extends BaseAgent {

    private KitchenHelperSupply kitchenHelperSupply;

    public KitchenHelperAgent(@NotNull MainController mainController, @NotNull Coordinate coordinate) {
        super(mainController, Sets.newHashSet(TaskEnum.WASH_DISH),
                coordinate);
        this.addBehaviour(new KitchenHelperResponderBehaviour(this, getContractNetTemplate(ACLMessage.CFP)
        , this.getMainController()));

        this.kitchenHelperSupply = new KitchenHelperSupply();
    }

}
