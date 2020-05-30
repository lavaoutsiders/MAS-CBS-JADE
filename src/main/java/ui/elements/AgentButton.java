package ui.elements;

import ui.StatusEnum;

import javax.swing.*;

public abstract class AgentButton extends JButton implements IAgentButton {

    private StatusEnum currentStatus;

    public StatusEnum getCurrentStatus() {
        return currentStatus;
    }

    public void setStatus(StatusEnum status) {
        this.setText(status.getValue());
        this.setBackground(status.getColor());
        this.currentStatus = status;
    }
} 
