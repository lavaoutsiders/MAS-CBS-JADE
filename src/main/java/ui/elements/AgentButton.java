package ui.elements;

import models.TaskEnum;
import ui.StatusEnum;

import javax.swing.*;

public abstract class AgentButton extends JButton implements IAgentButton {

    private TaskEnum currentStatus;

    public TaskEnum getCurrentStatus() {
        return currentStatus;
    }

    public void setStatus(StatusEnum status) {
        this.setText(status.getValue());
        this.setBackground(status.getColor());
    }
} 
