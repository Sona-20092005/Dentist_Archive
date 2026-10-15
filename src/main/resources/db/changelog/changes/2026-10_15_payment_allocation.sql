create table payment_allocation
(
    id                    uuid primary key,
    created_at            timestamptz    not null,
    created_by            uuid,
    updated_at            timestamptz,
    updated_by            uuid,
    archived              boolean        not null,
    archived_at           timestamptz,
    archived_by           uuid,

    payment_id            uuid           not null,
    amount                numeric(19, 2)  not null,
    treatment_plan_id     uuid,
    completed_treatment_id uuid,
    archive_reason        varchar(255),

    constraint fk_payment_allocation_payment
        foreign key (payment_id)
            references payment (id),

    constraint fk_payment_allocation_treatment_plan
        foreign key (treatment_plan_id)
            references treatment_plan (id),

    constraint fk_payment_allocation_completed_treatment
        foreign key (completed_treatment_id)
            references completed_treatment (id),

    constraint chk_payment_allocation_amount
        check (amount > 0),

    constraint chk_payment_allocation_target
        check (
            (treatment_plan_id is not null and completed_treatment_id is null)
                or
            (treatment_plan_id is null and completed_treatment_id is not null)
            ),

    constraint chk_payment_allocation_archive_reason
        check (
            (archived = false and archive_reason is null)
                or
            (archived = true and archive_reason is not null)
            )
);