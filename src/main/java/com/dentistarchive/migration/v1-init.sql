create table patient (
    id uuid primary key,
    created_at timestamp not null,
    created_by uuid,

    updated_at timestamp,
    updated_by uuid,

    archived boolean,
    archived_at timestamp,
    archived_by uuid,

    patient_name varchar(255) not null,

    address text,
    passport_information text,
    notes text
);

create table phone (
    id uuid primary key,
    created_at timestamp not null,
    created_by uuid,

    updated_at timestamp,
    updated_by uuid,

    archived boolean,
    archived_at timestamp,
    archived_by uuid,

    phone varchar(50) not null,
    patient_id uuid not null,

    constraint fk_patient
    foreign key (patient_id)
    references patient(id)
    on delete cascade
);

create table email (
    id uuid primary key,
    created_at timestamp not null,
    created_by uuid,

    updated_at timestamp,
    updated_by uuid,

    archived boolean,
    archived_at timestamp,
    archived_by uuid,

    email varchar(50) not null,
    patient_id uuid not null,

    constraint fk_patient
    foreign key (patient_id)
    references patient(id)
    on delete cascade
);


