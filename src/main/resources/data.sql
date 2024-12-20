-- Insert a new test user
INSERT INTO users (
    id,
    username,
    email,
    password,
    age,
    gender,
    height,
    weight,
    activity_level,
    nationality,
    diet_type,
    gastritis_duration,
    created_at,
    updated_at
) VALUES (
             '123e4567-e89b-12d3-a456-426614174000', -- Replace with a valid UUID
             'test_user',
             'test_user@example.com',
             'hashed_password', -- Replace with an appropriately hashed password
             30, -- Age
             'Male',
             1.75, -- Height in meters
             70.5, -- Weight in kilograms
             'Moderately Active',
             'Mexico',
             'Balanced',
             INTERVAL '2 years', -- Duration of gastritis
             NOW(),
             NOW()
         )
ON CONFLICT (email) DO NOTHING; -- Avoid duplication based on email
