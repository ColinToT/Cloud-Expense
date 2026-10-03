-- Manager
INSERT INTO users (email,
                   password_hash,
                   first_name,
                   last_name,
                   role)
VALUES ('manager@test.com',
        '$2a$10$7zy2PhwlS21aIsheB2xKVOoLU/un.900Liv0VgZ8uw7wreNNZK1.6',
        'Tom',
        'Manager',
        'MANAGER');

-- Finance
INSERT INTO users (email,
                   password_hash,
                   first_name,
                   last_name,
                   role)
VALUES ('finance@test.com',
        '$2a$10$7zy2PhwlS21aIsheB2xKVOoLU/un.900Liv0VgZ8uw7wreNNZK1.6',
        'Alice',
        'Finance',
        'FINANCE');

-- Employee
INSERT INTO users (email,
                   password_hash,
                   first_name,
                   last_name,
                   role,
                   manager_id)
VALUES ('employee@test.com',
        '$2a$10$7zy2PhwlS21aIsheB2xKVOoLU/un.900Liv0VgZ8uw7wreNNZK1.6',
        'John',
        'Employee',
        'EMPLOYEE',
        (SELECT id
         FROM users
         WHERE email = 'manager@test.com'));