package ui.elements;


import javax.swing.*;

public class OrderAgentButton extends AgentButton {
    public OrderAgentButton(int x, int y, String title) {
        this.setBorder(BorderFactory.createTitledBorder(title));
        this.setLocation(x,y);
        this.setSize(getBoxLength(), getBoxLength());
    }


    public static int getBoxLength(){
        return 60;
    }




}
