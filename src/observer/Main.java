package observer;

import controller.*;
import view.FileMenuBar;
import view.MainWindow;
import view.ToolBar;

public class Main {
    public static String mode;
    private FileHandler fileHandler;

    public static void main(String[] args) {
        //Observer
        Internationalization internationalization = new Internationalization();
        createWindow(internationalization);
    }

    private static void createWindow(Internationalization internationalization) {
        StudentTableWithPaging studentTableWithPaging = new StudentTableWithPaging(internationalization);
        FileHandler fileHandler = new FileHandler(internationalization);
        MainWindow mainWindow = new MainWindow(internationalization, studentTableWithPaging);
        mainWindow.setFileHandler(fileHandler);
        FileMenuBar fileMenuBar = new FileMenuBar(fileHandler, internationalization);
        mainWindow.setFileMenu(fileMenuBar.getMenuBar());

        AddDialog addDialog = new AddDialog(studentTableWithPaging, internationalization);
        SearchDialog searchDialog = new SearchDialog(mainWindow, "SEARCJ_MODE", studentTableWithPaging, internationalization);
        SearchDialog removeDialog = new SearchDialog(mainWindow, "REMOVE_MODE", studentTableWithPaging, internationalization);
        ToolBar toolBar = new ToolBar(fileHandler, searchDialog, addDialog, removeDialog);

        mainWindow.setToolBar(toolBar);
        internationalization.addObserver(mainWindow);
        internationalization.addObserver(studentTableWithPaging);
        internationalization.addObserver(addDialog);
        internationalization.addObserver(fileHandler);
        internationalization.addObserver(fileMenuBar);
        internationalization.addObserver(toolBar);
        internationalization.addObserver(searchDialog);
        internationalization.addObserver(removeDialog);

        mainWindow.frame.setVisible(true);
        //internationalization.addObserver(searchDialog);
        System.out.println("Program was started");
    }


}
