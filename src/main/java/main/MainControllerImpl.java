package main;

import exceptions.NoAssociatedUIElementException;
import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import javafx.util.BuilderFactory;
import models.TaskEnum;
import ui.GUI;
import ui.StatusEnum;
import ui.elements.AgentButton;

import java.util.HashMap;
import java.util.Map;

public class MainControllerImpl implements MainController {
    private final ContainerController containerController;
    public MainControllerImpl() {
        jade.core.Runtime runtime = jade.core.Runtime.instance();

        Profile profile = new ProfileImpl(true);
        profile.setParameter(Profile.CONTAINER_NAME, "SushiAgentContainer");
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        profile.setParameter(Profile.LOCAL_HOST, "127.0.0.1");

        containerController = runtime.createMainContainer(profile);

        this.agentButtonMap = new HashMap<>();
    }

    private Map<Agent, AgentButton> agentButtonMap;

    @Override
    public void registerAgent(String name, Agent agent, AgentButton agentButton) {
        try {
            this.containerController
                    .acceptNewAgent(name, agent)
                    .start();
            this.agentButtonMap.put(agent, agentButton);
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUIComponentState(Agent agent, StatusEnum state) {
        AgentButton button = this.agentButtonMap.get(agent);
        if (button == null) {
//            throw new NoAssociatedUIElementException();
            System.out.println("No associated UI Element found");
            return;
        }
        button.setStatus(state);
    }
}
