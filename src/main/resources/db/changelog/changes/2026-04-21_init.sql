create table users
(
    id                              uuid primary key,
    created_at                      timestamptz  not null,
    created_by                      uuid,
    updated_at                      timestamptz,
    updated_by                      uuid,
    archived                        boolean      not null,
    archived_at                     timestamptz,
    archived_by                     uuid,

    user_type                       varchar(50)  not null,

    user_name                       varchar(255) not null unique,
    full_name                       varchar(255) not null,
    role                            varchar(255) not null,

    password_hash                   varchar(255) not null,
    password_set_at                 timestamptz  not null,
    number_of_failed_login_attempts integer      not null default 0,
    last_login_failed_at            timestamptz,

    additional_params               jsonb,

    -- Doctor fields
    email                           varchar(255),
    phone                           varchar(255)
);

create table patient
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
    patient_status       varchar(32) not null,
    phones               jsonb       not null,
    emails               jsonb       not null,
    address              text,

    passport_information text,
    notes                text,

    constraint fk_patient_doctor foreign key (doctor_id)
        references users (id)
        on delete restrict
);

create table refresh_tokens
(
    id                   uuid primary key,
    created_at           timestamptz not null,
    created_by           uuid,

    user_id      uuid        not null,
    token_hash   varchar(500) not null unique,

    expires_at   timestamptz not null,
    revoked      boolean     not null,
    revoked_at   timestamptz,
    revoke_reason text,

    constraint fk_refresh_tokens_user
        foreign key (user_id)
            references users(id)
            on delete cascade
);