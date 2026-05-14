-- Filiais
INSERT INTO branches (id, name) VALUES
    ('a1b2c3d4-0001-0001-0001-000000000001', 'Filial Curitiba'),
    ('a1b2c3d4-0001-0001-0001-000000000002', 'Filial Londrina');

-- Tipos de grão
INSERT INTO grain_types (id, name, purchase_price_per_ton) VALUES
    ('b1b2c3d4-0002-0002-0002-000000000001', 'Soja', 1500.00),
    ('b1b2c3d4-0002-0002-0002-000000000002', 'Milho', 900.00),
    ('b1b2c3d4-0002-0002-0002-000000000003', 'Trigo', 1200.00);

-- Caminhões (tara em kg)
INSERT INTO trucks (id, plate, tare, branch_id) VALUES
    ('c1b2c3d4-0003-0003-0003-000000000001', 'ABC1D23', 8500.0, 'a1b2c3d4-0001-0001-0001-000000000001'),
    ('c1b2c3d4-0003-0003-0003-000000000002', 'XYZ9F87', 9200.0, 'a1b2c3d4-0001-0001-0001-000000000002');

-- Balanças
INSERT INTO scales (id, code, branch_id, api_key) VALUES
    ('d1b2c3d4-0004-0004-0004-000000000001', 'SCALE-001', 'a1b2c3d4-0001-0001-0001-000000000001', 'sk-balanca-001-abc123'),
    ('d1b2c3d4-0004-0004-0004-000000000002', 'SCALE-002', 'a1b2c3d4-0001-0001-0001-000000000002', 'sk-balanca-002-def456');

-- Transação de transporte ativa (caminhão ABC1D23 buscando Soja, já em trânsito)
INSERT INTO transport_transactions (id, truck_id, grain_type_id, status, started_at) VALUES
    ('e1b2c3d4-0005-0005-0005-000000000001', 'c1b2c3d4-0003-0003-0003-000000000001', 'b1b2c3d4-0002-0002-0002-000000000001', 'IN_TRANSIT', NOW());
