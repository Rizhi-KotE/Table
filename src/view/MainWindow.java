package view;

import controller.*;
import observer.Main;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class MainWindow extends JComponent implements Observer {
    private ToolBar toolBar;
    private FileHandler fileHandler;
    private AddDialog addDialog;

    public void setFileHandler(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    private StudentTableWithPaging studentTableWithPaging;

    public void setStudentTableWithPaging(StudentTableWithPaging studentTableWithPaging) {
        this.studentTableWithPaging = studentTableWithPaging;
        frame.add(studentTableWithPaging);
    }

    Internationalization internationalization;

    public static JFrame frame;

    private Toolkit kit = Toolkit.getDefaultToolkit();

    private Dimension screenSize = kit.getScreenSize();

    public int i = 1;
    public JMenu fileMenu;

    public MainWindow(Internationalization internationalization, StudentTableWithPaging studentTableWithPaging) {
        this.internationalization = internationalization;
        frame = new JFrame(internationalization.getLang().getString("st_table"));
        frame.setSize(640, 360);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setStudentTableWithPaging(studentTableWithPaging);
        frame.setVisible(true);
    }

    public void setFileMenu(JMenuBar menuBar) {
        frame.setJMenuBar(menuBar);
        frame.repaint();
    }

    public void setToolBar(ToolBar toolBar) {
        this.toolBar = toolBar;
        frame.add(toolBar.createToolBar(), BorderLayout.PAGE_START);
    }

    public StudentTableWithPaging getStudentTableWithPaging() {
        return studentTableWithPaging;
    }

    public void updateComponent() {
        frame.setTitle(internationalization.getLang().getString("st_table"));

    }
    public static void main(String[] args) {
        Main main = new Main();
        main.main(args);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
