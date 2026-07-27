CREATE TYPE payment_method AS ENUM (
    'BANK_TRANSFER',
    'CASH',
    'OTHER'
    );


CREATE TABLE payment_records
(

    id                    BIGSERIAL PRIMARY KEY,

    expense_id            BIGINT         NOT NULL,

    paid_by               BIGINT         NOT NULL,

    amount                NUMERIC(12, 2) NOT NULL,

    payment_method        payment_method NOT NULL,

    transaction_reference VARCHAR(100),

    paid_at               TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    created_at            TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,


    CONSTRAINT fk_payment_expense
        FOREIGN KEY (expense_id)
            REFERENCES expenses (id),

    CONSTRAINT fk_payment_user
        FOREIGN KEY (paid_by)
            REFERENCES users (id)

);