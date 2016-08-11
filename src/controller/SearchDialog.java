package controller;

import model.Examination;
import model.Student;
import model.TableModel;
import view.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Pattern;

public class SearchDialog extends JComponent implements Observer{

    private StudentTableWithPaging studentTableWithPaging;
    private TableModel tableModel;
    private String mode;
    private JTextField lastName;
    private JTextField group;
    private JComboBox minMiddleMark;
    private JComboBox maxMiddleMark;
    private JComboBox minMark;
    private JComboBox maxMark;
    private JFrame frame;
    private StudentTableWithPaging searchPanel;
    private Internationalization internationalization;
   JLabel labelText;
    private final String search;
    private final String remove;

    public SearchDialog(MainWindow mainWindow, String mode, StudentTableWithPaging studentTableWithPaging, Internationalization internationalization) {
        this.internationalization = internationalization;
        this.studentTableWithPaging = studentTableWithPaging;
        this.mode = mode;
        tableModel = studentTableWithPaging.getTableModel();
        frame = createFrame();
        frame.pack();
        frame.setLocationRelativeTo(studentTableWithPaging);
        frame.setResizable(false);
        frame.setVisible(true);
        search = internationalization.getLang().getString("search_st");
        remove = internationalization.getLang().getString("remove_st");
    }

