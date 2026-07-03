create extension if not exists btree_gist;

alter table work_schedule
    add constraint work_schedule_no_overlap
    exclude using gist (
    clinic_id with =,
    daterange(
        effective_from,
        coalesce(effective_until + 1, 'infinity'::date),
        '[)'
    ) with &&
)
where (archived = false);