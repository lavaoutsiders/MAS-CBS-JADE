package main;

import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import ui.GUI;

public class MainController {
    private final ContainerController containerController;
    public MainController() {
        // FROM https://stackoverflow.com/questions/22391640/how-to-create-containers-and-add-agents-into-it-in-jade
        //Get the JADE runtime interface (singleton)
        jade.core.Runtime runtime = jade.core.Runtime.instance();
        //Create a Profile, where the launch arguments are stored
        Profile profile = new ProfileImpl(true);
        profile.setParameter(Profile.CONTAINER_NAME, "SushiAgentContainer");
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        profile.setParameter(Profile.LOCAL_HOST, "127.0.0.1");
        //create a non-main agent container
        containerController = runtime.createMainContainer(profile);
    }

    public void registerAgent(String name, Agent agent) {
        try {
            this.containerController
                    .acceptNewAgent(name, agent)
                    .start();

        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
