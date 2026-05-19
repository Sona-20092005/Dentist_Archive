create table procedure
(
    id                   uuid primary key,
    created_at           timestamptz not null,
    created_by           uuid,
    updated_at           timestamptz,
    updated_by           uuid,
    archived             boolean     not null,
    archived_at          timestamptz,
    archived_by          uuid,

    doctor_id            uuid        not null,

    name                 text        not null,
    price                numeric(19, 2)       not null,
    description          text,

    constraint fk_patient_doctor foreign key (doctor_id)
        references users (id)
        on delete restrict
);
