# Student Management System
JavaFX + JDBC + PostgreSQL CRUD Application

---

## Prerequisites
- Java 17 or later
- Maven (IntelliJ bundles it — no separate install needed)
- PostgreSQL installed and running

---

## Setup Steps

### Step 1 — Set up the database
1. Open **pgAdmin** or **psql**
2. Run the contents of `database_setup.sql`
   - This creates `studentdb` and the `students` table
   - Optionally inserts sample data

### Step 2 — Configure the password
Open `src/main/java/com/student/DBConnection.java` and update:
```java
private static final String PASSWORD = "your_password";  // ← change this
```
Also change `USER` if your PostgreSQL username is not `postgres`.

### Step 3 — Open in IntelliJ IDEA
1. **File → Open** → select the `StudentManagementSystem` folder
2. IntelliJ will detect the `pom.xml` automatically
3. Wait for Maven to download dependencies (bottom progress bar)

### Step 4 — Run the app
- Open `src/main/java/com/student/MainApp.java`
- Click the green ▶ Run button next to `public static void main`

---

## Project Structure
```
StudentManagementSystem/
├── pom.xml                          ← Maven config (dependencies)
├── database_setup.sql               ← Run this in PostgreSQL first
└── src/main/
    ├── java/com/student/
    │   ├── MainApp.java             ← Entry point
    │   ├── Controller.java          ← All CRUD logic
    │   ├── Student.java             ← Model (TableView binding)
    │   ├── YearLevel.java           ← Enum for ChoiceBox
    │   └── DBConnection.java        ← DB credentials ← EDIT THIS
    └── resources/com/student/
        ├── main.fxml                ← UI layout
        └── style.css                ← Styling
```

---

## Features
- ➕ Add new student records
- ✏️ Update selected record (click a row first)
- 🗑️ Delete with confirmation dialog
- ✖ Clear input fields
- ✅ Input validation (no empty fields)
- 📋 Status messages for all actions
- 🎨 Clean, styled UI
