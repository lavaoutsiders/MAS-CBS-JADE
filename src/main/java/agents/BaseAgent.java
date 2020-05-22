package agents;

import jade.core.Agent;
import jade.core.Service;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import models.TaskEnum;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class BaseAgent extends Agent {
    private Set<TaskEnum> supportedTasks;
    public BaseAgent(@NotNull Set<TaskEnum> supportedTasks) {
        this.supportedTasks = supportedTasks;
    }

    @Override
    protected void setup() {
        DFAgentDescription agentDescription = new DFAgentDescription();
        agentDescription.setName(getAID());
        this.supportedTasks.forEach((supportedTask)->{
            agentDescription.addServices(supportedTask.toServiceDescription());
        });

        try {
            DFService.register(this, agentDescription);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }


}
