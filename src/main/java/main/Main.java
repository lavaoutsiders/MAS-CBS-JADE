package main;

import ui.GUI;

public class Main {
    public static void main(String[] args) {
        MainControllerImpl mainController = new MainControllerImpl();
        GUI gui = new GUI(mainController);
    }
}
