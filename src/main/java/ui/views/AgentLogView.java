package ui.views;

import agents.OrderAgent;
import models.ItemsEnum;
import org.jetbrains.annotations.NotNull;
import sun.management.Agent;

import javax.swing.*;
import java.awt.*;

public class AgentLogView {

    private final JFrame mainFrame;
    private final Agent agent;

    public AgentLogView(@NotNull Agent agent) {
        this.mainFrame = new JFrame();
        this.agent = agent;

        this.mainFrame.setTitle("See the log for agent ");
        this.mainFrame.setLayout(new GridLayout(1,4));
        this.mainFrame.setSize(600, 200);
    }
    public void setVisible() {
        this.mainFrame.setVisible(true);
    }

}
