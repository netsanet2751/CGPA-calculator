package com.cgpa;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainApp extends Application {

    private final TextField studentName = new TextField();
    private final TextField studentId = new TextField();
    private final TextField courseCode = new TextField();
    private final TextField courseTitle = new TextField();
    private final TextField credits = new TextField();
    private final TextField score = new TextField();

    private final Label totalCredits = new Label("0");
    private final Label cgpaResult = new Label("0.00");
    private final Label statusResult = new Label("Not calculated");

    private final TableView<Course> table = new TableView<>();

    @Override
    public void start(Stage stage) {
        Label heading = new Label("CGPA CALCULATOR");
        heading.setFont(Font.font("Arial", 28));
        heading.setTextFill(Color.WHITE);

        Label subtitle = new Label("Student Academic Performance System");
        subtitle.setTextFill(Color.WHITE);

        VBox header = new VBox(8, heading, subtitle);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(25));
        header.setStyle("-fx-background-color: #243B64;");

        studentName.setPromptText("Enter student name");
        studentId.setPromptText("Enter student ID");

        HBox studentInfo = new HBox(15,
                createField("Student Name", studentName),
                createField("Student ID", studentId));

        studentInfo.setPadding(new Insets(15));

        courseCode.setPromptText("e.g. CS101");
        courseTitle.setPromptText("e.g. Introduction to Programming");
        credits.setPromptText("e.g. 3");
        score.setPromptText("0–100");

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(12);
        form.setPadding(new Insets(20));

        form.add(new Label("Course Code"), 0, 0);
        form.add(courseCode, 1, 0);

        form.add(new Label("Course Title"), 2, 0);
        form.add(courseTitle, 3, 0);

        form.add(new Label("Credit Hours"), 0, 1);
        form.add(credits, 1, 1);

        form.add(new Label("Score"), 2, 1);
        form.add(score, 3, 1);

        for (javafx.scene.Node node : form.getChildren()) {
            if (node instanceof Label label) {
                label.setStyle("-fx-font-weight: bold;");
            }
        }

        TableColumn<Course, String> codeColumn = new TableColumn<>("Course Code");
        codeColumn.setCellValueFactory(data -> data.getValue().codeProperty());

        TableColumn<Course, String> titleColumn = new TableColumn<>("Course Title");
        titleColumn.setCellValueFactory(data -> data.getValue().titleProperty());

        TableColumn<Course, Number> creditColumn = new TableColumn<>("Credits");
        creditColumn.setCellValueFactory(data -> data.getValue().creditsProperty());

        TableColumn<Course, Number> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(data -> data.getValue().scoreProperty());

        TableColumn<Course, String> gradeColumn = new TableColumn<>("Grade");
        gradeColumn.setCellValueFactory(data -> data.getValue().gradeProperty());

        table.getColumns().addAll(codeColumn, titleColumn, creditColumn, scoreColumn, gradeColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setPlaceholder(new Label("No courses added yet"));

        Button addButton = new Button("Add Course");
        Button removeButton = new Button("Remove Selected");
        Button calculateButton = new Button("Calculate CGPA");
        Button clearButton = new Button("Clear All");

        addButton.setOnAction(e -> addCourse());
        removeButton.setOnAction(e -> table.getItems().remove(table.getSelectionModel().getSelectedItem()));
        calculateButton.setOnAction(e -> calculateCGPA());
        clearButton.setOnAction(e -> clearAll());

        addButton.setStyle("-fx-background-color: #287A55; -fx-text-fill: white;");
        calculateButton.setStyle("-fx-background-color: #243B64; -fx-text-fill: white;");

        HBox buttons = new HBox(12, addButton, removeButton, calculateButton, clearButton);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(15));

        Label creditsHeading = new Label("TOTAL CREDITS");
        creditsHeading.setStyle("-fx-font-weight: bold;");

        Label cgpaHeading = new Label("CGPA");
        cgpaHeading.setStyle("-fx-font-weight: bold;");

        Label statusHeading = new Label("ACADEMIC STATUS");
        statusHeading.setStyle("-fx-font-weight: bold;");

        VBox creditsCard = new VBox(10, creditsHeading, totalCredits);
        VBox cgpaCard = new VBox(10, cgpaHeading, cgpaResult);
        VBox statusCard = new VBox(10, statusHeading, statusResult);

        for (VBox card : new VBox[]{creditsCard, cgpaCard, statusCard}) {
            card.setAlignment(Pos.CENTER);
            card.setPadding(new Insets(20));
            card.setStyle(
                    "-fx-background-color: white;" +
                    "-fx-border-color: #D5DDE8;" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;");
            HBox.setHgrow(card, Priority.ALWAYS);
        }

        cgpaResult.setFont(Font.font("Arial", 24));
        cgpaResult.setTextFill(Color.web("#287A55"));

        HBox results = new HBox(15, creditsCard, cgpaCard, statusCard);
        results.setPadding(new Insets(15));

        VBox content = new VBox(10, studentInfo, form, buttons, table, results);
        content.setPadding(new Insets(10));
        VBox.setVgrow(table, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setCenter(content);
        root.setStyle("-fx-background-color: #F2F5FA;");

        Scene scene = new Scene(root, 1050, 750);

        stage.setTitle("New CGPA Calculator");
        stage.setScene(scene);
        stage.setMinWidth(850);
        stage.setMinHeight(650);
        stage.show();
    }

    private VBox createField(String labelText, TextField field) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-weight: bold;");
        VBox box = new VBox(8, label, field);
        HBox.setHgrow(box, Priority.ALWAYS);
        return box;
    }

    private void addCourse() {
        try {
            String code = courseCode.getText().trim();
            String title = courseTitle.getText().trim();
            int credit = Integer.parseInt(credits.getText().trim());
            double marks = Double.parseDouble(score.getText().trim());

            if (code.isEmpty() || title.isEmpty()) {
                showError("Enter both the course code and course title.");
                return;
            }

            if (credit <= 0 || marks < 0 || marks > 100) {
                showError("Credits must be positive and score must be 0–100.");
                return;
            }

            Course course = new Course(code, title, credit, marks);
            table.getItems().add(course);

            courseCode.clear();
            courseTitle.clear();
            credits.clear();
            score.clear();

        } catch (NumberFormatException ex) {
            showError("Enter a valid number for credits and score.");
        }
    }

    private void calculateCGPA() {
        if (table.getItems().isEmpty()) {
            showError("Please add at least one course.");
            return;
        }

        double weightedPoints = 0;
        int creditSum = 0;

        for (Course course : table.getItems()) {
            weightedPoints += course.getGradePoint() * course.getCredits();
            creditSum += course.getCredits();
        }

        double cgpa = weightedPoints / creditSum;

        totalCredits.setText(String.valueOf(creditSum));
        cgpaResult.setText(String.format("%.2f", cgpa));

        if (cgpa >= 2.0) {
            statusResult.setText("Pass");
        } else if (cgpa >= 1.75) {
            statusResult.setText("Warning");
        } else if (cgpa >= 1.5) {
            statusResult.setText("Readmission");
        } else {
            statusResult.setText("Dismissal");
        }
    }

    private void clearAll() {
        table.getItems().clear();
        studentName.clear();
        studentId.clear();
        totalCredits.setText("0");
        cgpaResult.setText("0.00");
        statusResult.setText("Not calculated");
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText("Please check your information");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}