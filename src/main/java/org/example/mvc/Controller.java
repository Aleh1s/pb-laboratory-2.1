package org.example.mvc;

import java.io.File;

public class Controller {

    private Model model;

    public Controller(
            Model model
    ) {
        this.model = model;
    }

    public void exportResultTable(String path) {
        File dir = new File(path);
        boolean readingIsSuccess = model.readResults(dir);

        if (readingIsSuccess) {
            model.writeResults(dir);
        }
    }
}
