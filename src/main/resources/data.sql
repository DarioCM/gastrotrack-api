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
             '123e4567-e89b-12d3-a456-426614174000', -- Example UUID
             'testuser',
             'testuser@example.com',
             'password123',
             30,
             'Male',
             1.75,
             70.5,
             'Moderately Active',
             'Mexican',
             'Balanced',
             'One year and a half', -- Free-form text for gastritis duration
             NOW(),
             NOW()
         );
