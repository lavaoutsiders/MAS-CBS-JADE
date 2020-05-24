package agents.behaviours;

import agents.ChefAgent;
import agents.OrderAgent;
import jade.core.behaviours.*;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import models.ItemsEnum;
import models.OrderDescription;
import models.TaskDescription;
import models.TaskEnum;
import org.jetbrains.annotations.NotNull;
import utils.DFUtils;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

public class TaskWorker {

    private OrderDescription orderDescription;
    private ChefAgentResponderBehaviour chefAgentResponderBehaviour;
    private ChefAgent chefAgent;
    private long waitTime;

    public TaskWorker(@NotNull OrderDescription orderDescription, @NotNull ChefAgentResponderBehaviour chefAgentResponderBehaviour){
        this.orderDescription = orderDescription;
        this.chefAgentResponderBehaviour = chefAgentResponderBehaviour;
        this.chefAgent = (ChefAgent) chefAgentResponderBehaviour.getAgent();
    }

    private Behaviour sliceFish() {
        if (! chefAgent.getAgentSupply().hasSlicedFish()) {
            return new ChefAgentInitiatorBehaviour(this.chefAgent, getTaskMessage(TaskEnum.CUT_FISH) );
        }
        return null;
    }

    private Behaviour rollRice() {
        if (! chefAgent.getAgentSupply().hasRiceRoll()) {
            return new ChefAgentInitiatorBehaviour(this.chefAgent, getTaskMessage(TaskEnum.KNEED_RICE));
        }
        return null;
    }


    private Behaviour warmSoup() {
        if (! chefAgent.getAgentSupply().hasSoup()) {
            return new ChefAgentInitiatorBehaviour(this.chefAgent, getTaskMessage(TaskEnum.WARM_SOUP));
        }
        return null;
    }

    private Behaviour getPlate() {
        if (! chefAgent.getAgentSupply().hasCleanDish()) {
           return new ChefAgentInitiatorBehaviour(this.chefAgent, getTaskMessage(TaskEnum.WASH_DISH));
        }
        return null;
    }

    private void addBehaviourIfNotNull(@NotNull ParallelBehaviour mainBehaviour, Behaviour behaviour){
        if (behaviour != null){
            mainBehaviour.addSubBehaviour(behaviour);
        }
    }

    public void executeTask(){
        SequentialBehaviour sequentialBehaviour = new SequentialBehaviour(this.chefAgent);

        ParallelBehaviour mainBehaviour = new ParallelBehaviour(this.chefAgent, ParallelBehaviour.WHEN_ALL);
        sequentialBehaviour.addSubBehaviour(mainBehaviour);

        long timeout = 0;
        switch (this.orderDescription.getItem()) {
            case SASHIMI:
                addBehaviourIfNotNull(mainBehaviour, sliceFish());
                addBehaviourIfNotNull(mainBehaviour, getPlate());
                break;
            case SOUP:
                addBehaviourIfNotNull(mainBehaviour, warmSoup());
                addBehaviourIfNotNull(mainBehaviour, getPlate());
                break;
            case SUSHI_ROLL:
                addBehaviourIfNotNull(mainBehaviour, sliceFish());
                addBehaviourIfNotNull(mainBehaviour, rollRice());
                addBehaviourIfNotNull(mainBehaviour, getPlate());
                timeout = TaskEnum.MAKE_SUSHI.getDuration();
                break;
        }
        timeout += TaskEnum.PLATING.getDuration();
        // This step simulates the final preparation step
        sequentialBehaviour.addSubBehaviour(new WakerBehaviour(this.chefAgent, timeout ) {
            @Override
            protected void onWake() {
                System.out.println("Finished task"); // TODO
            }
        });

        this.chefAgent.addBehaviour(sequentialBehaviour);

    }

    public long getWaitTime() {
        return waitTime;
    }


    public Date getReplyDeadline() {
        return new Date(System.currentTimeMillis() + 5 * 1000);
    }

    public ACLMessage getTaskMessage(TaskEnum task) {
        ACLMessage message = new ACLMessage(ACLMessage.CFP);
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        message.setReplyByDate(getReplyDeadline());
        try {
            message.setContentObject(new TaskDescription(task, this.orderDescription.getCoordinate()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Objects.requireNonNull(
                DFUtils.searchDF(task.toString(), this.chefAgent))
                .forEach(message::addReceiver);

        return message;
    }
}
