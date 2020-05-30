package agents;

import agents.behaviours.ContractNetResponderBehaviour;
import com.google.common.collect.Sets;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import main.MainController;
import models.*;
import org.jetbrains.annotations.NotNull;
import ui.StatusEnum;

import java.io.IOException;

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
    public KitchenHelperAgent getAgent() {
        return (KitchenHelperAgent) this.myAgent;
    }

    @Override
    public void resumeWork(ACLMessage cfp) {
        this.getAgent().setWorkingStatus(false);
        ACLMessage reply = cfp.createReply();

        this.getMainController().printLogLine( "PERFORMED - " +  this.getAgent().getName() + " washed an dish for " + cfp.getSender().getName());
        reply.setPerformative(ACLMessage.INFORM);

        this.getMainController().setUIComponentState(this.getAgent(), StatusEnum.IDLE);
        try {
            reply.setContentObject(cfp.getContentObject());
        } catch (IOException | UnreadableException e) {
            e.printStackTrace();
        }
        this.getAgent().send(reply);
    }


    @Override
    protected void handleWork(ACLMessage accept, Description description) {
        this.getAgent().setWorkingStatus(true);
        this.getMainController().setUIComponentState(this.getAgent(), StatusEnum.WORKING);
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
