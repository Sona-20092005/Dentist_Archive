alter table appointment
drop constraint fk_appointment_doctor;

alter table appointment
drop column doctor_id;

alter table appointment
    add column schedule_id uuid not null;

alter table appointment
    add column appointment_scheduling_status varchar(32) not null;

alter table appointment
    add constraint fk_appointment_schedule
        foreign key (schedule_id)
            references work_schedule (id);