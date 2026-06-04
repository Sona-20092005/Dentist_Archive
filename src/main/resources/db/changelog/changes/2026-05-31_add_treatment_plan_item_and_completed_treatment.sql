create table treatment_plan_item
(
    id                uuid primary key,
    created_at        timestamptz not null,
    created_by        uuid,
    updated_at        timestamptz,
    updated_by        uuid,
    archived          boolean     not null,
    archived_at       timestamptz,
    archived_by       uuid,

    treatment_plan_id uuid        not null,
    procedure_id      uuid        not null,

    item_status       varchar(32) not null,
    item_status_sort_order int,

    unit_price        numeric(19, 2) not null,
    tooth_numbers     jsonb,
    notes             text,

    constraint fk_treatment_plan_item_plan foreign key (treatment_plan_id)
        references treatment_plan (id),

    constraint fk_treatment_plan_item_procedure foreign key (procedure_id)
        references procedure (id)
);

create table completed_treatment
(
    id                    uuid primary key,
    created_at            timestamptz not null,
    created_by            uuid,
    updated_at            timestamptz,
    updated_by            uuid,
    archived              boolean     not null,
    archived_at           timestamptz,
    archived_by           uuid,

    patient_id            uuid        not null,
    procedure_id          uuid,
    appointment_id        uuid,
    treatment_plan_item_id uuid,

    date                  date        not null,
    unit_price            numeric(19, 2),
    tooth_numbers         jsonb,
    notes                 text,

    constraint fk_completed_treatment_patient foreign key (patient_id)
        references patient (id),

    constraint fk_completed_treatment_procedure foreign key (procedure_id)
        references procedure (id),

    constraint fk_completed_treatment_plan_item foreign key (treatment_plan_item_id)
        references treatment_plan_item (id)
);