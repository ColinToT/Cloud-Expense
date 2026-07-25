-- Add ADMIN role for system administration
ALTER TYPE user_role ADD VALUE 'ADMIN';

COMMENT ON TYPE user_role IS
    'System roles: admin, employee, manager and finance';

COMMENT ON TABLE users IS
    'Application users with different authorization roles';