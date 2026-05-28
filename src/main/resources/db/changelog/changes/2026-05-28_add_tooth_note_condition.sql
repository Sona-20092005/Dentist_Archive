create table tooth_condition_note
(
    id                   uuid primary key,
    created_at           timestamptz not null,
    created_by           uuid,
    updated_at           timestamptz,
    updated_by           uuid,

    patient_id           uuid        not null,

    tooth_number         int       not null,
    description          text not null,

    constraint fk_tooth_condition_note_patient foreign key (patient_id)
        references patient (id)
        on delete restrict
);
