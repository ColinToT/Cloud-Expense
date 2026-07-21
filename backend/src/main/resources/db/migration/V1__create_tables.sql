-- ============================================================
-- 1. Enumerated types
-- ============================================================
CREATE TYPE user_role AS ENUM ('EMPLOYEE', 'MANAGER', 'FINANCE');
COMMENT ON TYPE user_role IS 'User roles: employee, manager, finance';

CREATE TYPE expense_status AS ENUM ('DRAFT', 'SUBMITTED', 'MANAGER_APPROVED', 'FINANCE_APPROVED', 'REJECTED', 'PAID');
COMMENT ON TYPE expense_status IS 'Expense lifecycle status';

CREATE TYPE approval_stage AS ENUM ('MANAGER', 'FINANCE');
COMMENT ON TYPE approval_stage IS 'Approval stage: manager review or finance review';

CREATE TYPE approval_action AS ENUM ('APPROVE', 'REJECT');
COMMENT ON TYPE approval_action IS 'Approval decision: approve or reject';

-- ============================================================
-- 2. Users table
-- ============================================================
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       first_name VARCHAR(100) NOT NULL,
                       last_name VARCHAR(100) NOT NULL,
                       role user_role NOT NULL DEFAULT 'EMPLOYEE',
                       manager_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
                       created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE users IS 'System users – employees, managers and finance staff';
COMMENT ON COLUMN users.id IS 'Primary key, auto-incrementing';
COMMENT ON COLUMN users.email IS 'Unique login email';
COMMENT ON COLUMN users.password_hash IS 'BCrypt hashed password';
COMMENT ON COLUMN users.first_name IS 'Given name';
COMMENT ON COLUMN users.last_name IS 'Surname';
COMMENT ON COLUMN users.role IS 'User role – determines permissions';
COMMENT ON COLUMN users.manager_id IS 'Direct manager ID for employees; NULL for managers and finance users';
COMMENT ON COLUMN users.created_at IS 'Record creation timestamp';
COMMENT ON COLUMN users.updated_at IS 'Last update timestamp (auto-updated)';

-- ============================================================
-- 3. Expense categories
-- ============================================================
CREATE TABLE expense_categories (
                                    id BIGSERIAL PRIMARY KEY,
                                    name VARCHAR(50) NOT NULL UNIQUE,
                                    description TEXT
);

COMMENT ON TABLE expense_categories IS 'Predefined expense categories (lookup)';
COMMENT ON COLUMN expense_categories.id IS 'Primary key';
COMMENT ON COLUMN expense_categories.name IS 'Category name (e.g. MEAL, TRAVEL)';
COMMENT ON COLUMN expense_categories.description IS 'Human-readable description';

-- ============================================================
-- 4. Expenses (main reimbursement requests)
-- ============================================================
CREATE TABLE expenses (
                          id BIGSERIAL PRIMARY KEY,
                          user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                          title VARCHAR(255) NOT NULL,
                          description TEXT,
                          amount DECIMAL(10,2) NOT NULL CHECK (amount >= 0),
                          currency VARCHAR(3) DEFAULT 'CNY',
                          category_id BIGINT NOT NULL REFERENCES expense_categories(id),
                          expense_date DATE NOT NULL,
                          status expense_status NOT NULL DEFAULT 'DRAFT',
                          submitted_at TIMESTAMP WITH TIME ZONE,
                          approved_at TIMESTAMP WITH TIME ZONE,
                          created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                          deleted_at TIMESTAMP WITH TIME ZONE
);

COMMENT ON TABLE expenses IS 'Main expense reimbursement requests';
COMMENT ON COLUMN expenses.id IS 'Primary key';
COMMENT ON COLUMN expenses.user_id IS 'Employee who submitted the expense';
COMMENT ON COLUMN expenses.title IS 'Short summary of the expense';
COMMENT ON COLUMN expenses.description IS 'Detailed description (optional)';
COMMENT ON COLUMN expenses.amount IS 'Total amount in the specified currency (>=0)';
COMMENT ON COLUMN expenses.currency IS 'ISO currency code (default CNY)';
COMMENT ON COLUMN expenses.category_id IS 'Foreign key to expense_categories';
COMMENT ON COLUMN expenses.expense_date IS 'Date the expense was incurred';
COMMENT ON COLUMN expenses.status IS 'Current workflow status';
COMMENT ON COLUMN expenses.submitted_at IS 'Timestamp when status changed to SUBMITTED';
COMMENT ON COLUMN expenses.approved_at IS 'Timestamp when status became FINANCE_APPROVED (final approval)';
COMMENT ON COLUMN expenses.created_at IS 'Creation timestamp';
COMMENT ON COLUMN expenses.updated_at IS 'Last update timestamp (auto-updated)';
COMMENT ON COLUMN expenses.deleted_at IS 'Soft-delete marker; non‑NULL means deleted';

