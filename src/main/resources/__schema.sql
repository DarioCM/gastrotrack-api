-- Drop the table if it already exists (optional, for testing)
-- DROP TABLE IF EXISTS users CASCADE;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
                                     id UUID PRIMARY KEY,
                                     name VARCHAR(50) NOT NULL, -- The name of the patient/user
    email VARCHAR(255) UNIQUE NOT NULL,
    stored_hash BYTEA NOT NULL, -- Store hash for password hashing
    stored_salt BYTEA NOT NULL, -- Store salt for password hashing
    age INTEGER, -- Optional age of the user
    gender VARCHAR(20), -- Gender of the user
    height DECIMAL(5, 2), -- Height in meters
    weight DECIMAL(5, 2), -- Weight in kilograms
    gastritis_duration VARCHAR(50), -- Duration of gastritis (e.g., "1 year")
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Auto-populate on creation
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Auto-update on modification
    );

-- Create food_symptom_logs table
CREATE TABLE IF NOT EXISTS food_symptom_logs (
                                                 id UUID PRIMARY KEY,
                                                 user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    food_name VARCHAR(255) NOT NULL,
    symptom VARCHAR(255),
    severity INTEGER CHECK (severity BETWEEN 1 AND 10), -- Ensure severity is between 1 and 10
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Auto-populate on creation
    );

-- Create daily_diet_logs table
CREATE TABLE IF NOT EXISTS daily_diet_logs (
                                               id UUID PRIMARY KEY,
                                               user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    date DATE NOT NULL,
    meals TEXT NOT NULL, -- Meals stored as plain text or JSON
    type_meal VARCHAR(255), -- e.g., "Breakfast", "Lunch"
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Auto-populate on creation
    );
