-- users

ALTER TABLE users ADD COLUMN archive_status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE';

UPDATE users
SET archive_status = CASE
                         WHEN archived = TRUE THEN 'ARCHIVED'
                         ELSE 'ACTIVE'
    END;

ALTER TABLE users ALTER COLUMN archive_status DROP DEFAULT;

ALTER TABLE users DROP COLUMN archived;

-- patient

ALTER TABLE patient ADD COLUMN archive_status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE';

UPDATE patient
SET archive_status = CASE
                         WHEN archived = TRUE THEN 'ARCHIVED'
                         ELSE 'ACTIVE'
    END;

ALTER TABLE patient ALTER COLUMN archive_status DROP DEFAULT;

ALTER TABLE patient DROP COLUMN archived;
