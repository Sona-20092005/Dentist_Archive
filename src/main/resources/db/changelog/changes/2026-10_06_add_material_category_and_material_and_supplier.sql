create table material_category
(
    id          uuid primary key,
    created_at  timestamptz not null,
    created_by  uuid,
    updated_at  timestamptz,
    updated_by  uuid,
    archived    boolean     not null,
    archived_at timestamptz,
    archived_by uuid,

    name        varchar(255) not null,
    description text,
    doctor_id   uuid         not null,

    constraint fk_material_category_doctor
        foreign key (doctor_id)
            references users (id)
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

    name        varchar(255) not null,
    description text,
    notes       text,
    category_id uuid         not null,

    constraint fk_material_category
        foreign key (category_id)
            references material_category (id)
);

create table supplier
(
    id          uuid primary key,
    created_at  timestamptz not null,
    created_by  uuid,
    updated_at  timestamptz,
    updated_by  uuid,
    archived    boolean     not null,
    archived_at timestamptz,
    archived_by uuid,

    name        varchar(255) not null,
    phones      jsonb,
    emails      jsonb,
    addresses   jsonb,
    website     varchar(255),
    notes       text,
    doctor_id   uuid         not null,

    constraint fk_supplier_doctor
        foreign key (doctor_id)
            references users (id)
);