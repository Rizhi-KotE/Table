package view;

import controller.AddComponent;
import controller.AddDialog;
import controller.FileHandler;
import controller.SearchDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

public class ToolBar implements Observer{
    private final FileHandler fileHandler;

    private final SearchDialog searchDialog;

    private final AddDialog addDialog;

    private final SearchDialog removeDialog;

    public ToolBar(FileHandler fileHandler, SearchDialog searchDialog, AddDialog addDialog, SearchDialog removeDialog) {
        this.fileHandler = fileHandler;
        this.searchDialog = searchDialog;
        this.addDialog = addDialog;
        this.removeDialog = removeDialog;
    }

    JToolBar createToolBar() {
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
                searchDialog.setVisible(true);
            }
        }));
        toolBar.add(AddComponent.makeButton(new JButton(), "ADD.png", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addDialog.setVisibleTrue();
            }
        }));
        toolBar.add(AddComponent.makeButton(new JButton(), "REMOVE.png", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeDialog.setVisible(true);
            }
        }));
        return toolBar;
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}