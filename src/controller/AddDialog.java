package controller;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

public class AddDialog implements Observer{
    private static Internationalization internationalization = new Internationalization();
    private static  String LAST_NAME = internationalization.lang.getString("last_name");
    private static  String FIRST_NAME = internationalization.lang.getString("first_name");
    private static  String MIDDLE_NAME = internationalization.lang.getString("middle_name");
    private static  String GROUP = internationalization.lang.getString("group");
    private StudentTableWithPaging studentTableWithPaging;
    private TableModel tableModel;
    private Map<String, JTextField> fieldID = new HashMap<String, JTextField>();
    private Map<JTextField, JComboBox> examinationsMap = new HashMap<JTextField, JComboBox>();
    public JFrame frame = new JFrame(internationalization.lang.getString("add_st"));
    public JLabel labelText = new JLabel(internationalization.lang.getString("add_new_st"));
    public JLabel labelText1 = new JLabel(internationalization.lang.getString("examinations"));
    public JLabel labelText2 = new JLabel(internationalization.lang.getString("name"));
    public JLabel labelText3 = new JLabel(internationalization.lang.getString("mark"));
    public String message_1 = internationalization.lang.getString("message_1");
    public String error = internationalization.lang.getString("error");

    public AddDialog(StudentTableWithPaging studentTableWithPaging) {
        this.studentTableWithPaging = studentTableWithPaging;
        tableModel = studentTableWithPaging.getTableModel();
        JFrame frame = createFrame();
        frame.pack();
        frame.setLocationRelativeTo(studentTableWithPaging);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private JFrame createFrame(){
        JPanel jPanelID = new JPanel();
        jPanelID.setLayout(new GridBagLayout());
        labelText.setHorizontalAlignment(JLabel.CENTER);
        AddComponent.add(jPanelID,labelText, 0, 0, 2, 1);
        String[] labelString = {LAST_NAME, FIRST_NAME, MIDDLE_NAME, GROUP};
        for (int field = 0; field < labelString.length; field++) {
            labelText = new JLabel(labelString[field]);
            AddComponent.add(jPanelID, labelText, 0, field + 1, 1, 1);
            JTextField jtfField = new JTextField(30);
            fieldID.put(labelString[field],jtfField);
            AddComponent.add(jPanelID, jtfField, 1, field + 1, 1, 1);
        }
        labelText1.setHorizontalAlignment(JLabel.CENTER);
        AddComponent.add(jPanelID,labelText1, 0, 5, 2, 1);
        labelText2.setHorizontalAlignment(JLabel.CENTER);
        AddComponent.add(jPanelID,labelText2, 0, 6, 1, 1);
        labelText3.setHorizontalAlignment(JLabel.CENTER);
        AddComponent.add(jPanelID,labelText3, 1, 6, 1, 1);
        String[] markString = {"-", "4", "5", "6", "7", "8", "9", "10"};
        for (int exam=0; exam < tableModel.getNumberExaminations();exam++){
            JTextField jtfName = new JTextField(30);
            JComboBox jcbMark = new JComboBox(markString);
            examinationsMap.put(jtfName, jcbMark);
            AddComponent.add(jPanelID, jtfName, 0, exam+7, 1, 1);
            AddComponent.add(jPanelID, jcbMark, 1, exam+7, 2, 1);
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
        if (!isAllCorrect()){
            JOptionPane.showMessageDialog
                    (null, message_1, error, JOptionPane.ERROR_MESSAGE);
        } else
        {
            List<Examination> examinations = new ArrayList<>();
            for (Map.Entry exam: examinationsMap.entrySet()) {
                JTextField name = (JTextField)exam.getKey();
                JComboBox mark = (JComboBox)exam.getValue();
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
        for (Map.Entry exam: examinationsMap.entrySet()) {
            JTextField name = (JTextField)exam.getKey();
            JComboBox mark = (JComboBox)exam.getValue();
            if (isNotCorrectExamination(name.getText(), (String)mark.getSelectedItem())) return true;
        }
        return false;
    }

    private boolean isNotCorrectExamination(String name, String mark) {
        return ((name.equals("") && !mark.equals("-")) ||
                (mark.equals("-") && !name.equals("")) ||
                (name.length() > 0 && name.charAt(0) == ' '));
    }

    public void updateComponent(){
        System.out.println(internationalization.lang.getString("lang"));
        LAST_NAME = internationalization.lang.getString("last_name");
        FIRST_NAME = internationalization.lang.getString("first_name");
        MIDDLE_NAME = internationalization.lang.getString("middle_name");
        GROUP = internationalization.lang.getString("group");
        frame.setTitle(internationalization.lang.getString("add_st"));
        labelText.setText(internationalization.lang.getString("add_new_st"));
        labelText1.setText(internationalization.lang.getString("examinations"));
        labelText2.setText(internationalization.lang.getString("name"));
        labelText3.setText(internationalization.lang.getString("mark"));
        message_1 = internationalization.lang.getString("message_1");
        error = internationalization.lang.getString("error");
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("vvv");
        updateComponent();
    }
}
