package agents;

import com.google.common.collect.Sets;
import jade.core.Agent;
import models.TaskEnum;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class ChefAgent extends BaseAgent implements IChefAgent {

    public ChefAgent() {
        super(Sets.newHashSet(TaskEnum.KNEED_RICE, TaskEnum.CUT_FISH));
    }
}
