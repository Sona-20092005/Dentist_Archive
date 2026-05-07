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

-- nurse

ALTER TABLE nurse ADD COLUMN archive_status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE';

UPDATE nurse
SET archive_status = CASE
                         WHEN archived = TRUE THEN 'ARCHIVED'
                         ELSE 'ACTIVE'
    END;

ALTER TABLE nurse ALTER COLUMN archive_status DROP DEFAULT;

ALTER TABLE nurse DROP COLUMN archived;

-- material

ALTER TABLE material ADD COLUMN archive_status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE';

UPDATE material
SET archive_status = CASE
                         WHEN archived = TRUE THEN 'ARCHIVED'
                         ELSE 'ACTIVE'
    END;

ALTER TABLE material ALTER COLUMN archive_status DROP DEFAULT;

ALTER TABLE material DROP COLUMN archived;

-- procedure

ALTER TABLE procedure ADD COLUMN archive_status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE';

UPDATE procedure
SET archive_status = CASE
                         WHEN archived = TRUE THEN 'ARCHIVED'
                         ELSE 'ACTIVE'
    END;

ALTER TABLE procedure ALTER COLUMN archive_status DROP DEFAULT;

ALTER TABLE procedure DROP COLUMN archived;

-- technician

ALTER TABLE technician ADD COLUMN archive_status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE';

UPDATE technician
SET archive_status = CASE
                         WHEN archived = TRUE THEN 'ARCHIVED'
                         ELSE 'ACTIVE'
    END;

ALTER TABLE technician ALTER COLUMN archive_status DROP DEFAULT;

ALTER TABLE technician DROP COLUMN archived;