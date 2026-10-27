alter table work_schedule_modification
    add column archive_reason varchar(50);

alter table work_schedule_rule
    add column archive_reason varchar(50);