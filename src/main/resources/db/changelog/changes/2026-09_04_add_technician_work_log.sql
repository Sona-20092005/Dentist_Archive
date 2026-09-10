create table technician_work_log
(
    id                      uuid primary key,
    created_at              timestamptz not null,
    created_by              uuid,
    updated_at              timestamptz,
    updated_by              uuid,
    archived                boolean     not null,
    archived_at             timestamptz,
    archived_by             uuid,

    technician_id           uuid not null,
    requested_date          date not null,
    completed_date          date not null,

    unit_price              numeric(19,2),
    quantity                integer,

    tooth_numbers           jsonb,

    technician_procedure_id uuid not null,
    patient_id              uuid not null,

    payment_date            date,
    notes                   text,

    constraint fk_technician_work_log_technician
        foreign key (technician_id)
            references technician(id),

    constraint fk_technician_work_log_technician_procedure
        foreign key (technician_procedure_id)
            references technician_procedure(id),

    constraint fk_technician_work_log_patient
        foreign key (patient_id)
            references patient(id)
);