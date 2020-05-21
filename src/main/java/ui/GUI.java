package ui;

import agents.ChefAgent;
import agents.OrderAgent;
import org.jetbrains.annotations.NotNull;
import ui.elements.ChefAgentButton;
import ui.elements.OrderAgentButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

class OrderAgentActionListener implements ActionListener {
    public OrderAgentActionListener(OrderAgentButton orderAgentButton, OrderAgent orderAgent){
        this.orderAgent = orderAgent;
        this.orderAgentButton = orderAgentButton;
    }
    private OrderAgentButton orderAgentButton;
    private OrderAgent orderAgent;
    public void actionPerformed(ActionEvent e) {
        orderAgentButton.setStatus(StatusEnum.WORKING);
        orderAgent.sendNewOrder();
    }
};

public class GUI {

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame("Conveyor belt sushi multi-agent simulation");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainFrame.setSize(800, 600);

        // We are using absolute coordinates here
        mainFrame.setLayout(null);
        addOrderAgents(mainFrame, 100, 100);
        addChefAgents(mainFrame, 180, 180);
        mainFrame.setVisible(true);


    }

    private static void addChefAgents(@NotNull JFrame mainFrame, int startX, int startY) {
        int totalRows = 3;
        int totalColumns = 3;
        Map<ChefAgentButton, ChefAgent> result = new HashMap<ChefAgentButton, ChefAgent>();
        for (int i = 0; i < totalColumns; i++ ){
            for (int j = 0; j < totalRows; j++) {
                ChefAgent chefAgent = new ChefAgent();
                ChefAgentButton chefAgentButton = new ChefAgentButton(
                        startX + i * ChefAgentButton.getBoxWidth() + 50,
                        startY + j * ChefAgentButton.getBoxHeight() + 50
                );
                chefAgentButton.setStatus(StatusEnum.IDLE);
                mainFrame.add(chefAgentButton);
            }
        }
    }

    private static void addOrderAgents(@NotNull JFrame mainFrame, int startX, int startY){
        int totalRows = (mainFrame.getHeight() - 250) / (OrderAgentButton.getBoxLength() + 20 );
        int totalColumns = (mainFrame.getWidth() - 250) / (OrderAgentButton.getBoxLength() + 20);
        for (int i = 0; i <= totalColumns; i++) {
            for (int j = 0; j <= totalRows; j++) {
                if ( !(i == 0 || i == totalColumns) && !(j == 0 || j == totalRows)) {
                    continue;
                }
                OrderAgent orderAgent = new OrderAgent();
                OrderAgentButton orderAgentButton = new OrderAgentButton(
                        startX + i * OrderAgentButton.getBoxLength(),
                        startY + j * OrderAgentButton.getBoxLength());
                orderAgentButton.setStatus(StatusEnum.IDLE);
                orderAgentButton.addActionListener(new OrderAgentActionListener(orderAgentButton, orderAgent));
                mainFrame.add(orderAgentButton) ;
            }

        }
    }

}
