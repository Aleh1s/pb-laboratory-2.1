package org.example.mvc;

public interface Observable {
    void attach(Observer o);
    void detach(Observer o);
    void notifyObservers(String message);
    
}
