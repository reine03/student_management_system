-- ============================================================
--  Student Management System - Database Setup Script
--  Run this in pgAdmin or psql BEFORE launching the app
-- ============================================================

-- 1. Create the database (run this line separately if needed)
CREATE DATABASE studentdb;

-- 2. Connect to studentdb, then run the rest:

-- 3. Create the students table
CREATE TABLE IF NOT EXISTS students (
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    course     VARCHAR(50)  NOT NULL,
    year_level VARCHAR(20)  NOT NULL
);

-- 4. (Optional) Insert sample data to test the app
INSERT INTO students (name, course, year_level) VALUES
    ('Juan dela Cruz',   'BSIT',   '1st Year'),
    ('Maria Santos',     'BSCS',   '2nd Year'),
    ('Pedro Reyes',      'BSECE',  '3rd Year'),
    ('Ana Gonzales',     'BSEE',   '4th Year');
