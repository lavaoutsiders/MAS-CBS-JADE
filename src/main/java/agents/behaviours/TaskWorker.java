package agents.behaviours;

import agents.ChefAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
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
    private ContractNetResponderBehaviour chefAgentResponderBehaviour;
    private ChefAgent chefAgent;
    private long waitTime;
    private ACLMessage originCFP;

    public TaskWorker(@NotNull OrderDescription orderDescription, @NotNull ContractNetResponderBehaviour chefAgentResponderBehaviour,
            @NotNull ACLMessage cfp ){
        this.orderDescription = orderDescription;
        this.chefAgentResponderBehaviour = chefAgentResponderBehaviour;
        this.chefAgent = (ChefAgent) chefAgentResponderBehaviour.getAgent();
        this.originCFP = cfp;
    }

    public ACLMessage getOriginCFP() {
        return originCFP;
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
            protected void onWake(){
                chefAgentResponderBehaviour.resumeWork(getOriginCFP());
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
                .stream()
                .filter(aid -> ! aid.equals(this.chefAgent.getAID()))
                .forEach(message::addReceiver);

        return message;
    }
}
