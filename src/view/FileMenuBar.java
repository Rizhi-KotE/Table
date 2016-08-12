package view;

import controller.FileHandler;
import controller.Internationalization;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

public class FileMenuBar implements Observer {

    private final Internationalization internationalization;
    private JMenuItem openFile;
    private JMenuItem saveFile;
    private JMenuItem exit;
    private JMenu fileMenu;
    private JMenuBar menuBar;

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    public FileMenuBar(FileHandler fileHandler, Internationalization internationalization) {
        this.internationalization = internationalization;
        createFileMenu(fileHandler, this.internationalization);
    }


    private void createFileMenu(FileHandler fileHandler, Internationalization internationalization) {
        ImageIcon iconOpen = new ImageIcon("img/OPEN.png");
        openFile = new JMenuItem(internationalization.lang.getString("open"), iconOpen);
        ImageIcon iconSave = new ImageIcon("img/SAVE.png");
        saveFile = new JMenuItem(internationalization.lang.getString("save"), iconSave);
        ImageIcon iconExit = new ImageIcon("img/EXIT.png");
        exit = new JMenuItem(internationalization.lang.getString("exit"), iconExit);
        fileMenu = new JMenu(internationalization.lang.getString("file"));
        openFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileHandler.openFile();
            }
        });
        fileMenu.add(openFile);
        saveFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileHandler.saveFile();
            }
        });
        fileMenu.add(saveFile);
        fileMenu.addSeparator();
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exit);
        menuBar = new JMenuBar();
        menuBar.add(fileMenu);
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("sss");
        fileMenu.setText(internationalization.lang.getString("file"));
        openFile.setText(internationalization.lang.getString("open"));
        saveFile.setText(internationalization.lang.getString("save"));
        exit.setText(internationalization.lang.getString("exit"));
    }


}