INSERT INTO users
(
    email,
    password_hash,
    first_name,
    last_name,
    role
)
VALUES
    (
        'admin@cloudexpense.com',
        '$2a$10$Yvv13C.jBm6x1dPTc3uQGugcoFdIEyRRl6ew6X5Yaj9I3oLyZAibS',
        'System',
        'Admin',
        'ADMIN'
    );