alter table technician
    add column collaboration_start_date     date,
    add column collaboration_end_date       date,
    add column monthly_report_start_date    date not null;

alter table technician_work_log
    add column is_paid                      boolean not null;


create table technician_monthly_report
(
    id                  uuid primary key,
    created_at          timestamptz not null,
    created_by          uuid,
    updated_at          timestamptz,
    updated_by          uuid,

    technician_id       uuid not null,
    status              varchar(255) not null,
    year_month          date not null,

    total_amount        numeric(19,2) not null default 0,
    paid_amount         numeric(19,2) not null default 0,
    bonus               numeric(19,2) not null default 0,
    deduction           numeric(19,2) not null default 0,
    number_of_work_logs integer not null default 0,

    locked              boolean not null,
    notes               text,

    constraint fk_technician_monthly_report_technician
        foreign key (technician_id)
            references technician(id),

    constraint uk_technician_monthly_report_technician_year_month
        unique (technician_id, year_month)
);

