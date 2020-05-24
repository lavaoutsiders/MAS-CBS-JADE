package ui.views;

import agents.OrderAgent;
import models.ItemsEnum;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class SelectItemView {
    private final JFrame mainFrame;
    private final OrderAgent orderAgent;

    public SelectItemView(@NotNull OrderAgent orderAgent) {
        this.mainFrame = new JFrame();
        this.orderAgent = orderAgent;
        this.mainFrame.add(createButton(ItemsEnum.SASHIMI));
        this.mainFrame.add(createButton(ItemsEnum.SOUP));
        this.mainFrame.add(createButton(ItemsEnum.SUSHI_ROLL));
        this.mainFrame.setTitle("Select an item to order");
        this.mainFrame.setLayout(new GridLayout(1,4));
        this.mainFrame.setSize(600, 200);
    }
    public void setVisible() {
        this.mainFrame.setVisible(true);
    }
    private JButton createButton(ItemsEnum item) {
        JButton button = new JButton();
        button.setText(item.toString());
        button.setSize(80, 40);
        button.addActionListener(
                e -> {
                    SelectItemView.this.orderAgent.submitNewOrder(item);
                    SelectItemView.this.mainFrame.dispose();
                });
        return button;
    }
}
