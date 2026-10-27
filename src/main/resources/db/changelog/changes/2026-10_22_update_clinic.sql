alter table clinic
    add column commission_percent numeric(5, 2),
    add column commission_payment_methods jsonb;