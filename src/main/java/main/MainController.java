package main;

import jade.core.Agent;
import models.TaskEnum;
import ui.StatusEnum;
import ui.elements.AgentButton;

public interface MainController {

    void registerAgent(String name, Agent agent, AgentButton button);

    void setUIComponentState(Agent agent, StatusEnum state);



}
