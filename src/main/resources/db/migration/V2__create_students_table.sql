CREATE TABLE students (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          registration_number VARCHAR(100) NOT NULL UNIQUE,
                          phone_number VARCHAR(30) NOT NULL,
                          birth_date DATE NOT NULL
);