ALTER TABLE T_STORE DROP COLUMN is_deleted;
ALTER TABLE T_STORE DROP COLUMN deleted;
ALTER TABLE T_STORE ADD COLUMN status INT DEFAULT 0;
ALTER TABLE T_STORE ADD COLUMN suspended DATE;
