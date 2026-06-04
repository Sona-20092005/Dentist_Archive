create table treatment_plan
(
    id               uuid primary key,
    created_at       timestamptz not null,
    created_by       uuid,
    updated_at       timestamptz,
    updated_by       uuid,
    archived         boolean     not null,
    archived_at      timestamptz,
    archived_by      uuid,

    patient_id       uuid        not null,

    date             date        not null,
    plan_status      varchar(32),
    discount_percent numeric(5, 2),
    discount_amount  numeric(19, 4),
    completed_date   date,
    notes            text,

    constraint fk_treatment_plan_patient foreign key (patient_id)
        references patient (id)
);