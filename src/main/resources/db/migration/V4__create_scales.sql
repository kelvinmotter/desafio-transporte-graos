CREATE TABLE scales (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code VARCHAR(50) NOT NULL UNIQUE,
    branch_id UUID NOT NULL REFERENCES branches(id),
    api_key VARCHAR(255) NOT NULL UNIQUE
);
