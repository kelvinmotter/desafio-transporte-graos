CREATE TABLE grain_types (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL UNIQUE,
    purchase_price_per_ton NUMERIC(12,2) NOT NULL
);
