package com.student;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;

public class Controller {

    @FXML private TextField txtName;
    @FXML private TextField txtCourse;
    @FXML private ChoiceBox<YearLevel> cbYear;

    @FXML private TableView<Student> table;
    @FXML private TableColumn<Student, Integer> colId;
    @FXML private TableColumn<Student, String>  colName;
    @FXML private TableColumn<Student, String>  colCourse;
    @FXML private TableColumn<Student, String>  colYear;

    @FXML private Label lblStatus;

    private final ObservableList<Student> list = FXCollections.observableArrayList();
    private Connection conn;
    private int selectedId = -1;

    // ── Lifecycle ────────────────────────────────────────────────

    @FXML
    public void initialize() {
        conn = DBConnection.connect();

        if (conn == null) {
            showStatus("ERROR: Could not connect to database. Check DBConnection.java settings.", true);
        } else {
            showStatus("Connected to database successfully.", false);
        }

        // Populate ChoiceBox with enum values
        cbYear.getItems().setAll(YearLevel.values());

        // Bind TableView columns
        colId.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        colName.setCellValueFactory(data -> data.getValue().nameProperty());
        colCourse.setCellValueFactory(data -> data.getValue().courseProperty());
        colYear.setCellValueFactory(data -> data.getValue().yearLevelProperty());

        loadData();

        // Row click → populate fields
        table.setOnMouseClicked(e -> {
            Student s = table.getSelectionModel().getSelectedItem();
            if (s != null) {
                selectedId = s.getId();
                txtName.setText(s.getName());
                txtCourse.setText(s.getCourse());

                for (YearLevel y : YearLevel.values()) {
                    if (y.toString().equals(s.getYearLevel())) {
                        cbYear.setValue(y);
                        break;
                    }
                }
            }
        });
    }

    // ── Data Loading ─────────────────────────────────────────────

    private void loadData() {
        list.clear();
        if (conn == null) return;

        try {
            String query = "SELECT * FROM students ORDER BY id";
            ResultSet rs  = conn.createStatement().executeQuery(query);

            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("course"),
                        rs.getString("year_level")
                ));
            }
            table.setItems(list);

        } catch (Exception e) {
            showStatus("Error loading data: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    // ── CRUD Handlers ────────────────────────────────────────────

    @FXML
    private void addStudent() {
        if (!validateInputs()) return;

        try {
            String query = "INSERT INTO students(name, course, year_level) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, txtName.getText().trim());
            pst.setString(2, txtCourse.getText().trim());
            pst.setString(3, cbYear.getValue().toString());
            pst.executeUpdate();

            showStatus("Student added successfully.", false);
            loadData();
            clearFields();

        } catch (Exception e) {
            showStatus("Error adding student: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    @FXML
    private void updateStudent() {
        if (selectedId == -1) {
            showStatus("Please select a student from the table first.", true);
            return;
        }
        if (!validateInputs()) return;

        try {
            String query = "UPDATE students SET name=?, course=?, year_level=? WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, txtName.getText().trim());
            pst.setString(2, txtCourse.getText().trim());
            pst.setString(3, cbYear.getValue().toString());
            pst.setInt(4, selectedId);
            pst.executeUpdate();

            showStatus("Student updated successfully.", false);
            loadData();
            clearFields();

        } catch (Exception e) {
            showStatus("Error updating student: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteStudent() {
        if (selectedId == -1) {
            showStatus("Please select a student from the table first.", true);
            return;
        }

        // Confirmation dialog
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this student?",
                ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                try {
                    String query = "DELETE FROM students WHERE id=?";
                    PreparedStatement pst = conn.prepareStatement(query);
                    pst.setInt(1, selectedId);
                    pst.executeUpdate();

                    showStatus("Student deleted successfully.", false);
                    loadData();
                    clearFields();

                } catch (Exception e) {
                    showStatus("Error deleting student: " + e.getMessage(), true);
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void clearFields() {
        txtName.clear();
        txtCourse.clear();
        cbYear.setValue(null);
        selectedId = -1;
        table.getSelectionModel().clearSelection();
        showStatus("Fields cleared.", false);
    }

    // ── Helpers ──────────────────────────────────────────────────

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

    private void showStatus(String message, boolean isError) {
        lblStatus.setText(message);
        lblStatus.setStyle(isError
                ? "-fx-text-fill: #e74c3c;"
                : "-fx-text-fill: #27ae60;");
    }
}
