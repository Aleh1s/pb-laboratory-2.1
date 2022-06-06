package org.example.mvc;

public class ConsoleView implements View {

    private final Controller controller;

    public ConsoleView(
            Controller controller,
            Model model
    ) {
        this.controller = controller;
        model.attach(this);
    }

    @Override
    public void update(String message) {
        showMessage(message);
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void exportResultTable(String path) {
        controller.exportResultTable(path);
    }
}
