package agents.behaviours;

import agents.BaseAgent;
import agents.ChefAgent;
import com.sun.org.apache.regexp.internal.RE;
import exceptions.TaskNotDecomposableException;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import main.MainController;
import models.Description;
import models.TaskDescription;
import models.TaskEnum;
import org.jetbrains.annotations.NotNull;
import ui.StatusEnum;

import java.io.IOException;

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
        return ! ( this.getAgent()).getWorkingStatus();
    }

    public double calculateCost(Description taskDescription) {
        if (this.getAgent() == null || !(taskDescription instanceof TaskDescription)) {
            return Double.POSITIVE_INFINITY; // Impossible...
        }
        try {
            return (this.getAgent()).getCoordinate()
                    .euclideanDistance(taskDescription.getCoordinate())
                    + ( this.getAgent())
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
    public ChefAgent getAgent() {
        return (ChefAgent) this.myAgent;
    }

    @Override
    public void resumeWork(ACLMessage cfp) {
        this.getAgent().setWorkingStatus(false);
        this.getMainController().setUIComponentState(this.getAgent(), StatusEnum.IDLE);
        String output = "";
        try {
            if (cfp.getContentObject() != null && cfp.getContentObject() instanceof TaskDescription) {
                output += ((TaskDescription) cfp.getContentObject()).getTask().toString();
            }
            else {
                output += "task";
            }

        } catch (UnreadableException e) {
            e.printStackTrace();
        }

        this.getAgent().getMainController().printLogLine("PERFORMED - "+ this.getAgent().getName() + " performed " + output +
                " for " + cfp.getSender().getName());
        ACLMessage reply = cfp.createReply();
        reply.setPerformative(ACLMessage.INFORM);
        try {
            reply.setContentObject(cfp.getContentObject());
        } catch (IOException | UnreadableException e) {
            e.printStackTrace();
        }
        this.getAgent().send(reply);
    }

    @Override
    protected void handleWork(ACLMessage accept, Description description) {
        this.workForDuration(accept, ((TaskDescription) description).getTask().getDuration());
    }


}
