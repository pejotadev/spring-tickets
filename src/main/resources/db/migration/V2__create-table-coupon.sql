CREATE TABLE coupon (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    code VARCHAR(100) NOT NULL,
    discount DECIMAL(5, 2) NOT NULL,
    validUntil TIMESTAMP NOT NULL,
    event_id UUID,

    FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE
);