package agents.behaviours;

import agents.BaseAgent;
import agents.ChefAgent;
import com.sun.tools.javac.jvm.Items;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import main.MainController;
import models.Description;
import models.ItemsEnum;
import models.OrderDescription;
import models.TaskEnum;
import org.jetbrains.annotations.NotNull;
import ui.StatusEnum;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ChefAgentOrderResponderBehaviour extends ContractNetResponderBehaviour {

    private final Set<TaskEnum> unfinishedTasks;

    public ChefAgentOrderResponderBehaviour(BaseAgent a, MessageTemplate mt, @NotNull MainController mainController) {
        super(a, mt, mainController);
        this.unfinishedTasks = new HashSet<>();
    }


    public void finishTask(@NotNull TaskEnum task) {
        this.unfinishedTasks.remove(task);
    }

    public boolean isAllSubtasksDone(){
        return this.unfinishedTasks.size() == 0;
    }

    public boolean onlyPlatingLeft() {
        return this.unfinishedTasks.size() == 1 && this.unfinishedTasks.contains(TaskEnum.PLATING);
    }

    @Override
    public ChefAgent getAgent() {
        return (ChefAgent) super.getAgent();
    }

    @Override
    public boolean shouldAcceptProposal() {
        return ! this.getAgent().getWorkingStatus();
    }

    @Override
    public void resumeWork(ACLMessage cfp) {
        this.getMainController().setUIComponentState(this.getAgent(), StatusEnum.WORKING);
        this.getAgent().addBehaviour(new WakerBehaviour(this.getAgent(), TaskEnum.PLATING.getDuration()) {
            @Override
            protected void onWake() {
                ChefAgentOrderResponderBehaviour.this.getAgent().setWorkingStatus(false);
                ChefAgentOrderResponderBehaviour.this.getMainController().setUIComponentState(this.getAgent(), StatusEnum.IDLE);
                ACLMessage reply = cfp.createReply();
                reply.setPerformative(ACLMessage.INFORM);
                try {
                    reply.setContentObject(cfp.getContentObject());
                } catch (IOException | UnreadableException e) {
                    e.printStackTrace();
                }
                this.getAgent().send(reply);
            }
        });

    }


    @Override
    protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {

        ACLMessage reply = super.handleAcceptProposal(cfp, propose, accept);
        reply.setContent("Dispatched");

        return reply;

    }

    @Override
    protected void handleWork(ACLMessage cfp, Description description) {
        this.getMainController().setUIComponentState(this.getAgent(), StatusEnum.WORKING);
        if (!(description instanceof OrderDescription)) return;
        long waitDuration;
        OrderDescription orderDescription = (OrderDescription) description;
        this.unfinishedTasks.addAll(orderDescription.getItem().getRequiredTasks());
        this.unfinishedTasks.add(TaskEnum.WASH_DISH);
        TaskWorker taskWorker = new TaskWorker(new OrderDescription(orderDescription.getItem(),
                (this.getAgent()).getCoordinate()),
                this, cfp);
        taskWorker.executeTask();

        this.getMainController().setUIComponentState(this.getAgent(), StatusEnum.IDLE);

    }

    @Override
    public double calculateCost(@NotNull Description orderDescription) {
        if (this.getAgent() == null || !(orderDescription instanceof OrderDescription)) {
            return Double.POSITIVE_INFINITY; // Impossible...
        }
        return this.getAgent().getCoordinate()
                .euclideanDistance(orderDescription.getCoordinate())
                + this.getAgent().calculateMainTaskCost(((OrderDescription) orderDescription).getItem()); // TODO
    }

}
