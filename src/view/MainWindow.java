package view;

import controller.*;
import observer.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

public class MainWindow extends JComponent implements Observer {
    private FileHandler fileHandler;

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
        frame = new JFrame(internationalization.lang.getString("st_table"));
        frame.setSize(screenSize.width, screenSize.height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(createToolBar(), BorderLayout.PAGE_START);
        frame.setMinimumSize(new Dimension(screenSize.width, screenSize.height));
        setStudentTableWithPaging(studentTableWithPaging);
        frame.setVisible(true);
    }

    public void setFileMenu(JMenuBar menuBar) {
        frame.setJMenuBar(menuBar);
        frame.repaint();
    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.add(AddComponent.makeButton(new JButton(), "SAVE.png", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileHandler.saveFile();
            }
        }));
        toolBar.add(AddComponent.makeButton(new JButton(), "OPEN.png", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileHandler.openFile();
            }
        }));
        toolBar.addSeparator();
        toolBar.add(AddComponent.makeButton(new JButton(), "SEARCH.png", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SearchDialog(MainWindow.this, "SEARCH_MODE", studentTableWithPaging , internationalization);
            }
        }));
        toolBar.add(AddComponent.makeButton(new JButton(), "ADD.png", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new AddDialog(studentTableWithPaging);
            }
        }));
        toolBar.add(AddComponent.makeButton(new JButton(), "REMOVE.png", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SearchDialog(MainWindow.this, "REMOVE_MODE", studentTableWithPaging , internationalization);
            }
        }));
        return toolBar;
    }

    public StudentTableWithPaging getStudentTableWithPaging() {
        return studentTableWithPaging;
    }

    public void updateComponent() {
        frame.setTitle(internationalization.lang.getString("st_table"));

    }
    public static void main(String[] args) {
        Main main = new Main();
        main.main(args);
    }

    @Override
    public void update(Observable o, Object arg) {
        updateComponent();
    }
}
