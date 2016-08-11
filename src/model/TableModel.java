package model;

import java.util.ArrayList;
import java.util.List;


public class TableModel {

    private int numberExaminations = 5;
    private int numberMaxExaminations;
    private List<Student> students;

    public TableModel() {
        students = new ArrayList<Student>();
    }

    public int getNumberExaminations() {
        return numberExaminations;
    }

    public void setNumberExaminations(int numberExaminations) {
        this.numberExaminations = numberExaminations;
    }

    public int getNumberMaxExaminations() {
        return numberMaxExaminations;
    }

    public void setNumberMaxExaminations(int maxExaminations) {
        numberMaxExaminations = (maxExaminations > numberMaxExaminations) ? maxExaminations : numberMaxExaminations;
    }

    public void setNumberMaxExaminations() {
        numberMaxExaminations = 5;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

}
