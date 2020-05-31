package agents;

import agents.behaviours.ChefAgentOrderResponderBehaviour;
import agents.behaviours.ChefAgentTaskResponderBehaviour;
import com.google.common.collect.Sets;
import exceptions.TaskNotDecomposableException;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import main.MainController;
import models.*;
import org.jetbrains.annotations.NotNull;
import ui.StatusEnum;

import java.util.HashSet;
import java.util.Set;

public class ChefAgent extends BaseAgent implements IChefAgent {
    private AgentSupply agentSupply;

    public ChefAgent(@NotNull MainController mainController, @NotNull Coordinate coordinate) {
        super(mainController,Sets.newHashSet(TaskEnum.START_ORDER, TaskEnum.KNEED_RICE, TaskEnum.CUT_FISH,
                TaskEnum.MAKE_SUSHI, TaskEnum.PLATING, TaskEnum.WARM_SOUP),
                coordinate);
        //new MessageContentMatcher(OrderDescription.class).getMessageTemplate()
        MessageTemplate orderAgentMessage = MessageTemplate.and(getContractNetTemplate(ACLMessage.CFP),
                new MessageTemplate((MessageTemplate.MatchExpression) aclMessage -> aclMessage.getSender().getName().contains("Order")));
        ChefAgentOrderResponderBehaviour chefAgentOrderResponderBehaviour = new ChefAgentOrderResponderBehaviour(this, orderAgentMessage, mainController);
        this.addBehaviour(chefAgentOrderResponderBehaviour);

        //new MessageContentMatcher(TaskDescription.class).getMessageTemplate()
        MessageTemplate taskMessageTemplate = MessageTemplate.and(getContractNetTemplate(ACLMessage.CFP),
                new MessageTemplate((MessageTemplate.MatchExpression) aclMessage -> aclMessage.getSender().getName().contains("Chef")));
        this.addBehaviour(new ChefAgentTaskResponderBehaviour(this, taskMessageTemplate, this.getMainController()));

        this.addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                MessageTemplate template = MessageTemplate.and(
                        getContractNetTemplate(ACLMessage.INFORM),
                        new MessageTemplate((MessageTemplate.MatchExpression) aclMessage -> {
                            try {
                                return aclMessage != null && (aclMessage.getContentObject() != null && (aclMessage.getContentObject() instanceof TaskDescription));
                            } catch (UnreadableException e) {
                                e.printStackTrace();
                                return false;
                            }
                        } )
                );

                ACLMessage msg = blockingReceive(template, 40);
                if (msg == null) return;
                TaskDescription finishedTask = null;
                try {
                    if (!(msg.getContentObject() instanceof TaskDescription)){
                        return;
                    }
                     finishedTask = (TaskDescription) msg.getContentObject();
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
                if (finishedTask == null){
                    return;
                }
                chefAgentOrderResponderBehaviour.finishTask(finishedTask.getTask());
                if (chefAgentOrderResponderBehaviour.onlyPlatingLeft() || chefAgentOrderResponderBehaviour.onlySushiAssemblyLeft()) {
                    chefAgentOrderResponderBehaviour.resumeWork(chefAgentOrderResponderBehaviour.getOriginCFP());
                }

            }
        });
        this.agentSupply = new AgentSupply();
    }

    public void finishPlating(OrderDescription orderDescription, AID orderAgent) {
        getMainController().printLogLine("FINALIZING ORDER - " + ChefAgent.this.getName()
                + " is plating ");
        this.getMainController().setUIComponentState(this, StatusEnum.WORKING);
        this.addBehaviour(new WakerBehaviour(this, TaskEnum.PLATING.getDuration()) {
            @Override
            protected void onWake() {
                getMainController().setUIComponentState(ChefAgent.this, StatusEnum.IDLE);


            }
        });
    }

    public AgentSupply getAgentSupply() {
        return agentSupply;
    }

    @Override
    public double calculateMainTaskCost(ItemsEnum item) {
        switch (item) {
            case SOUP:
                return this.agentSupply.hasSoup() ? TaskEnum.WARM_SOUP.getDuration() + TaskEnum.PLATING.getDuration()
                        : TaskEnum.PLATING.getDuration();
            case SASHIMI:
                return this.agentSupply.hasSlicedFish() ? TaskEnum.CUT_FISH.getDuration() + TaskEnum.PLATING.getDuration()
                        : TaskEnum.PLATING.getDuration();
            case SUSHI_ROLL:
                return calculateMainTaskCost(ItemsEnum.SASHIMI) + (this.agentSupply.hasRiceRoll() ?
                        TaskEnum.MAKE_SUSHI.getDuration(): TaskEnum.MAKE_SUSHI.getDuration());
            default:
                throw new UnsupportedOperationException();

        }
    }

    @Override
    public double calculateSubTaskCost(TaskEnum task) throws TaskNotDecomposableException {
        switch (task) {
            case CUT_FISH:
                return this.getAgentSupply().hasSlicedFish() ? 0 : task.getDuration() ;
            case KNEED_RICE:
                return this.getAgentSupply().hasRiceRoll() ? 0 : task.getDuration() ;
            case WARM_SOUP:
                return this.getAgentSupply().hasSoup() ? 0 : task.getDuration() ;
            default:
                throw new TaskNotDecomposableException();
        }
    }
}

