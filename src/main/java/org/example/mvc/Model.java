package org.example.mvc;

import java.io.File;

public interface Model extends Observable {

    boolean readResults(File dir);
    void writeResults(File dir);

}
