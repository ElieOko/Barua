CREATE TABLE IF NOT EXISTS demande_procuration_status (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    label VARCHAR(100) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

INSERT INTO demande_procuration_status (code, label) VALUES
    ('soumises', 'Soumises'),
    ('en_cours', 'En cours'),
    ('approuvees', 'Approuvées'),
    ('rejetees', 'Rejetées')
ON CONFLICT (code) DO NOTHING;

ALTER TABLE demande_procuration
    ADD COLUMN IF NOT EXISTS status_id BIGINT;

UPDATE demande_procuration
SET status_id = (
    SELECT id FROM demande_procuration_status WHERE code = 'soumises' LIMIT 1
)
WHERE status_id IS NULL;

UPDATE demande_procuration dp
SET status_id = dps.id
FROM demande_procuration_status dps
WHERE dp.status_id IS NULL
  AND dp.status IS NOT NULL
  AND dps.code = CASE dp.status
      WHEN 'EN_ATTENTE' THEN 'soumises'
      WHEN 'EN_COURS' THEN 'en_cours'
      WHEN 'TERMINE' THEN 'approuvees'
      WHEN 'REJETE' THEN 'rejetees'
      WHEN 'ANNULE' THEN 'rejetees'
      ELSE 'soumises'
  END;

UPDATE demande_procuration
SET status_id = (
    SELECT id FROM demande_procuration_status WHERE code = 'soumises' LIMIT 1
)
WHERE status_id IS NULL;

ALTER TABLE demande_procuration DROP COLUMN IF EXISTS status;

ALTER TABLE demande_procuration
    ALTER COLUMN status_id SET NOT NULL;

ALTER TABLE demande_procuration
    ADD CONSTRAINT fk_demande_procuration_status
        FOREIGN KEY (status_id)
        REFERENCES demande_procuration_status (id);

CREATE TABLE IF NOT EXISTS demande_procuration_status_history (
    id BIGSERIAL PRIMARY KEY,
    demande_procuration_id BIGINT NOT NULL,
    status_id BIGINT NOT NULL,
    previous_status_id BIGINT,
    note TEXT,
    changed_by_user_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_demande_procuration_status_history_demande
        FOREIGN KEY (demande_procuration_id)
        REFERENCES demande_procuration (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_demande_procuration_status_history_status
        FOREIGN KEY (status_id)
        REFERENCES demande_procuration_status (id),
    CONSTRAINT fk_demande_procuration_status_history_previous_status
        FOREIGN KEY (previous_status_id)
        REFERENCES demande_procuration_status (id)
);

CREATE INDEX IF NOT EXISTS idx_demande_procuration_status_history_demande_id
    ON demande_procuration_status_history (demande_procuration_id);

CREATE INDEX IF NOT EXISTS idx_demande_procuration_status_history_created_at
    ON demande_procuration_status_history (created_at DESC);
