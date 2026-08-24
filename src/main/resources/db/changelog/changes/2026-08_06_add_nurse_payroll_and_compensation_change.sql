create table nurse_payroll
(
    id                           uuid primary key,
    created_at                   timestamptz not null,
    created_by                   uuid,
    updated_at                   timestamptz,
    updated_by                   uuid,

    nurse_id                     uuid        not null,
    status                       varchar(50) not null,
    year                         integer     not null,
    month                        integer     not null,
    calculated_regular_amount    numeric(19,2) not null default 0,
    regular_amount               numeric(19,2) not null default 0,
    calculated_overtime_amount   numeric(19,2) not null default 0,
    overtime_amount              numeric(19,2) not null default 0,
    bonus                        numeric(19,2) not null default 0,
    deduction                    numeric(19,2) not null default 0,
    regular_minutes_worked       integer     not null default 0,
    overtime_minutes_worked      integer     not null default 0,
    payment_date                 date,
    compensation_type            varchar(50) not null,
    locked                       boolean     not null,
    notes                        text,

    constraint fk_nurse_payroll_nurse
        foreign key (nurse_id)
            references nurse(id),

    constraint uq_nurse_payroll_month
        unique (nurse_id, year, month),

    constraint chk_nurse_payroll_month
        check (month between 1 and 12)
);

create table nurse_compensation_type_change
(
    id                 uuid primary key,
    created_at         timestamptz not null,
    created_by         uuid,
    updated_at         timestamptz,
    updated_by         uuid,

    nurse_id           uuid        not null,
    compensation_type  varchar(50) not null,
    effective_from     date        not null,

    constraint fk_nurse_compensation_type_change_nurse
        foreign key (nurse_id)
            references nurse(id),

    constraint uq_nurse_compensation_type_change
        unique (nurse_id, effective_from)
);