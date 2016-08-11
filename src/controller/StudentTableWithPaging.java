package controller;

import model.Student;
import model.TableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class StudentTableWithPaging extends JComponent implements Observer{

    private TableModel tableModel;
    private JScrollPane scrollTable;
    private Internationalization internationalization;
    private int currentPage = 1;
    private int studentOnPage = 10;
    private int heightTable;
    public int i = 1;

    public StudentTableWithPaging(Internationalization internationalization) {
        this.internationalization = internationalization;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        tableModel = new TableModel();
        heightTable = 250;
        makePanel();
    }

    public void makePanel() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(makeTable(), BorderLayout.NORTH);
        scrollTable = new JScrollPane(tablePanel);
        scrollTable.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent evt) {
                updateScrollTable();
            }
        });
        add(scrollTable);
        add(makeToolsPanel());
    }

    private JPanel makeTable() {
        JPanel table = new JPanel();
        table.setLayout(new GridBagLayout());
        int numberExaminations = tableModel.getNumberExaminations();
        List<Student> students = tableModel.getStudents();
        AddComponent.add(table, internationalization.getLang().getString("full_name"), 0, 0, 1, 3);
        AddComponent.add(table, internationalization.getLang().getString("group"), 1, 0, 1, 3);
        AddComponent.add(table, internationalization.getLang().getString("examinations"), 2, 0, numberExaminations * 2, 1);
        for (int i = 0, x = 2; i < numberExaminations; i++, x += 2) {
            AddComponent.add(table, Integer.toString(i + 1), x, 1, 2, 1);
            AddComponent.add(table, internationalization.getLang().getString("name"), x, 2, 1, 1);
            AddComponent.add(table, internationalization.getLang().getString("mark"), x + 1, 2, 1, 1);
        }
        int firstStudentOnPage = studentOnPage * (currentPage - 1);
        int lineInHeaderTable = 3;
        for (int y = lineInHeaderTable, student = firstStudentOnPage;
             y < studentOnPage + lineInHeaderTable && student < students.size();
             y++, student++) {
            tableModel.setNumberMaxExaminations(students.get(student).getExaminations().size());
            for (int i = 0; i < numberExaminations * 2 + 2; i++) {
                String write = getFieldForStudent(students.get(student), i);
                AddComponent.add(table, write, i, y, 1, 1);
            }
        }
        return table;
    }

    private JPanel makeToolsPanel() {
        int numberExaminations = tableModel.getNumberExaminations();
        List<Student> students = tableModel.getStudents();
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        String statusBar = internationalization.getLang().getString("page") + ": " + currentPage + "/" + getNumberMaxPage()
                + "   " + internationalization.getLang().getString("records") + ": " + students.size() + " ";
        panel.add(new JLabel(statusBar));
        panel.add(AddComponent.makeButton(new JButton(), "FIRST_12.png", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                firstPage();
            }
        }));
        panel.add(AddComponent.makeButton(new JButton(), "PREVIOUS_12.png", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                prevPage();
            }
        }));
        panel.add(AddComponent.makeButton(new JButton(), "NEXT_12.png", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextPage();
            }
        }));
        panel.add(AddComponent.makeButton(new JButton(), "LAST_12.png", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lastPage();
            }
        }));
        JLabel label = new JLabel(" " + internationalization.getLang().getString("students") + ": ");
        panel.add(label);
        String[] sizeStudent = {"10", "20", "30", "50", "100"};
        JComboBox sizeBox = new JComboBox(sizeStudent);
        sizeBox.setSelectedIndex(Arrays.asList(sizeStudent).indexOf(Integer.toString(studentOnPage)));
        sizeBox.setMaximumSize(sizeBox.getPreferredSize());
        sizeBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeStudentOnPage(e);
            }
        });
        panel.add(sizeBox);
        label = new JLabel("   " + internationalization.getLang().getString("exams") + ": ");
        panel.add(label);
        String[] sizeExam = {"5", "6", "7", "8", "9", "10", "12", "15", "20"};
        JComboBox examBox = new JComboBox(sizeExam);
        examBox.setSelectedIndex(Arrays.asList(sizeExam).indexOf(Integer.toString(numberExaminations)));
        examBox.setMaximumSize(examBox.getPreferredSize());
        examBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeNumberExam(e);
            }
        });
        panel.add(examBox);
        ImageIcon iconRU = new ImageIcon("img/RU.png");
        ImageIcon iconUK = new ImageIcon("img/UK.png");
        String selected_lang = "   " + internationalization.getLang().getString("lang");
        JLabel label_lang = new JLabel(selected_lang);
        panel.add(label_lang);
        JComboBox c = new JComboBox();
        c.addItem(iconRU);
        c.addItem(iconUK);
        c.setSelectedIndex(0);
        c.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                i = c.getSelectedIndex();
                if (i == 0) {
                    internationalization.setLang("ru");
                    System.out.println(internationalization.getLang().getString("lang") + " : Русский");
                }
                if (i == 1) {
                    internationalization.setLang("eng");
                    System.out.println(internationalization.getLang().getString("lang") + " : English");
                }
            }
        });
        panel.add(c);
        panel.setMaximumSize(new Dimension(840, 100));
        return panel;
    }

    public String getFieldForStudent(Student student, int i) {
        if (i == 0) return student.getLastName() + " " + student.getFirstName() + " " + student.getMiddleName();
        else if (i == 1) return student.getNumberGroup();
        else {
            int numberExamination = (i - 2) / 2;
            if (i % 2 == 0) {
                if (numberExamination < student.getExaminations().size()) {
                    return student.getExaminations().get(numberExamination).getExaminationName();
                } else return " - ";
            } else {
                if (numberExamination < student.getExaminations().size()) {
                    return student.getExaminations().get(numberExamination).getExaminationMark();
                } else return " - ";
            }
        }
    }

    public void nextPage() {
        if (hasNextPage()) {
            currentPage++;
            updateComponent();
        }
    }

    private boolean hasNextPage() {
        return tableModel.getStudents().size() > studentOnPage * (currentPage - 1) + studentOnPage;
    }

    public void prevPage() {
        if (currentPage > 1) {
            currentPage--;
            updateComponent();
        }
    }

    public void firstPage() {
        if (currentPage > 1) {
            currentPage = 1;
            updateComponent();
        }
    }

    public void lastPage() {
        if (currentPage != getNumberMaxPage()) {
            currentPage = getNumberMaxPage();
            updateComponent();
        }
    }

    private int getNumberMaxPage() {
        return (int) ((tableModel.getStudents().size() - 1) / studentOnPage) + 1;
    }

    public void changeStudentOnPage(ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        String change = (String) cb.getSelectedItem();
        if (studentOnPage != Integer.parseInt(change)) {
            studentOnPage = Integer.parseInt(change);
            updateComponent();
        }
    }

    public TableModel getTableModel() {
        return tableModel;
    }

    public void setTableModel(TableModel tableModel) {
        this.tableModel = tableModel;
    }

    public int getHeightTable() {
        return heightTable;
    }

    public void setHeightTable(int heightTable) {
        this.heightTable = heightTable;
    }

    public JScrollPane getScrollTable() {
        return scrollTable;
    }

    public void setScrollTable(JScrollPane scrollTable) {
        this.scrollTable = scrollTable;
    }

    public void updateComponent() {
        removeAll();
        makePanel();
        revalidate();
        repaint();
    }

    private void updateScrollTable() {
        scrollTable.revalidate();
        scrollTable.repaint();
    }

    public void changeNumberExam(ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        String change = (String) cb.getSelectedItem();
        if (canChangeNumberExam(change)) {
            tableModel.setNumberExaminations(Integer.parseInt(change));
            updateComponent();
        } else {
            JOptionPane.showMessageDialog
                    (null, internationalization.getLang().getString("message_5") + " " + tableModel.getNumberMaxExaminations(),
                            internationalization.getLang().getString("error"), JOptionPane.ERROR_MESSAGE | JOptionPane.OK_OPTION);
        }
    }

    private boolean canChangeNumberExam(String change) {
        return tableModel.getNumberMaxExaminations() <= Integer.parseInt(change);
    }

    @Override
    public void update(Observable obs, Object arg) {
        System.out.println("ddd");
        updateComponent();
    }
}

