alter table appointment
    add column appointment_status_sort_order int not null;

alter table appointment
    add column appointment_scheduling_status_sort_order int not null;

alter table work_schedule_rule
    add column day_of_week_sort_order int not null;

alter table work_schedule_modification
    add column modification_type_sort_order int not null;

alter table work_schedule_modification
    add column work_session_type_sort_order int not null;