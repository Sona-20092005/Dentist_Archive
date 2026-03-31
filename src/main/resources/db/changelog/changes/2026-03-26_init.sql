create table patient
(
    id                   uuid primary key,
    created_at           timestamptz not null,
    created_by           uuid,
    updated_at           timestamptz,
    updated_by           uuid,
    archived             boolean    not null,
    archived_at          timestamptz,
    archived_by          uuid,

    name                 text       not null,
    phones               jsonb      not null,
    emails               jsonb      not null,
    address              text,

    passport_information text,
    notes                text
);



