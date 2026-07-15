ALTER TABLE demande_procuration
    ADD COLUMN IF NOT EXISTS "fullName" VARCHAR(255),
    ADD COLUMN IF NOT EXISTS "numberIdentity" VARCHAR(255),
    ADD COLUMN IF NOT EXISTS phone VARCHAR(50);

ALTER TABLE demande_procuration DROP COLUMN IF EXISTS piece;

CREATE TABLE IF NOT EXISTS demande_procuration_pieces (
    id BIGSERIAL PRIMARY KEY,
    demande_procuration_id BIGINT NOT NULL,
    name VARCHAR(500) NOT NULL,
    path TEXT NOT NULL,
    content_type VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_demande_procuration_pieces_demande
        FOREIGN KEY (demande_procuration_id)
        REFERENCES demande_procuration (id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_demande_procuration_pieces_demande_id
    ON demande_procuration_pieces (demande_procuration_id);
