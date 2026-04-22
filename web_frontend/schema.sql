-- Script para Neon (PostgreSQL)

CREATE TABLE IF NOT EXISTS plano_voo (
    id SERIAL PRIMARY KEY,
    identificacao_aeronave VARCHAR(50),
    regras VARCHAR(50),
    tipo_voo VARCHAR(10),
    numero_aeronave VARCHAR(10),
    tipo_aeronave VARCHAR(50),
    catet VARCHAR(10),
    equipamento VARCHAR(50),
    equipamento_vigilancia VARCHAR(50),
    aerodromo_pt VARCHAR(10),
    hora_eob VARCHAR(5),
    velocidade VARCHAR(20),
    nivel_voo VARCHAR(20),
    rota TEXT,
    aerodromo_destino VARCHAR(10),
    duracao_total_voo VARCHAR(10),
    aerodromo_alternativa VARCHAR(10),
    observacoes TEXT,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);