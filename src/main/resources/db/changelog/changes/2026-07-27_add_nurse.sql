create table nurse
(
    id                     uuid primary key,
    created_at             timestamptz not null,
    created_by             uuid,
    updated_at             timestamptz,
    updated_by             uuid,
    archived               boolean     not null,
    archived_at            timestamptz,
    archived_by            uuid,

    name                   text        not null,
    base_salary            numeric(19, 2),
    hourly_rate            numeric(19, 2),
    overtime_hourly_rate   numeric(19, 2),
    contracted_weekly_hours integer,
    compensation_type      varchar(16) not null,
    hire_date              date,
    termination_date       date,
    payroll_start_date     date,
    phones                 jsonb,
    emails                 jsonb,
    notes                  text,
    clinic_id              uuid        not null,

    constraint fk_nurse_clinic
        foreign key (clinic_id)
            references clinic (id)
);