ALTER TABLE account_users
    ADD COLUMN IF NOT EXISTS organism_id BIGINT;

ALTER TABLE account_users
    DROP CONSTRAINT IF EXISTS fk_account_users_organism;

ALTER TABLE account_users
    ADD CONSTRAINT fk_account_users_organism
        FOREIGN KEY (organism_id)
        REFERENCES organisms (id)
        ON DELETE SET NULL;

DROP INDEX IF EXISTS uq_account_users_account_user;

CREATE UNIQUE INDEX IF NOT EXISTS uq_account_users_user_account_no_organism
    ON account_users (user_id, account_id)
    WHERE organism_id IS NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uq_account_users_user_account_organism
    ON account_users (user_id, account_id, organism_id)
    WHERE organism_id IS NOT NULL;
