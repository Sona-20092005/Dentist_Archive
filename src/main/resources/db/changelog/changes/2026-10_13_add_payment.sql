create table payment
(
    id                uuid primary key,
    created_at        timestamptz not null,
    created_by        uuid,
    updated_at        timestamptz,
    updated_by        uuid,
    archived          boolean     not null,
    archived_at       timestamptz,
    archived_by       uuid,

    date              date           not null,
    amount            numeric(19, 2) not null,
    payment_method    varchar(255)   not null,
    commission_amount numeric(19, 2) not null default 0,
    notes             text,

    patient_id        uuid           not null,

    constraint fk_payment_patient
        foreign key (patient_id)
            references patient (id)
);