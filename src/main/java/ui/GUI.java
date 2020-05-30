package ui;

import agents.ChefAgent;
import agents.KitchenHelperAgent;
import agents.OrderAgent;
import jade.core.Agent;
import main.MainControllerImpl;
import models.Coordinate;
import org.jetbrains.annotations.NotNull;
import ui.elements.KitchenAgentButton;
import ui.elements.OrderAgentButton;

import javax.swing.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

;

public class GUI {

    private JFrame mainFrame;
    private MainControllerImpl mainController;
    private JTextArea logArea;

    public GUI(@NotNull MainControllerImpl mainController) {
        this.mainController = mainController;
        mainController.registerGUI(this);
        JFrame mainFrame = new JFrame("Conveyor belt sushi multi-agent simulation");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainFrame.setSize(1000, 830);

        // We are using absolute coordinates here
        mainFrame.setLayout(null);
        addOrderAgents(mainFrame, 100, 100);
        Map<KitchenAgentButton, Agent> agentButtonChefAgentMap = addKitchenAgents(mainFrame, 180, 180);
        mainFrame.setVisible(true);
        this.logArea = new JTextArea();
               this.logArea.setEditable(false); // set textArea non-editable
        JScrollPane scrollPane = new JScrollPane(this.logArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setLocation(0, 550);
        scrollPane.setSize(1000, 230);

        mainFrame.add(scrollPane);

        this.mainFrame = mainFrame;
    }

    public void addLogAreaLine(String line) {
        this.logArea.append(line);
    }

    private Map<KitchenAgentButton, Agent> addKitchenAgents(@NotNull JFrame mainFrame, int startX, int startY) {
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
                Coordinate coordinate = new Coordinate(startX +  (int)(i * KitchenAgentButton.getBoxWidth() * 1.7) ,
                        startY + (int)(j * KitchenAgentButton.getBoxHeight() * 1.6));
                if (helperRobotColumn.contains(i)) {
                    agent = new KitchenHelperAgent(this.mainController, coordinate);
                    prefix = "Helper ";
                    count = kitchenHelperCount;
                    kitchenHelperCount++;
                }
                else {
                    agent = new ChefAgent(this.mainController, coordinate);
                    prefix = "Chef ";
                    count = chefCount;
                    chefCount++;
                }
                String agentName = prefix + count;
                KitchenAgentButton kitchenAgentButton = new KitchenAgentButton(
                        coordinate.getX(), coordinate.getY(), agentName);
                this.mainController.registerAgent(agentName, agent, kitchenAgentButton);

                kitchenAgentButton.setStatus(StatusEnum.IDLE);
                mainFrame.add(kitchenAgentButton);
                result.put(kitchenAgentButton, agent);
            }
        }
        return result;
    }

    private void addOrderAgents(@NotNull JFrame mainFrame, int startX, int startY){
        int totalRows = (mainFrame.getHeight() - 400) / (OrderAgentButton.getBoxLength() + 20 );
        int totalColumns = (mainFrame.getWidth() - 150) / (OrderAgentButton.getBoxLength() + 20);
        int count = 0;
        for (int i = 0; i <= totalColumns; i++) {
            for (int j = 0; j <= totalRows; j++) {
                if ( !(i == 0 || i == totalColumns) && !(j == 0 || j == totalRows)) {
                    continue;
                }
                String agentName = "OrderAgent " + count;
                Coordinate coordinate =    new Coordinate(startX + i * OrderAgentButton.getBoxLength(),
                        startY + j * OrderAgentButton.getBoxLength());
                OrderAgent orderAgent = new OrderAgent(this.mainController, coordinate);

                OrderAgentButton orderAgentButton = new OrderAgentButton(
                        coordinate.getX(), coordinate.getY(), "OA " + count);
                orderAgentButton.setStatus(StatusEnum.IDLE);
                orderAgentButton.addActionListener(new OrderAgentActionListener(orderAgentButton, orderAgent));
                mainFrame.add(orderAgentButton);
                this.mainController.registerAgent(agentName, orderAgent, orderAgentButton);
                count++;
            }

        }
    }


}
