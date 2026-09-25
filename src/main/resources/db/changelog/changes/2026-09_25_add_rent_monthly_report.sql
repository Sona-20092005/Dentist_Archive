create table rent_monthly_report
(
    id                         uuid primary key,
    created_at                 timestamptz not null,
    created_by                 uuid,
    updated_at                 timestamptz,
    updated_by                 uuid,

    clinic_rent_agreement_id   uuid        not null,
    year_month                 date        not null,
    status                     varchar(32) not null,

    regular_amount             numeric(19, 2) not null,
    overtime_amount            numeric(19, 2) not null,
    regular_minutes_used       integer     not null,
    overtime_minutes_used      integer     not null,
    manual_adjustment          numeric(19, 2) not null,

    locked                     boolean     not null,

    notes                      text,

    constraint fk_rent_monthly_report_clinic_rent_agreement
        foreign key (clinic_rent_agreement_id)
            references clinic_rent_agreement (id),

    constraint uq_rent_monthly_report_agreement_year_month
        unique (clinic_rent_agreement_id, year_month)
);