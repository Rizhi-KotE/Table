package observer;

import controller.AddDialog;
import controller.FileHandler;
import controller.Internationalization;
import controller.StudentTableWithPaging;
import view.FileMenuBar;
import view.MainWindow;

public class Main {
    public static String mode;
    private FileHandler fileHandler;

    public static void main(String[] args) {
        //Observer
        Internationalization internationalization = new Internationalization();
        createWindow(internationalization);
        internationalization.setLang("eng");
    }

    private static void createWindow(Internationalization internationalization) {
        StudentTableWithPaging studentTableWithPaging = new StudentTableWithPaging(internationalization);
        FileHandler fileHandler = new FileHandler(internationalization);
        MainWindow mainWindow = new MainWindow(internationalization, studentTableWithPaging);
        mainWindow.setFileHandler(fileHandler);
        FileMenuBar fileMenuBar = new FileMenuBar(fileHandler, internationalization);
        mainWindow.setFileMenu(fileMenuBar.getMenuBar());
        AddDialog addDialog = new AddDialog(studentTableWithPaging);
        internationalization.addObserver(mainWindow);
        internationalization.addObserver(studentTableWithPaging);
        internationalization.addObserver(addDialog);
        internationalization.addObserver(fileHandler);
        internationalization.addObserver(fileMenuBar);
        mainWindow.frame.setVisible(true);
        //internationalization.addObserver(searchDialog);
        System.out.println("Program was started");
    }


}
