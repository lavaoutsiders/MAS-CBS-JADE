package main;

import jade.core.Agent;
import org.jetbrains.annotations.NotNull;
import ui.GUI;
import ui.StatusEnum;
import ui.elements.AgentButton;

public interface MainController {

    void registerAgent(String name, Agent agent, AgentButton button);

    void setUIComponentState(Agent agent, StatusEnum state);

    void printLogLine(String logLine);

    void registerGUI(@NotNull GUI gui);

}
