alter table role
    add column name varchar(255),
    add column description varchar(1000);

update role
set name = code;

alter table role
    alter column name set not null;

alter table role
    drop column if exists names_map,
    drop column if exists descriptions_map,
    drop column if exists updated_at,
    drop column if exists updated_by,
    drop column if exists archived,
    drop column if exists archived_at,
    drop column if exists archived_by;