create table nurse_work_log
(
    id                    uuid primary key,
    created_at            timestamptz not null,
    created_by            uuid,
    updated_at            timestamptz,
    updated_by            uuid,
    archived              boolean     not null,
    archived_at           timestamptz,
    archived_by           uuid,

    nurse_id              uuid        not null,
    work_type             varchar(16) not null,
    date                  date        not null,
    start_time            time        not null,
    end_time              time        not null,
    hourly_rate           numeric(19, 2),

    notes                 text,

    constraint fk_nurse_work_log_nurse
        foreign key (nurse_id)
            references nurse (id)
);