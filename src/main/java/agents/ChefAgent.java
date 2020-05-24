package agents;

import agents.behaviours.ChefAgentOrderResponderBehaviour;
import agents.behaviours.ChefAgentTaskResponderBehaviour;
import com.google.common.collect.Sets;
import exceptions.TaskNotDecomposableException;
import jade.lang.acl.MessageTemplate;
import main.MainController;
import models.*;
import org.jetbrains.annotations.NotNull;

public class ChefAgent extends BaseAgent implements IChefAgent {
    private AgentSupply agentSupply;

    public ChefAgent(@NotNull MainController mainController, @NotNull Coordinate coordinate) {
        super(mainController,Sets.newHashSet(TaskEnum.START_ORDER, TaskEnum.KNEED_RICE, TaskEnum.CUT_FISH,
                TaskEnum.MAKE_SUSHI, TaskEnum.PLATING, TaskEnum.WARM_SOUP),
                coordinate);

        MessageTemplate orderAgentMessage = MessageTemplate.and(getContractNetTemplate(),
                new MessageContentMatcher(OrderDescription.class).getMessageTemplate());

        this.addBehaviour(new ChefAgentOrderResponderBehaviour(this, orderAgentMessage, mainController));

        MessageTemplate taskMessageTemplate = MessageTemplate.and(getContractNetTemplate(),
                new MessageContentMatcher(TaskDescription.class).getMessageTemplate());
        this.addBehaviour(new ChefAgentTaskResponderBehaviour(this, taskMessageTemplate, this.getMainController()));
        this.agentSupply = new AgentSupply();
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
                return this.getAgentSupply().hasSlicedFish() ? task.getDuration() : 0 ;
            case KNEED_RICE:
                return this.getAgentSupply().hasRiceRoll() ? task.getDuration() : 0;
            case WARM_SOUP:
                return this.getAgentSupply().hasSoup() ? task.getDuration() : 0;
            default:
                throw new TaskNotDecomposableException();
        }
    }
}

