create table role
(
    id                uuid primary key,
    created_at        timestamptz not null,
    created_by        uuid,
    updated_at        timestamptz,
    updated_by        uuid,
    archived          boolean     not null,
    archived_at       timestamptz,
    archived_by       uuid,

    names_map         jsonb       not null,
    code              varchar(255) not null unique,
    descriptions_map  jsonb       not null,
    client_role       boolean     not null,
    scope             varchar(255) not null,
    permission_codes  jsonb       not null
);

create table users
(
    id                              uuid primary key,
    created_at                      timestamptz not null,
    created_by                      uuid,
    updated_at                      timestamptz,
    updated_by                      uuid,
    archived                        boolean not null,
    archived_at                     timestamptz,
    archived_by                     uuid,

    user_type                       varchar(50) not null,

    nick_name                       varchar(255) not null unique,
    full_name                       varchar(255) not null,
    scope                           varchar(50) not null,

    password_hash                   varchar(255) not null,
    password_set_at                 timestamptz not null,
    temporary_password              boolean,
    number_of_failed_login_attempts integer not null default 0,
    last_login_failed_at            timestamptz,

    additional_params               jsonb,

    -- Doctor fields
    email                           varchar(255) not null,
    phone                           varchar(255),
    company_id                      uuid,
    locale                          varchar(50)
);

create table user_roles
(
    user_id uuid not null,
    role_id uuid not null,
    primary key (user_id, role_id),
    constraint fk_user_roles_user foreign key (user_id) references users(id),
    constraint fk_user_roles_role foreign key (role_id) references role(id)
);