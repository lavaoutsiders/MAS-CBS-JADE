package agents;

import jade.core.Agent;
import jade.core.Service;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import models.TaskEnum;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class BaseAgent extends Agent {
    private final Set<TaskEnum> supportedTasks;
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
            System.out.println("REGISTER - " + this.getName() + " registered.");
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
            System.out.println("DEREGISTER - " + this.getName() + " deregistered.");
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    protected MessageTemplate getContractNetTemplate(int performative) {
        return MessageTemplate.and(
                getContractNetTemplate(),
                MessageTemplate.MatchPerformative(performative) );
    }

    protected MessageTemplate getContractNetTemplate() {
        return MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);

    }

}
