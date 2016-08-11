package controller;

import model.Examination;
import model.Student;
import model.TableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

public class AddDialog implements Observer {
    private Internationalization internationalization;
    private StudentTableWithPaging studentTableWithPaging;
    private TableModel tableModel;
    private Map<String, JTextField> fieldID = new HashMap<String, JTextField>();
    private Map<JTextField, JComboBox> examinationsMap = new HashMap<JTextField, JComboBox>();
    private String LAST_NAME;
    private String FIRST_NAME;
    private String MIDDLE_NAME;
    private String GROUP;
    private JFrame frame;
    private JLabel labelText;
    private JLabel labelText1;
    private JLabel labelText2;
    private JLabel labelText3;
    private String message_1;
    private String error;

    public AddDialog(StudentTableWithPaging studentTableWithPaging, Internationalization internationalization) {
        this.studentTableWithPaging = studentTableWithPaging;
        this.internationalization = internationalization;
        LAST_NAME = internationalization.getLang().getString("last_name");
        FIRST_NAME = internationalization.getLang().getString("first_name");
        MIDDLE_NAME = internationalization.getLang().getString("middle_name");
        GROUP = internationalization.getLang().getString("group");
        frame = new JFrame(internationalization.getLang().getString("add_st"));
        labelText = new JLabel(internationalization.getLang().getString("add_new_st"));
        labelText1 = new JLabel(internationalization.getLang().getString("examinations"));
        labelText2 = new JLabel(internationalization.getLang().getString("name"));
        labelText3 = new JLabel(internationalization.getLang().getString("mark"));
        message_1 = internationalization.getLang().getString("message_1");
        error = internationalization.getLang().getString("error");


        tableModel = studentTableWithPaging.getTableModel();
        JFrame frame = createFrame();
        frame.pack();
        frame.setLocationRelativeTo(studentTableWithPaging);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private JFrame createFrame() {
        JPanel jPanelID = new JPanel();
        jPanelID.setLayout(new GridBagLayout());
        labelText.setHorizontalAlignment(JLabel.CENTER);
        AddComponent.add(jPanelID, labelText, 0, 0, 2, 1);
        String[] labelString = {LAST_NAME, FIRST_NAME, MIDDLE_NAME, GROUP};
        for (int field = 0; field < labelString.length; field++) {
            labelText = new JLabel(labelString[field]);
            AddComponent.add(jPanelID, labelText, 0, field + 1, 1, 1);
            JTextField jtfField = new JTextField(30);
            fieldID.put(labelString[field], jtfField);
            AddComponent.add(jPanelID, jtfField, 1, field + 1, 1, 1);
        }
        labelText1.setHorizontalAlignment(JLabel.CENTER);
        AddComponent.add(jPanelID, labelText1, 0, 5, 2, 1);
        labelText2.setHorizontalAlignment(JLabel.CENTER);
        AddComponent.add(jPanelID, labelText2, 0, 6, 1, 1);
        labelText3.setHorizontalAlignment(JLabel.CENTER);
        AddComponent.add(jPanelID, labelText3, 1, 6, 1, 1);
        String[] markString = {"-", "4", "5", "6", "7", "8", "9", "10"};
        for (int exam = 0; exam < tableModel.getNumberExaminations(); exam++) {
            JTextField jtfName = new JTextField(30);
            JComboBox jcbMark = new JComboBox(markString);
            examinationsMap.put(jtfName, jcbMark);
            AddComponent.add(jPanelID, jtfName, 0, exam + 7, 1, 1);
            AddComponent.add(jPanelID, jcbMark, 1, exam + 7, 2, 1);
        }
        frame.add(jPanelID, BorderLayout.NORTH);
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createNewStudent();
            }
        });
        frame.add(okButton, BorderLayout.SOUTH);
        return frame;
    }

    private void createNewStudent() {
        if (!isAllCorrect()) {
            JOptionPane.showMessageDialog
                    (null, message_1, error, JOptionPane.ERROR_MESSAGE);
        } else {
            List<Examination> examinations = new ArrayList<>();
            for (Map.Entry exam : examinationsMap.entrySet()) {
                JTextField name = (JTextField) exam.getKey();
                JComboBox mark = (JComboBox) exam.getValue();
                if (!name.getText().equals("")) {
                    examinations.add(new Examination(name.getText(),
                            Integer.parseInt((String) mark.getSelectedItem())));
                }
            }
            tableModel.getStudents().add(new Student(getTextID(LAST_NAME),
                    getTextID(FIRST_NAME),
                    getTextID(MIDDLE_NAME),
                    getTextID(GROUP),
                    examinations));
            studentTableWithPaging.updateComponent();
        }
    }

    private boolean isAllCorrect() {
        return (!(isNotCorrectID(LAST_NAME) || isNotCorrectID(FIRST_NAME) || isNotCorrectID(MIDDLE_NAME) ||
                isNotCorrectGroup() || isNotCorrectExaminations()));
    }

    private boolean isNotCorrectGroup() {
        Pattern p = Pattern.compile("[0-9]+");
        return !p.matcher(fieldID.get(GROUP).getText()).matches();
    }

    private String getTextID(String key) {
        return fieldID.get(key).getText();
    }

    private boolean isNotCorrectID(String key) {
        return ((fieldID.get(key).getText().equals("")) ||
                (fieldID.get(key).getText().length() > 0 && fieldID.get(key).getText().charAt(0) == ' '));
    }

    private boolean isNotCorrectExaminations() {
        for (Map.Entry exam : examinationsMap.entrySet()) {
            JTextField name = (JTextField) exam.getKey();
            JComboBox mark = (JComboBox) exam.getValue();
            if (isNotCorrectExamination(name.getText(), (String) mark.getSelectedItem())) return true;
        }
        return false;
    }

    private boolean isNotCorrectExamination(String name, String mark) {
        return ((name.equals("") && !mark.equals("-")) ||
                (mark.equals("-") && !name.equals("")) ||
                (name.length() > 0 && name.charAt(0) == ' '));
    }

    public void updateComponent() {
        LAST_NAME = internationalization.getLang().getString("last_name");
        FIRST_NAME = internationalization.getLang().getString("first_name");
        MIDDLE_NAME = internationalization.getLang().getString("middle_name");
        GROUP = internationalization.getLang().getString("group");
        frame.setTitle(internationalization.getLang().getString("add_st"));
        labelText.setText(internationalization.getLang().getString("add_new_st"));
        labelText1.setText(internationalization.getLang().getString("examinations"));
        labelText2.setText(internationalization.getLang().getString("name"));
        labelText3.setText(internationalization.getLang().getString("mark"));
        message_1 = internationalization.getLang().getString("message_1");
        error = internationalization.getLang().getString("error");
        frame.dispose();
        frame.setVisible(true);
        createFrame();
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("addDialog change languige");
        updateComponent();
    }
}
