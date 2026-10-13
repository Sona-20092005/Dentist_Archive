create table material_supplier
(
    id                 uuid primary key,
    created_at         timestamptz not null,
    created_by         uuid,
    updated_at         timestamptz,
    updated_by         uuid,
    archived           boolean     not null,
    archived_at        timestamptz,
    archived_by        uuid,

    unit_price         numeric(19, 2) not null,
    last_price_update  timestamptz    not null,
    unit_of_measure    varchar(255),
    product_name       varchar(255),
    notes              text,

    material_id        uuid           not null,
    supplier_id        uuid           not null,

    constraint fk_material_supplier_material
        foreign key (material_id)
            references material (id),

    constraint fk_material_supplier_supplier
        foreign key (supplier_id)
            references supplier (id)
--
--     constraint uq_material_supplier_material_supplier
--         unique (material_id, supplier_id)
);

create table material_purchase
(
    id                  uuid primary key,
    created_at          timestamptz not null,
    created_by          uuid,
    updated_at          timestamptz,
    updated_by          uuid,
    archived            boolean     not null,
    archived_at         timestamptz,
    archived_by         uuid,

    quantity            integer         not null,
    unit_price          numeric(19, 2)  not null,
    total_amount        numeric(19, 2)  not null,
    purchase_date       date            not null,
    notes               text,

    material_supplier_id uuid           not null,

    constraint fk_material_purchase_material_supplier
        foreign key (material_supplier_id)
            references material_supplier (id)
);