package agents.behaviours;

import agents.BaseAgent;
import agents.ChefAgent;
import exceptions.TaskNotDecomposableException;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import javafx.concurrent.Task;
import main.MainController;
import models.Description;
import models.OrderDescription;
import models.TaskDescription;
import org.jetbrains.annotations.NotNull;

public class ChefAgentTaskResponderBehaviour extends ChefAgentResponderBehaviour {

    public ChefAgentTaskResponderBehaviour(BaseAgent a, MessageTemplate mt, @NotNull MainController mainController) {
        super(a, mt, mainController);
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