    private JFrame createFrame(){
        JFrame frame;
        if (mode.equals("SEARCH_MODE")) {
            frame = new JFrame(search);
            labelText = new JLabel(search);
        }
        else {
            frame = new JFrame(remove);
            labelText = new JLabel(remove);
        }
        JPanel jPanelID = new JPanel();
        jPanelID.setLayout(new GridBagLayout());
        labelText.setHorizontalAlignment(JLabel.CENTER);
        AddComponent.add(jPanelID,labelText, 0, 0, 3, 1);
        String[] labelString = {internationalization.getLang().getString("last_name") + ":", internationalization.getLang().getString("group") + ":"};
        labelText = new JLabel(labelString[0]);
        AddComponent.add(jPanelID,labelText, 0, 1, 1, 1);
        lastName = new JTextField(30);
        AddComponent.add(jPanelID, lastName, 1, 1, 3, 1);
        labelText = new JLabel(labelString[1]);
        AddComponent.add(jPanelID, labelText, 0, 2, 1, 1);
        group = new JTextField(30);
        AddComponent.add(jPanelID, group, 1, 2, 3, 1);
        String[] markString = {"-", "4", "5", "6", "7", "8", "9", "10"};
        labelText = new JLabel(internationalization.getLang().getString("middle_mark") + "(" + internationalization.getLang().getString("less") + "/" + internationalization.getLang().getString("great") + ")");
        labelText.setHorizontalAlignment(JLabel.CENTER);
        AddComponent.add(jPanelID,labelText, 0, 3, 1, 1);
        minMiddleMark = new JComboBox(markString);
        AddComponent.add(jPanelID, minMiddleMark, 1, 3, 1, 1);
        maxMiddleMark = new JComboBox(markString);
        AddComponent.add(jPanelID, maxMiddleMark, 2, 3, 1, 1);
        labelText = new JLabel(internationalization.getLang().getString("mark") + "(" + internationalization.getLang().getString("less") + "/" + internationalization.getLang().getString("great") + ")");
        labelText.setHorizontalAlignment(JLabel.CENTER);
        AddComponent.add(jPanelID,labelText, 0, 4, 1, 1);
        minMark = new JComboBox(markString);
        AddComponent.add(jPanelID, minMark, 1, 4, 1, 1);
        maxMark = new JComboBox(markString);
        AddComponent.add(jPanelID, maxMark, 2, 4, 1, 1);
        frame.add(jPanelID, BorderLayout.NORTH);
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clickButton();
            }
        });
        frame.add(okButton, BorderLayout.SOUTH);
        return frame;
    }

    private void clickButton() {
        if (mode.equals("SEARCH_MODE")) searchStudent();
        else removeStudent();
    }

    private void searchStudent(){
        if (isAllCorrect()){
            if (searchPanel != null) frame.remove(searchPanel);
            searchPanel = new StudentTableWithPaging(internationalization);
            searchPanel.getTableModel().setNumberExaminations(studentTableWithPaging.getTableModel().getNumberExaminations());
            for (Student student: tableModel.getStudents()) {
                if (compliesTemplate(student)) {
                    searchPanel.getTableModel().getStudents().add(student);
                }
            }
            searchPanel.updateComponent();
            frame.add(searchPanel, BorderLayout.CENTER);
            frame.setSize(new Dimension(850,350));
            frame.revalidate();
            frame.repaint();
        } else {
            JOptionPane.showMessageDialog
                    (null, internationalization.getLang().getString("message_1"), internationalization.getLang().getString("error"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeStudent(){
        if (isAllCorrect()){
            int counterStudent = 0;
            Iterator<Student> itr = tableModel.getStudents().iterator();
            while (itr.hasNext()) {
                Student student = itr.next();
                if (compliesTemplate(student)) {
                    itr.remove();
                    counterStudent++;
                }
            }
            tableModel.setNumberMaxExaminations();
            studentTableWithPaging.updateComponent();
            if (counterStudent > 0) {
                JOptionPane.showMessageDialog
                        (null,  counterStudent  + " " + internationalization.getLang().getString("st") + " " + " " + internationalization.getLang().getString("deleted"), internationalization.getLang().getString("info"), JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog
                        (null, internationalization.getLang().getString("message_4"), internationalization.getLang().getString("warning"), JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog
                    (null, internationalization.getLang().getString("message_1"), internationalization.getLang().getString("error"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean compliesTemplate(Student student) {
        if (!lastName.getText().equals("") && !lastName.getText().equals(student.getLastName())) return false;
        if (!group.getText().equals("") && !group.getText().equals(student.getNumberGroup())) return false;
        if (!getExaminationsMarkNull(minMiddleMark) && !isaCompliesMiddleMark(student)) return false;
        if (!getExaminationsMarkNull(minMark) && !isaCompliesMark(student)) return false;
        return true;
    }

    private boolean isaCompliesMiddleMark(Student student) {
        return  getExaminationsMarkInt(minMiddleMark) <= student.getMiddleMark() &&
                getExaminationsMarkInt(maxMiddleMark) >= student.getMiddleMark();
    }

    private boolean isaCompliesMark(Student student) {
        boolean answer = true;
        if (student.getExaminations().size() == 0) return false;
        for (Examination exam : student.getExaminations()) {
            int examMark = exam.getExaminationMarkInt();
            if (!(getExaminationsMarkInt(minMark) <= examMark && getExaminationsMarkInt(maxMark) >= examMark))
                answer = false;
        }
        return answer;
    }

    private boolean isAllCorrect() {
        return (!(isNotCorrectLastName() || isNotCorrectGroup() ||
                isNotCorrectMark(minMiddleMark, maxMiddleMark) || isNotCorrectMark(minMark, maxMark)));
    }

    private boolean isNotCorrectGroup() {
        Pattern p = Pattern.compile("[0-9]+");
        return (!p.matcher(group.getText()).matches() ^ group.getText().equals(""));
    }

    private boolean isNotCorrectLastName() {
        return (lastName.getText().length() > 0 && lastName.getText().charAt(0) == ' ');
    }

    private boolean isNotCorrectMark(JComboBox min, JComboBox max) {
        return ((getExaminationsMarkNull(min) && !getExaminationsMarkNull(max)) ||
                (getExaminationsMarkNull(max) && !getExaminationsMarkNull(min)) ||
                (getExaminationsMarkInt(min) > getExaminationsMarkInt(max)));
    }

    private boolean getExaminationsMarkNull(JComboBox markBox){
        return markBox.getSelectedItem().equals("-");
    }

    private int getExaminationsMarkInt(JComboBox markBox){
        if (getExaminationsMarkNull(markBox)) return 0;
        return Integer.parseInt((String)markBox.getSelectedItem());
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
