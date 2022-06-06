package org.example;

import org.example.mvc.ConsoleView;
import org.example.mvc.Controller;
import org.example.mvc.FileSystemModel;
import org.example.mvc.View;

import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        FileSystemModel model = new FileSystemModel();
        View view = new ConsoleView(
                new Controller(
                        model
                ),
                model
        );
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Write directory path or '-1' to exit: ");
            String line = scanner.nextLine();
            if (line.equals("-1")) break;
            boolean exit = false;
            while (!exit) {
                System.out.println("If result.csv exists it will be removed. Do you want to continue? [Y/N]");
                String chosen = scanner.nextLine();
                switch (chosen.toLowerCase(Locale.ROOT)) {
                    case "y" -> {
                        view.exportResultTable(line);
                        exit = true;
                    }
                    case "n" -> exit = true;
                    default -> System.out.println("Invalid chosen try again");
                }
            }
        }
    }
}