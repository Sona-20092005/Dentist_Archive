create table clinic
(
    id              uuid primary key,
    created_at      timestamptz not null,
    created_by      uuid,
    updated_at      timestamptz,
    updated_by      uuid,
    archived        boolean     not null,
    archived_at     timestamptz,
    archived_by     uuid,

    name            varchar(255) not null,
    phones          jsonb,
    emails          jsonb,
    address         varchar(255),
    notes           text,

    doctor_id       uuid not null,

    constraint fk_clinic_doctor foreign key (doctor_id)
        references users (id)
);

create table work_schedule
(
    id               uuid primary key,
    created_at       timestamptz not null,
    created_by       uuid,
    updated_at       timestamptz,
    updated_by       uuid,
    archived         boolean     not null,
    archived_at      timestamptz,
    archived_by      uuid,

    name             varchar(255),
    notes            text,

    effective_from   date not null,
    effective_until  date,

    doctor_id        uuid not null,
    clinic_id        uuid not null,

    constraint fk_work_schedule_doctor foreign key (doctor_id)
        references users (id),

    constraint fk_work_schedule_clinic foreign key (clinic_id)
        references clinic (id)
);

create table work_schedule_rule
(
    id              uuid primary key,
    created_at      timestamptz not null,
    created_by      uuid,
    updated_at      timestamptz,
    updated_by      uuid,
    archived        boolean     not null,
    archived_at     timestamptz,
    archived_by     uuid,

    day_of_week     varchar(16) not null,
    start_time      time not null,
    end_time        time not null,
    notes           text,

    schedule_id     uuid not null,

    constraint fk_work_schedule_rule_schedule foreign key (schedule_id)
        references work_schedule (id)
);

create table work_schedule_modification
(
    id                 uuid primary key,
    created_at         timestamptz not null,
    created_by         uuid,
    updated_at         timestamptz,
    updated_by         uuid,
    archived           boolean     not null,
    archived_at        timestamptz,
    archived_by        uuid,

    date               date not null,
    start_time         time not null,
    end_time           time not null,

    modification_type  varchar(32) not null,
    work_session_type  varchar(32) not null,

    source_date        date,
    source_start_time  time,
    source_end_time    time,

    doctor_id          uuid not null,

    constraint fk_work_schedule_modification_doctor foreign key (doctor_id)
        references users (id)
);

create table appointment
(
    id                   uuid primary key,
    created_at           timestamptz not null,
    created_by           uuid,
    updated_at           timestamptz,
    updated_by           uuid,
    archived             boolean     not null,
    archived_at          timestamptz,
    archived_by          uuid,

    date                 date not null,
    start_time           time not null,
    end_time             time not null,

    appointment_status   varchar(32) not null,

    notes                text,

    nurse_id             uuid,
    patient_id           uuid not null,
    doctor_id            uuid not null,

    constraint fk_appointment_patient foreign key (patient_id)
        references patient (id),

    constraint fk_appointment_doctor foreign key (doctor_id)
        references users (id)
);

alter table completed_treatment
    add constraint fk_completed_treatment_appointment
        foreign key (appointment_id)
            references appointment (id);