create table technician
(
    id          uuid primary key,
    created_at  timestamptz not null,
    created_by  uuid,
    updated_at  timestamptz,
    updated_by  uuid,
    archived       boolean     not null,
    archived_at    timestamptz,
    archived_by    uuid,

    name        text not null,
    phones      jsonb,
    emails      jsonb,
    address     varchar(255),
    notes       text,

    doctor_id   uuid not null,

    constraint fk_technician_doctor
        foreign key (doctor_id)
            references users(id)
);

create table technician_procedure
(
    id            uuid primary key,
    created_at    timestamptz not null,
    created_by    uuid,
    updated_at    timestamptz,
    updated_by    uuid,
    archived      boolean     not null,
    archived_at   timestamptz,
    archived_by   uuid,

    name          text not null,
    price         numeric(19,2) not null,
    description   text,

    technician_id uuid not null,

    constraint fk_technician_procedure_technician
        foreign key (technician_id)
            references technician(id)
);