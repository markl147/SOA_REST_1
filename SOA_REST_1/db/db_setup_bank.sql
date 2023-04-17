use soa_rest;

drop table if exists bankaccount;
CREATE TABLE bankaccount (
    branch_code    VARCHAR(10),
    account_number INTEGER,
    cust_name      VARCHAR(20),
    cust_address   VARCHAR(50),
    cust_rating    INTEGER,
    balance        FLOAT,
    PRIMARY KEY (branch_code, account_number)
);

select * from bankaccount;

INSERT INTO bankaccount (branch_code, account_number, cust_name, cust_address, cust_rating, balance)
VALUES 	('ATH001', 123, 'Billy Smith', 'Willow Park', 70, 1000), 
		('ATH001', 124, 'William Doe', 'Auburn Heights', 85, 2000),
		('MUL001', 457, 'Jane Smith', 'Dublin Rd.', 50, 500),
        ('MUL001', 688, 'Mary Smith', 'Galway Rd.', 67, 1500);
        
select * from bankaccount;