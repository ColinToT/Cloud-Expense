-- Add notification type enum
CREATE TYPE notification_type AS ENUM (
    'EXPENSE_SUBMITTED',
    'EXPENSE_APPROVED',
    'EXPENSE_REJECTED',
    'PAYMENT_COMPLETED'
    );

-- Add notification type column
ALTER TABLE notifications ADD COLUMN type notification_type;

-- Add reference id for related business entity
ALTER TABLE notifications ADD COLUMN reference_id BIGINT;

COMMENT ON COLUMN notifications.type IS 'Notification business type';
COMMENT ON COLUMN notifications.reference_id IS 'Related business entity id, such as expense id';

-- Make type column required
ALTER TABLE notifications ALTER COLUMN type SET NOT NULL;