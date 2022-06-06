package org.example.mvc;

public interface View extends Observer {

    void showMessage(String message);

    void exportResultTable(String path);
}
