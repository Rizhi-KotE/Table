package controller;

import model.Examination;
import model.Student;
import model.TableModel;
import view.MainWindow;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class FileHandler implements Observer{

    private static final String LAST_NAME = "lastName";
    private static final String FIRST_NAME = "firstName";
    private static final String MIDDLE_NAME = "middleName";
    private static final String GROUP = "group";
    private static final String EXAM = "exam";
    private static final String NAME = "name";
    private static final String NUMBER_EXAM = "numberExam";
    private static final String STUDENT = "student";
    private static final String STUDENTS = "students";
    private static final String FORMAT = "UTF-8";
    private static final String VER = "1.0";
    private static final String EXTENSION = "stable";
    private Internationalization internationalization;
    private MainWindow mainWindow;
    private StudentTableWithPaging studentTableWithPaging;
    private TableModel tableModel;
    private final String message_2;
    private final String message_3;
    private final String error;


    public FileHandler(Internationalization internationalization){
        message_2 = internationalization.lang.getString("message_2");
        message_3 = internationalization.lang.getString("message_3");
        error = internationalization.lang.getString("error");
    }

    private void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    private void setStdentTableWithPaging(StudentTableWithPaging studentTableWithPaging) {
        this.studentTableWithPaging = studentTableWithPaging;
        this.tableModel = studentTableWithPaging.getTableModel();
    }

    public void openFile(){
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter( "Student table", EXTENSION));
        if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            openXMLFile(fc.getSelectedFile().getPath());
        }
    }

    public void saveFile(){
        try {
            JFileChooser fc = new JFileChooser();
            if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                XMLOutputFactory output = XMLOutputFactory.newInstance();
                XMLStreamWriter writer = output.createXMLStreamWriter
                        (new FileWriter(fc.getSelectedFile() + "." + EXTENSION));
                writer.writeStartDocument(FORMAT, VER);
                writer.writeStartElement(STUDENTS);
                writer.writeStartElement(NUMBER_EXAM);
                writer.writeCharacters(Integer.toString(tableModel.getNumberExaminations()));
                writer.writeEndElement();
                for (Student student: tableModel.getStudents()){
                    writer.writeStartElement(STUDENT);
                    writer.writeAttribute(LAST_NAME, student.getLastName());
                    writer.writeAttribute(FIRST_NAME, student.getFirstName());
                    writer.writeAttribute(MIDDLE_NAME, student.getMiddleName());
                    writer.writeAttribute(GROUP, student.getNumberGroup());
                    for (Examination exam : student.getExaminations()) {
                        writer.writeStartElement(EXAM);
                        writer.writeAttribute(NAME, exam.getExaminationName());
                        writer.writeCharacters(exam.getExaminationMark());
                        writer.writeEndElement();
                    }
                    writer.writeEndElement();
                }
                writer.writeEndElement();
                writer.writeEndDocument();
                writer.flush();
            }
        } catch (Exception eSave) {
            JOptionPane.showMessageDialog
                    (null, message_2, error, JOptionPane.ERROR_MESSAGE|JOptionPane.OK_OPTION);
        }
    }

    public void openXMLFile(String fileName) {
        try {
            String name;
            String mark;
            String lastName = "";
            String firstName = "";
            String middleName = "";
            String group = "";
            String numberExam = "";
            tableModel.getStudents().clear();
            List<Examination> exams = new ArrayList<Examination>();
            XMLStreamReader xmlr = XMLInputFactory.newInstance()
                    .createXMLStreamReader(fileName, new FileInputStream(fileName));
            while (xmlr.hasNext()) {
                xmlr.next();
                if (xmlr.isStartElement()) {
                    if (xmlr.getLocalName().equals(STUDENT)){
                        exams = new ArrayList<Examination>();
                        lastName = xmlr.getAttributeValue(null, LAST_NAME);
                        firstName = xmlr.getAttributeValue(null, FIRST_NAME);
                        middleName = xmlr.getAttributeValue(null, MIDDLE_NAME);
                        group = xmlr.getAttributeValue(null, GROUP);
                    }
                    else if (xmlr.getLocalName().equals(EXAM)){
                        name = xmlr.getAttributeValue(null, NAME);
                        xmlr.next();
                        mark = xmlr.getText();
                        exams.add(new Examination(name, Integer.parseInt(mark)));
                    } else if (xmlr.getLocalName().equals(NUMBER_EXAM)){
                        xmlr.next();
                        numberExam = xmlr.getText();
                    }
                } else if (xmlr.isEndElement()) {
                    if (xmlr.getLocalName().equals(STUDENT)){
                        tableModel.getStudents().add(new Student(lastName, firstName, middleName, group, exams));
                    }
                }
            }
            tableModel.setNumberExaminations(Integer.parseInt(numberExam));
            studentTableWithPaging.updateComponent();
        } catch (Exception e) {
            JOptionPane.showMessageDialog
                    (null, message_3, error, JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
