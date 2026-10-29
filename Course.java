```java
package com.cgpa;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Course {

    private final SimpleStringProperty code;
    private final SimpleStringProperty title;
    private final SimpleIntegerProperty credits;
    private final SimpleDoubleProperty score;
    private final SimpleStringProperty grade;

    public Course(String code, String title, int credits, double score) {

        this.code = new SimpleStringProperty(code);
        this.title = new SimpleStringProperty(title);
        this.credits = new SimpleIntegerProperty(credits);
        this.score = new SimpleDoubleProperty(score);

        this.grade = new SimpleStringProperty(calculateGrade(score));
    }

    private String calculateGrade(double score) {

        if (score >= 90) {
            return "A";
        } else if (score >= 85) {
            return "A-";
        } else if (score >= 80) {
            return "B+";
        } else if (score >= 75) {
            return "B";
        } else if (score >= 70) {
            return "B-";
        } else if (score >= 65) {
            return "C+";
        } else if (score >= 60) {
            return "C";
        } else if (score >= 50) {
            return "D";
        } else {
            return "F";
        }
    }

    public double getGradePoint() {

        switch (grade.get()) {
            case "A":
                return 4.0;
            case "A-":
                return 3.75;
            case "B+":
                return 3.5;
            case "B":
                return 3.0;
            case "B-":
                return 2.75;
            case "C+":
                return 2.5;
            case "C":
                return 2.0;
            case "D":
                return 1.0;
            default:
                return 0.0;
        }
    }

    public String getCode() {
        return code.get();
    }

    public String getTitle() {
        return title.get();
    }

    public int getCredits() {
        return credits.get();
    }

    public double getScore() {
        return score.get();
    }

    public String getGrade() {
        return grade.get();
    }

    public SimpleStringProperty codeProperty() {
        return code;
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public SimpleIntegerProperty creditsProperty() {
        return credits;
    }

    public SimpleDoubleProperty scoreProperty() {
        return score;
    }

    public SimpleStringProperty gradeProperty() {
        return grade;
    }
}
```