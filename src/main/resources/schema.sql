CREATE TABLE IF NOT EXISTS users (
                       id UUID PRIMARY KEY,
                       username VARCHAR(100) NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       age INTEGER CHECK(age > 0),
                       gender VARCHAR(50), -- e.g., 'Male', 'Female', 'Non-binary', 'Other'
                       height DECIMAL(5, 2) CHECK(height > 0), -- Height in meters
                       weight DECIMAL(5, 2) CHECK(weight > 0), -- Weight in kilograms
                       activity_level VARCHAR(50), -- e.g., 'Sedentary', 'Moderately Active', 'Very Active'
                       nationality VARCHAR(100),
                       diet_type VARCHAR(100), -- e.g., 'Vegetarian', 'Vegan', 'Meat-based', 'Balanced'
                       gastritis_duration INTERVAL, -- Time since the user has had gastritis
                       created_at TIMESTAMP DEFAULT NOW(),
                       updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS food_symptom_logs (
                                   id UUID PRIMARY KEY,
                                   user_id UUID REFERENCES users(id) ON DELETE CASCADE,
                                   food_name VARCHAR(255) NOT NULL,
                                   symptom VARCHAR(255),
                                   severity INTEGER CHECK(severity BETWEEN 1 AND 10),
                                   notes TEXT,
                                   created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS daily_diet_logs (
    id UUID PRIMARY KEY,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    date DATE NOT NULL,
    meals TEXT NOT NULL,
    type_meal VARCHAR(255),
    notes TEXT,
    created_at TIMESTAMP DEFAULT NOW()
    );
