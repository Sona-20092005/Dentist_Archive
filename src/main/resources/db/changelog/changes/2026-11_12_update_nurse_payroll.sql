alter table nurse_payroll
    drop column year,
    drop column month,
    add column year_month date not null;