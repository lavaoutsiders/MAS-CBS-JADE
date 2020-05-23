package ui;

import agents.OrderAgent;
import ui.elements.OrderAgentButton;
import ui.views.SelectItemView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrderAgentActionListener implements ActionListener {
    public OrderAgentActionListener(OrderAgentButton orderAgentButton, OrderAgent orderAgent){
        this.orderAgent = orderAgent;
        this.orderAgentButton = orderAgentButton;
    }
    private final OrderAgentButton orderAgentButton;
    private final OrderAgent orderAgent;
    public void actionPerformed(ActionEvent e) {
        SelectItemView selectItemView = new SelectItemView(this.orderAgent);
        selectItemView.setVisible();
        orderAgentButton.setStatus(StatusEnum.WORKING);
    }

}
