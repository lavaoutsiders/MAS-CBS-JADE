package ui;

import agents.ChefAgent;
import agents.KitchenHelperAgent;
import agents.OrderAgent;
import jade.core.Agent;
import org.jetbrains.annotations.NotNull;
import ui.elements.KitchenAgentButton;
import ui.elements.OrderAgentButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

        mainFrame.setSize(1000, 700);

        // We are using absolute coordinates here
        mainFrame.setLayout(null);
        addOrderAgents(mainFrame, 100, 100);
        Map<KitchenAgentButton, Agent> agentButtonChefAgentMap = addKitchenAgents(mainFrame, 180, 180);
        mainFrame.setVisible(true);


    }

    private static Map<KitchenAgentButton, Agent> addKitchenAgents(@NotNull JFrame mainFrame, int startX, int startY) {
        int totalRows = 3;
        int totalColumns = 4;
        Set<Integer> helperRobotColumn = new HashSet<Integer>();
        helperRobotColumn.add(3);
        Map<KitchenAgentButton, Agent> result = new HashMap<KitchenAgentButton, Agent>();
        int chefCount = 0;
        int kitchenHelperCount = 0;
        for (int i = 0; i < totalColumns; i++ ){
            for (int j = 0; j < totalRows; j++) {
                Agent agent;
                String prefix;
                int count;
                if (helperRobotColumn.contains(i)) {
                    agent = new KitchenHelperAgent();
                    prefix = "Helper ";
                    count = kitchenHelperCount;
                    kitchenHelperCount++;
                }
                else {
                    agent = new ChefAgent();
                    prefix = "Chef ";
                    count = chefCount;
                    chefCount++;
                }
                KitchenAgentButton kitchenAgentButton = new KitchenAgentButton(
                        startX + i * KitchenAgentButton.getBoxWidth() * 2 ,
                        startY + j * KitchenAgentButton.getBoxHeight() * 2, prefix + count
                );
                kitchenAgentButton.setStatus(StatusEnum.IDLE);
                mainFrame.add(kitchenAgentButton);
                result.put(kitchenAgentButton, agent);
            }
        }
        return result;
    }

    private static void addOrderAgents(@NotNull JFrame mainFrame, int startX, int startY){
        int totalRows = (mainFrame.getHeight() - 150) / (OrderAgentButton.getBoxLength() + 20 );
        int totalColumns = (mainFrame.getWidth() - 150) / (OrderAgentButton.getBoxLength() + 20);
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
