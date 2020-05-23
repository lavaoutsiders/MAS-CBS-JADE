package main;

import ui.GUI;

public class Main {
    public static void main(String[] args) {
        MainController mainController = new MainController();
        GUI gui = new GUI(mainController);
    }
}
