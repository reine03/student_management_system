package com.student;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Controller {

    @FXML private TextField txtName;
    @FXML private TextField txtCourse;
    @FXML private ChoiceBox<YearLevel> cbYear;

    @FXML private TableView<Student> table;
    @FXML private TableColumn<Student, Integer> colId;
    @FXML private TableColumn<Student, String> colName;
    @FXML private TableColumn<Student, String> colCourse;
    @FXML private TableColumn<Student, String> colYear;

    @FXML private Label lblStatus;

    private final ObservableList<Student> list = FXCollections.observableArrayList();
    private Connection conn;
    private int selectedId = -1;
    private int nextLocalId = 1;

    @FXML
    public void initialize() {
        conn = DBConnection.connect();

        if (conn == null) {
            showStatus("Ready. Records are temporary until a database is configured.", false);
        } else {
            showStatus("Connected to database successfully.", false);
        }

        cbYear.getItems().setAll(YearLevel.values());

        colId.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        colName.setCellValueFactory(data -> data.getValue().nameProperty());
        colCourse.setCellValueFactory(data -> data.getValue().courseProperty());
        colYear.setCellValueFactory(data -> data.getValue().yearLevelProperty());

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, student) -> {
            if (student != null) {
                selectedId = student.getId();
                txtName.setText(student.getName());
                txtCourse.setText(student.getCourse());
                cbYear.setValue(findYearLevel(student.getYearLevel()));
            }
        });

        loadData();
    }

    private void loadData() {
        list.clear();
        table.setItems(list);

        if (conn == null) {
            return;
        }

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM students ORDER BY id")) {
            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("course"),
                        rs.getString("year_level")
                ));
            }
        } catch (Exception e) {
            showStatus("Error loading data: " + e.getMessage(), true);
        }
    }

    @FXML
    private void addStudent() {
        if (!validateInputs()) {
            return;
        }

        if (conn == null) {
            list.add(new Student(
                    nextLocalId++,
                    txtName.getText().trim(),
                    txtCourse.getText().trim(),
                    cbYear.getValue().toString()
            ));
            showStatus("Student added locally. Connect the database to save permanently.", false);
            resetForm();
            return;
        }

        try (PreparedStatement pst = conn.prepareStatement(
                "INSERT INTO students(name, course, year_level) VALUES (?, ?, ?)")) {
            pst.setString(1, txtName.getText().trim());
            pst.setString(2, txtCourse.getText().trim());
            pst.setString(3, cbYear.getValue().toString());
            pst.executeUpdate();

            showStatus("Student added successfully.", false);
            loadData();
            resetForm();
        } catch (Exception e) {
            showStatus("Error adding student: " + e.getMessage(), true);
        }
    }

    @FXML
    private void updateStudent() {
        if (selectedId == -1) {
            showStatus("Please select a student from the table first.", true);
            return;
        }
        if (!validateInputs()) {
            return;
        }

        if (conn == null) {
            updateLocalStudent();
            return;
        }

        try (PreparedStatement pst = conn.prepareStatement(
                "UPDATE students SET name=?, course=?, year_level=? WHERE id=?")) {
            pst.setString(1, txtName.getText().trim());
            pst.setString(2, txtCourse.getText().trim());
            pst.setString(3, cbYear.getValue().toString());
            pst.setInt(4, selectedId);
            pst.executeUpdate();

            showStatus("Student updated successfully.", false);
            loadData();
            resetForm();
        } catch (Exception e) {
            showStatus("Error updating student: " + e.getMessage(), true);
        }
    }

    @FXML
    private void deleteStudent() {
        if (selectedId == -1) {
            showStatus("Please select a student from the table first.", true);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this student?",
                ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                deleteSelectedStudent();
            }
        });
    }

    private void deleteSelectedStudent() {
        if (conn == null) {
            list.removeIf(student -> student.getId() == selectedId);
            showStatus("Student deleted locally.", false);
            resetForm();
            return;
        }

        try (PreparedStatement pst = conn.prepareStatement("DELETE FROM students WHERE id=?")) {
            pst.setInt(1, selectedId);
            pst.executeUpdate();

            showStatus("Student deleted successfully.", false);
            loadData();
            resetForm();
        } catch (Exception e) {
            showStatus("Error deleting student: " + e.getMessage(), true);
        }
    }

    @FXML
    private void clearFields() {
        resetForm();
        showStatus("Fields cleared.", false);
    }

    private void resetForm() {
        txtName.clear();
        txtCourse.clear();
        cbYear.setValue(null);
        selectedId = -1;
        table.getSelectionModel().clearSelection();
    }

    private boolean validateInputs() {
        if (txtName.getText().trim().isEmpty()) {
            showStatus("Name cannot be empty.", true);
            txtName.requestFocus();
            return false;
        }
        if (txtCourse.getText().trim().isEmpty()) {
            showStatus("Course cannot be empty.", true);
            txtCourse.requestFocus();
            return false;
        }
        if (cbYear.getValue() == null) {
            showStatus("Please select a Year Level.", true);
            cbYear.requestFocus();
            return false;
        }
        return true;
    }

    private YearLevel findYearLevel(String displayName) {
        for (YearLevel yearLevel : YearLevel.values()) {
            if (yearLevel.toString().equals(displayName)) {
                return yearLevel;
            }
        }
        return null;
    }

    private void updateLocalStudent() {
        for (int i = 0; i < list.size(); i++) {
            Student student = list.get(i);
            if (student.getId() == selectedId) {
                list.set(i, new Student(
                        selectedId,
                        txtName.getText().trim(),
                        txtCourse.getText().trim(),
                        cbYear.getValue().toString()
                ));
                showStatus("Student updated locally.", false);
                resetForm();
                return;
            }
        }

        showStatus("Selected local student was not found.", true);
    }

    private void showStatus(String message, boolean isError) {
        lblStatus.setText(message);
        lblStatus.setStyle(isError
                ? "-fx-text-fill: #e74c3c;"
                : "-fx-text-fill: #27ae60;");
    }
}
