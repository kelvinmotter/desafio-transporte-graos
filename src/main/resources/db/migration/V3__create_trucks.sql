CREATE TABLE trucks (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    plate VARCHAR(10) NOT NULL UNIQUE,
    tare DOUBLE PRECISION NOT NULL,
    branch_id UUID REFERENCES branches(id)
);
