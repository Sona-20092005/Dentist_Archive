create table rent_overtime
(
    id                             uuid primary key,
    created_at                     timestamptz not null,
    created_by                     uuid,
    updated_at                     timestamptz,
    updated_by                     uuid,
    archived                       boolean     not null,
    archived_at                    timestamptz,
    archived_by                    uuid,

    work_schedule_modification_id uuid        not null,
    hourly_rent                    numeric(19, 2) not null,
    locked                         boolean     not null,
    notes                          text,

    constraint fk_rent_overtime_work_schedule_modification
        foreign key (work_schedule_modification_id)
            references work_schedule_modification (id),

    constraint uq_rent_overtime_work_schedule_modification
        unique (work_schedule_modification_id)
);