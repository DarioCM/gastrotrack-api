INSERT INTO users (
    id,
    name,
    email,
    stored_hash,
    stored_salt,
    age,
    gender,
    height,
    weight,
    gastritis_duration,
    created_at,
    updated_at
) VALUES (
             gen_random_uuid(), -- Generate a new random UUID
             'John Doe',
             'johndoe@example.com',
             decode('5f4dcc3b5aa765d61d8327deb882cf99', 'hex'),
             decode('1234567890abcdef1234567890abcdef', 'hex'),
             30,
             'Male',
             1.75,
             70.5,
             'One year and a half',
             NOW(),
             NOW()
         );