-- Indexes for performance
CREATE INDEX idx_expenses_user_id ON expenses(user_id);
CREATE INDEX idx_expenses_status ON expenses(status);
CREATE INDEX idx_expenses_expense_date ON expenses(expense_date);
CREATE INDEX idx_expenses_deleted_at ON expenses(deleted_at) WHERE deleted_at IS NULL;

-- ============================================================
-- 5. Receipt attachments
-- ============================================================
CREATE TABLE receipts (
                          id BIGSERIAL PRIMARY KEY,
                          expense_id BIGINT NOT NULL REFERENCES expenses(id) ON DELETE CASCADE,
                          file_name VARCHAR(255) NOT NULL,
                          file_url TEXT NOT NULL,
                          uploaded_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE receipts IS 'Receipt files attached to expenses';
COMMENT ON COLUMN receipts.id IS 'Primary key';
COMMENT ON COLUMN receipts.expense_id IS 'Associated expense';
COMMENT ON COLUMN receipts.file_name IS 'Original file name';
COMMENT ON COLUMN receipts.file_url IS 'S3 storage URL (or equivalent)';
COMMENT ON COLUMN receipts.uploaded_at IS 'Upload timestamp';

-- ============================================================
-- 6. Approval history
-- ============================================================
CREATE TABLE approval_records (
                                  id BIGSERIAL PRIMARY KEY,
                                  expense_id BIGINT NOT NULL REFERENCES expenses(id) ON DELETE CASCADE,
                                  approver_id BIGINT NOT NULL REFERENCES users(id),
                                  approval_stage approval_stage NOT NULL,
                                  action approval_action NOT NULL,
                                  comment TEXT,
                                  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE approval_records IS 'Audit trail of all approval actions';
COMMENT ON COLUMN approval_records.id IS 'Primary key';
COMMENT ON COLUMN approval_records.expense_id IS 'Expense being approved';
COMMENT ON COLUMN approval_records.approver_id IS 'User who performed the approval';
COMMENT ON COLUMN approval_records.approval_stage IS 'Which stage (manager or finance)';
COMMENT ON COLUMN approval_records.action IS 'Approve or reject decision';
COMMENT ON COLUMN approval_records.comment IS 'Optional remarks from approver';
COMMENT ON COLUMN approval_records.created_at IS 'Timestamp of the approval action';

CREATE INDEX idx_approval_records_expense_id ON approval_records(expense_id);
CREATE INDEX idx_approval_records_expense_stage ON approval_records(expense_id, approval_stage);

-- ============================================================
-- 7. User notifications
-- ============================================================
CREATE TABLE notifications (
                               id BIGSERIAL PRIMARY KEY,
                               user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                               title VARCHAR(255) NOT NULL,
                               message TEXT NOT NULL,
                               read_status BOOLEAN DEFAULT FALSE,
                               created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE notifications IS 'In-app notifications for users';
COMMENT ON COLUMN notifications.id IS 'Primary key';
COMMENT ON COLUMN notifications.user_id IS 'Target user';
COMMENT ON COLUMN notifications.title IS 'Notification subject';
COMMENT ON COLUMN notifications.message IS 'Notification body text';
COMMENT ON COLUMN notifications.read_status IS 'Whether the user has read the notification';
COMMENT ON COLUMN notifications.created_at IS 'Notification creation timestamp';

CREATE INDEX idx_notifications_user_id_read ON notifications(user_id, read_status);

-- ============================================================
-- 8. Automatic updated_at trigger
-- ============================================================
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

COMMENT ON FUNCTION update_updated_at_column() IS 'Trigger function to auto‑update updated_at on row change';

-- Apply trigger to tables that have updated_at
CREATE TRIGGER trigger_users_updated_at BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trigger_expenses_updated_at BEFORE UPDATE ON expenses
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();