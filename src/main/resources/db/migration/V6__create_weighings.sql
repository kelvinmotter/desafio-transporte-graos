CREATE TABLE weighings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    scale_id UUID NOT NULL REFERENCES scales(id),
    transaction_id UUID NOT NULL REFERENCES transport_transactions(id),
    plate VARCHAR(10) NOT NULL,
    gross_weight DOUBLE PRECISION NOT NULL,
    tare DOUBLE PRECISION NOT NULL,
    net_weight DOUBLE PRECISION NOT NULL,
    grain_type VARCHAR(255) NOT NULL,
    cargo_cost NUMERIC(12,2) NOT NULL,
    weighed_at TIMESTAMP WITH TIME ZONE NOT NULL
);
