package agents;

import jade.core.Agent;

public class OrderAgent extends Agent implements IOrderAgent {
    @Override
    protected void setup() {
        super.setup();
    }


    public void sendNewOrder() {
        System.out.println("Order agent submitted a new order");
    }
}
