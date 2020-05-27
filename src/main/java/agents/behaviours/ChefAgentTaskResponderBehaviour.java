package agents.behaviours;

import agents.BaseAgent;
import agents.ChefAgent;
import exceptions.TaskNotDecomposableException;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import main.MainController;
import models.Description;
import models.TaskDescription;
import models.TaskEnum;
import org.jetbrains.annotations.NotNull;

public class ChefAgentTaskResponderBehaviour extends ContractNetResponderBehaviour {

    public ChefAgentTaskResponderBehaviour(BaseAgent a, MessageTemplate mt, @NotNull MainController mainController) {
        super(a, mt, mainController);
    }

    public void startWork(TaskEnum task, ACLMessage cfp){
        this.getAgent().addBehaviour(new WakerBehaviour(this.getAgent(), task.getDuration()) {
            @Override
            protected void onWake() {
                if (this.getAgent() instanceof BaseAgent) {
                    ChefAgentTaskResponderBehaviour.this.resumeWork(cfp);
                }
            }
        });
    }

    @Override
    public boolean shouldAcceptProposal() {
        return true; // TODO
    }

    public double calculateCost(Description taskDescription) {
        if (!(this.getAgent() instanceof ChefAgent) || !(taskDescription instanceof TaskDescription)) {
            return Double.POSITIVE_INFINITY; // Impossible...
        }
        try {
            return ((ChefAgent) this.getAgent()).getCoordinate()
                    .euclideanDistance(taskDescription.getCoordinate())
                    + ((ChefAgent) this.getAgent())
                    .calculateSubTaskCost(((TaskDescription) taskDescription).getTask()); // TODO
        } catch (TaskNotDecomposableException e) {
            e.printStackTrace();
        }
        return Double.POSITIVE_INFINITY;
    }


    @Override
    protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
        super.handleRejectProposal(cfp, propose, reject);
    }

    @Override
    public void resumeWork(ACLMessage cfp) {

    }

    @Override
    protected void handleWork(ACLMessage accept, Description description) {

    }


}
