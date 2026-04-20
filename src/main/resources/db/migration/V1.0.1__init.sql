SET search_path TO public;

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    password VARCHAR(500),
    email VARCHAR(255),
    username VARCHAR(255),
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    full_name VARCHAR(500) NOT NULL,
    from_service VARCHAR(255),
    is_premium BOOLEAN NOT NULL DEFAULT FALSE,
    is_certified BOOLEAN NOT NULL DEFAULT FALSE,
    is_lock BOOLEAN NOT NULL DEFAULT FALSE,
    is_valid BOOLEAN NOT NULL DEFAULT FALSE,
    phone VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_users_email ON users (email);
CREATE INDEX IF NOT EXISTS idx_users_phone ON users (phone);

CREATE TABLE type_accounts (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE cities (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS accounts (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type_account_id BIGINT NOT NULL REFERENCES type_accounts (id)
);

CREATE INDEX IF NOT EXISTS idx_accounts_type_account_id ON accounts (type_account_id);

CREATE TABLE IF NOT EXISTS account_users (
    id BIGSERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL REFERENCES accounts (id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_account_users_account_user ON account_users (account_id, user_id);
CREATE INDEX IF NOT EXISTS idx_account_users_user_id ON account_users (user_id);
CREATE INDEX IF NOT EXISTS idx_account_users_account_id ON account_users (account_id);

CREATE TABLE IF NOT EXISTS refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    expires_at TIMESTAMP NOT NULL,
    hashed_token VARCHAR(500) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_refresh_tokens_user_id ON refresh_tokens (user_id);
CREATE INDEX IF NOT EXISTS idx_refresh_tokens_user_hashed ON refresh_tokens (user_id, hashed_token);

CREATE TABLE IF NOT EXISTS document_types (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS documents (
    id BIGSERIAL PRIMARY KEY,
    document_type_id BIGINT NOT NULL REFERENCES document_types (id),
    title VARCHAR(500) NOT NULL,
    code VARCHAR(100),
    description TEXT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE type_organism (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE organisms (
    id BIGSERIAL PRIMARY KEY,
    type_id BIGINT NOT NULL,
    city_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_type_organism
        FOREIGN KEY (type_id)
        REFERENCES type_organism(id)
        ON DELETE CASCADE
);

CREATE TABLE service_documentaries (
    id BIGSERIAL PRIMARY KEY,
    document_id BIGINT NOT NULL,
    organism_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    devise_id BIGINT NOT NULL,
    description TEXT NOT NULL,
    delay_day_open VARCHAR(50) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_organism
        FOREIGN KEY (organism_id)
        REFERENCES organisms(id)
        ON DELETE CASCADE
);

CREATE TABLE demande_procuration (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    service_documentaire_id BIGINT NOT NULL,
    status VARCHAR(50) DEFAULT 'EN_ATTENTE',
    commentaire TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_service_documentaire
        FOREIGN KEY (service_documentaire_id)
        REFERENCES service_documentaries(id)
        ON DELETE CASCADE
);