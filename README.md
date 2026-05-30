# Student Management System

JavaFX + JDBC + PostgreSQL CRUD application.

## Prerequisites

- Java 17 or later
- Maven, or IntelliJ IDEA with Maven support
- PostgreSQL installed and running

## Setup Steps

### Step 1 - Set up the database

1. Open pgAdmin or psql.
2. Run the contents of `database_setup.sql`.
3. This creates `studentdb`, creates the `students` table, and inserts optional sample data.

### Step 2 - Configure the database connection

By default, the app connects to:

```text
jdbc:postgresql://localhost:5432/studentdb
user: postgres
password: empty
```

You can override those values without editing the source code:

```powershell
$env:DB_URL = "jdbc:postgresql://localhost:5432/studentdb"
$env:DB_USER = "postgres"
$env:DB_PASSWORD = "your_postgres_password"
```

You can also pass JVM system properties named `db.url`, `db.user`, and `db.password`.

### Step 3 - Open in IntelliJ IDEA

1. Select File > Open.
2. Open the `StudentManagementSystem` folder that contains `pom.xml`.
3. Wait for IntelliJ to import the Maven project and download dependencies.

### Step 4 - Run the app

Open `src/main/java/com/student/MainApp.java`, then run the `main` method.

## Project Structure

```text
StudentManagementSystem/
├── pom.xml
├── database_setup.sql
└── src/main/
    ├── java/com/student/
    │   ├── MainApp.java
    │   ├── Controller.java
    │   ├── Student.java
    │   ├── YearLevel.java
    │   └── DBConnection.java
    └── resources/com/student/
        ├── main.fxml
        └── style.css
```

## Features

- Add new student records
- Update a selected record
- Delete with confirmation dialog
- Clear input fields
- Validate required fields
- Show status messages for user actions
- Styled JavaFX interface
