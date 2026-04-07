alter table patient
    add column doctor_id uuid not null;

alter table patient
    add constraint fk_patient_doctor
        foreign key (doctor_id)
            references users(id)
            on delete restrict;
