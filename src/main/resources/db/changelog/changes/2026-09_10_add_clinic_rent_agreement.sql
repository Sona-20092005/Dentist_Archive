create table clinic_rent_agreement
(
    id                    uuid primary key,
    created_at            timestamptz not null,
    created_by            uuid,
    updated_at            timestamptz,
    updated_by            uuid,
    archived              boolean     not null,
    archived_at           timestamptz,
    archived_by           uuid,

    clinic_id             uuid        not null,
    effective_from        date        not null,
    effective_until       date,
    calculation_type      varchar(32) not null,
    fixed_monthly_rent    numeric(19, 2),
    hourly_rate           numeric(19, 2),
    overtime_hourly_rate  numeric(19, 2),
    collection_percentage integer,

    notes                 text,

    constraint fk_clinic_rent_agreement_clinic
        foreign key (clinic_id)
            references clinic (id)
);