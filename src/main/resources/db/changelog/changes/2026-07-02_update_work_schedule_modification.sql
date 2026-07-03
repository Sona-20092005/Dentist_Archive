alter table work_schedule_modification
    drop constraint fk_work_schedule_modification_doctor;

alter table work_schedule_modification
    drop column doctor_id;

alter table work_schedule_modification
    add column schedule_id uuid not null;

alter table work_schedule_modification
    add constraint fk_work_schedule_modification_schedule
        foreign key (schedule_id)
            references work_schedule (id);

alter table work_schedule_modification
    alter column date drop not null;

alter table work_schedule_modification
    alter column start_time drop not null;

alter table work_schedule_modification
    alter column end_time drop not null;