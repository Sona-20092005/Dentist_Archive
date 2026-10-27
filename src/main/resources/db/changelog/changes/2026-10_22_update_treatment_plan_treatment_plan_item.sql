alter table treatment_plan_item
    add column quantity integer not null default 1;

alter table treatment_plan
    add column final_amount numeric(19, 2);