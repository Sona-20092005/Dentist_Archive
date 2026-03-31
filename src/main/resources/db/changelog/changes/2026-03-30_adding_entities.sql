create table procedure
(
    id          uuid primary key,
    created_at  timestamptz not null,
    created_by  uuid,
    updated_at  timestamptz,
    updated_by  uuid,
    archived    boolean     not null,
    archived_at timestamptz,
    archived_by uuid,

    name        text        not null,
    price       numeric(19, 2),
    description text,
    notes       text
);

create table technician
(
    id          uuid primary key,
    created_at  timestamptz not null,
    created_by  uuid,
    updated_at  timestamptz,
    updated_by  uuid,
    archived    boolean     not null,
    archived_at timestamptz,
    archived_by uuid,

    name        text        not null,
    address     text,
    phones      jsonb       not null,
    notes       text
);

create table nurse
(
    id                   uuid primary key,
    created_at           timestamptz not null,
    created_by           uuid,
    updated_at           timestamptz,
    updated_by           uuid,
    archived             boolean     not null,
    archived_at          timestamptz,
    archived_by          uuid,

    name                 text        not null,
    base_salary          numeric(19, 2),
    overtime_hourly_rate numeric(19, 2),
    hire_date            date,
    termination_date     date,
    phones               jsonb       not null,
    notes                text
);

create table material
(
    id          uuid primary key,
    created_at  timestamptz not null,
    created_by  uuid,
    updated_at  timestamptz,
    updated_by  uuid,
    archived    boolean     not null,
    archived_at timestamptz,
    archived_by uuid,

    name        text        not null,
    unit_price  numeric(19, 2),
    stores      jsonb       not null,
    description text,
    notes       text
)


