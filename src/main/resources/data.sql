INSERT INTO users (
    id,
    username,
    email,
    stored_hash,
    stored_salt,
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
             '123e4567-e89b-12d3-a456-426614174000',
             'testuser',
             'testuser@example.com',
             decode('a12b3c4d5e6f7890abcd1234567890ef', 'hex'), -- Valid hash
             decode('c3d4e5f607a8b9c123456789abcdef01', 'hex'), -- Valid salt
             30,
             'Male',
             1.75,
             70.5,
             'Moderately Active',
             'Mexican',
             'Balanced',
             'One year and a half',
             NOW(),
             NOW()
         );
