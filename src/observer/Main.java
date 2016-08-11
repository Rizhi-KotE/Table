package observer;

import controller.*;
import view.FileMenuBar;
import view.MainWindow;

public class Main {
    public static String mode;
    public static void main(String[] args) {
        //Observer
        Internationalization internationalization = new Internationalization();
        StudentTableWithPaging studentTableWithPaging = new StudentTableWithPaging(internationalization);
        FileHandler fileHandler = new FileHandler(internationalization);
        MainWindow mainWindow = new MainWindow(internationalization, studentTableWithPaging);
        mainWindow.setFileHandler(fileHandler);
        mainWindow.setFileMenu(new FileMenuBar(fileHandler, internationalization).getMenuBar());
        AddDialog addDialog = new AddDialog(studentTableWithPaging);
        internationalization.addObserver(mainWindow);
        internationalization.addObserver(studentTableWithPaging);
        internationalization.addObserver(addDialog);
        internationalization.addObserver(fileHandler);
        mainWindow.frame.setVisible(true);
        //internationalization.addObserver(searchDialog);
        System.out.println("Program was started");
    }
}
