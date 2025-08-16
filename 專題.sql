Create table member(
	m_id int not null primary key,
	m_name nvarchar(10) not null,
	m_identity nvarchar(10) not null,
	m_gender nvarchar(2),
	m_account nvarchar(20) not null,
	m_password nvarchar(20) not null,
	m_address nvarchar(50),
	m_phone varchar(20),
	m_birthday date,
	m_email varchar(30) not null,
	creation date,
	m_state int
)
INSERT INTO member VALUES
(1,'�����n','A123456789','�k','kevin','123456','���c���s�͸��G�q126��','0912345678','1999-12-31','aaaaa@gmail.com','2025-06-21',1),
(2,'�����l','Z223455789','�k','henry','123456','���c���j��Ϸs�H��15��','0922344678','1988-03-31','kkk@gmail.com','2025-06-20',1),
(3,'������','U223464789','�k','candy','123456','��饫�s�_�Ϸs�H��15��','0922355678','1992-04-21','keek@gmail.com','2025-06-20',1);
Create table worker(
	w_id int not null primary key,
	w_name nvarchar(10) not null,
	w_account nvarchar(20) not null,
	w_password nvarchar(20) not null,
	role_id int
)
insert into worker values
(1,'�޲z��','w1111','w123456',1),
(2,'�U�ڧ@�~��','w2222','w123456',2),
(3,'�H�Υd�@�~��','w3333','w123456',3);

Create table permission(
	role_id int not null,
	page_id int not null,
)
insert into permission values
(1,1),
(1,2),
(1,3),
(1,4),
(1,5),
(2,2),
(3,3);

Create table "page"(
	page_id int not null primary key,
	page_name nvarchar(20),
	page_exp nvarchar(20)
)

insert into "page" values
(1,'�b��','�b�᭶������'),
(2,'�U��','�U�ڭ�������'),
(3,'�H�Υd','�H�Υd��������'),
(4,'���','�����������'),
(5,'���b','���b��������');

Create table "role"(
	role_id int not null primary key,
	role_name nvarchar(20)
)

insert into "role" values
(1,'�޲z��'),
(2,'�U��'),
(3,'�H�Υd');

alter table worker
add constraint FK_worker_roleId
foreign key (role_id) references "role"(role_id)

alter table permission
add constraint permission_id primary key (role_id,page_id)

alter table permission
add constraint FK_role_id
foreign key (role_id) references "role"(role_id)

alter table permission
add constraint FK_page_id
foreign key (page_id)references page(page_id)

�b��
** �Ȧ�b��10�X : 7(�Ȧ�)XXX(�~������)00001(�y�������X)X(�ýX����)
�~������ : 100 -> ���s�b�� 

CREATE TABLE account_application(
    application_id nvarchar(12) not null PRIMARY KEY,
    m_id int not null CONSTRAINT act_application_m_id_fk REFERENCES member(m_id),
    id_card_front nvarchar(255) not null,
    id_card_back nvarchar(255) not null,
    second_doc nvarchar(255),
    status nvarchar(20),
    reviewer_id int,
    review_time datetime,
    rejection_reason nvarchar(200),
    apply_time datetime not null
);

CREATE TABLE accounts(
    account_id nvarchar(12) not null PRIMARY KEY,
    m_id int not null CONSTRAINT accounts_m_id_fk REFERENCES member(m_id),
    account_name nvarchar(50) not null,
    currency nvarchar(10) not null,
    balance decimal(15,2) not null,
    opened_date datetime,
    status nvarchar(20),
    memo nvarchar(200),
    operator_id int CONSTRAINT accounts_operator_fk REFERENCES worker(w_id),
    status_updated_time datetime
);

INSERT INTO accounts( account_id,m_id,account_name,currency,balance,opened_date, status) VALUES 
('7100000015',1,'�����s��','NT',120000,'2025-06-24','�ҥ�'),
('7100000028',2,'�����s��','NT',1500000,'2025-06-24','�ҥ�'),
('7100000039',3,'�����s��','NT',25000000,'2025-06-24','�ҥ�'),
('7100000047',4,'�����s��','NT',3200000,'2025-06-24','�ҥ�'),
('7100000051',5,'�����s��','NT',6510000,'2025-06-24','�ҥ�')
('7999999987',999,'�c�l�Ȧ�','NT',9999999999,'2025-06-24','�ҥ�')

CREATE TABLE transactions(
    transaction_id nvarchar(12) not null PRIMARY KEY,
    account_id  nvarchar(12) not null CONSTRAINT t_account_id_fk REFERENCES accounts(account_id),
    transaction_type nvarchar(30),
    to_bank_code nvarchar(3),
    to_account_id nvarchar(20),
    currency nvarchar(10) not null,
    amount decimal(10,2) not null,
    tx_time datetime,
    memo nvarchar(200),
    status nvarchar(20),
    operator_id int CONSTRAINT t_accounts_operator_fk REFERENCES worker(w_id)
);

CREATE TABLE serial_control (
    type NVARCHAR(20),
    key_code NVARCHAR(20),
    last_number INT NOT NULL,
    PRIMARY KEY (type, key_code)
);

���

CREATE TABLE fund_account
(
    fund_acc_id int PRIMARY KEY IDENTITY(1,1),
    member_id int NOT NULL UNIQUE CONSTRAINT fk_fa_mid REFERENCES member(m_id),
	account_id nvarchar(12) NOT NULL UNIQUE CONSTRAINT fk_fa_accid REFERENCES accounts(account_id),
	risk_type nchar(3) NOT NULL,
	acc_status nchar(3) NOT NULL
);
CREATE TABLE fund
(
	fund_id int PRIMARY KEY IDENTITY(1,1),
    fund_code varchar(20) NOT NULL UNIQUE,
	fund_name nvarchar(100) NOT NULL,
	currency char(3),
	fund_type nvarchar(20),
	size decimal(18,4),
	create_date date,
	invest_area nvarchar(20),
	min_buy decimal(18,4),
	buy_fee decimal(18,4),
	risk_level int,
	fund_status nchar(3)
);
CREATE TABLE fund_nav
(
    nav_id int PRIMARY KEY IDENTITY(1,1),
	fund_id int NOT NULL CONSTRAINT fk_nav_fid REFERENCES fund(fund_id),
	nav_date date NOT NULL,
	nav decimal(18,4) NOT NULL,
	CONSTRAINT uq_fid_nd UNIQUE(fund_id, nav_date)
);
CREATE TABLE fund_holdings
(
    fund_hold_id int PRIMARY KEY IDENTITY(1,1),
    fund_acc_id int NOT NULL CONSTRAINT fk_fh_faid REFERENCES fund_account(fund_acc_id),
	fund_id int NOT NULL CONSTRAINT fk_fh_fid REFERENCES fund(fund_id),
	units decimal(18,4) NOT NULL,
	cost decimal(18,4) NOT NULL,
	CONSTRAINT uq_faid_fid UNIQUE(fund_acc_id, fund_id)
);
CREATE TABLE fund_transaction
(
	fund_tran_id int PRIMARY KEY IDENTITY(1,1),
	fund_acc_id int NOT NULL CONSTRAINT fk_ft_faid REFERENCES fund_account(fund_acc_id),
	fund_id int NOT NULL CONSTRAINT fk_ft_fid REFERENCES fund(fund_id),
	tran_type nchar(2) NOT NULL,
	units decimal(18,4) NOT NULL,
	price decimal(18,4) NOT NULL,
	fee decimal(18,4) NOT NULL,
	balance decimal(18,4) NOT NULL,
	tran_time datetime2 NOT NULL,
	tran_status nchar(3) NOT NULL,
    remark nvarchar(200)
);
ú�O

CREATE TABLE bills ( 
bill_id NVARCHAR(11) NOT NULL PRIMARY KEY, -- �b��s���G�T�w11�X 
m_id INT NOT NULL, -- �|���s�� 
account_id NVARCHAR(12), -- �I�ڱb�� 
card_id NVARCHAR(16), -- �d���G�H�Υd�~���� 
b_type NVARCHAR(20), -- �b�������G�H�Υd�B���O�B�q�O 
provider NVARCHAR(50), -- �b��ӷ����W�� 
status NVARCHAR(10), -- �b�檬�A�G���I��, �w�I��, �O��, ����
b_amount DECIMAL(15, 2) NOT NULL, -- �`���B 
minimum_payment DECIMAL(15, 2), -- �H�Υd�̧C��ú���B�G�H�Υd�~���� 
paid_amount DECIMAL(15, 2), -- �w�I���B 
card_settle_date DATE, -- �H�Υd���b��G�H�Υd�~���� 
due_date DATE, -- ú�ںI��� 
payment_date DATE, -- �I�ڤ�� 
payment_method NVARCHAR(20), -- ú�O�覡 
auto_pay NVARCHAR(20), -- �O�_�۰ʦ��� 
note NVARCHAR(200), -- �Ƶ� 
createdDate DATE -- �إ߮ɶ�
);



CONSTRAINT FK_bills_member FOREIGN KEY (m_id) REFERENCES member(m_id),
CONSTRAINT FK_bills_account FOREIGN KEY (account_id) REFERENCES accounts(account_id),
CONSTRAINT FK_bills_card FOREIGN KEY (card_id) REFERENCES credittransactions(card_id)
);

INSERT INTO bills (
    b_id, m_id, m_name, account_id, card_id, b_type, provider, status, 
    b_amount, minimum_payment, paid_amount, card_settle_date, due_date, 
    payment_date, payment_method, auto_pay, note, createdAt
)
VALUES
-- �b��1�G���I��
('00000000001', 1, '�����n', '111111111111', '4111111111111111', '�H�Υd', '��i�Ȧ�', '���I��',
 12000.00, 1200.00, 0.00, '2025-06-30', '2025-07-15', NULL, '�b�ᦩ��ú�O', '�_', 'NULL', '2025-06-01'),

-- �b��2�G�w�I��
('00000000002', 2, '�����l', '111111111112', '4222222222222222', '�H�Υd', '��i�Ȧ�', '�w�I��',
 8500.00, 850.00, 8500.00, '2025-06-30', '2025-07-15', '2025-07-14', '�b�ᦩ��ú�O', '�_', '�Ǯ�ú��', '2025-06-02'),

-- �b��3�G���I��
('00000000003', 3, '������', '111111111113', '4333333333333333', '�H�Υd', '��i�Ȧ�', '���I��',
 15300.00, 1530.00, 15300.00, '2025-06-30', '2025-07-15', NULL, '�b�ᦩ��ú�O', '�_', 'NULL', '2025-06-05');


























�����-card_detail 
CREATE TABLE card_detail (
    card_id NVARCHAR NOT NULL PRIMARY KEY,                   -- �d�� (�D��)
    m_id INT NOT NULL,                                  -- ���d�H (�~��A�Ѧ� member.m_id)
    card_type INT ,                             -- �d�O�]���q���A�D�~��^
    cvv_code NVARCHAR(4),                                -- �w���X
    expiration_date DATE,                               -- �����
    credit_limit DECIMAL,                               -- �B��
    currentBalance DECIMAL(15, 2),                      -- �Ѿl�B��
    status NVARCHAR(20),                                 -- ���A
    issued_date DATE,                                   -- �o�d��
);

    CONSTRAINT FK_card_detail_member 
        FOREIGN KEY (m_id) REFERENCES member(m_id);


INSERT INTO card_detail (
    card_id, m_id, card_type, cvv_code, expiration_date,
    credit_limit, currentBalance, status, issued_date
)
VALUES 
-- �Ĥ@���GVISA
(1000100010001000, 3, 1, '123', '2026-12-31', 100000, 75000.50, '�ҥ�', '2023-05-01'),

-- �ĤG���GJCB
(2000200020002000, 5, 2, '456', '2025-08-31', 80000, 20000.00, '�ҥ�', '2022-11-15');



CREATE TABLE credittransactions (
    transaction_id INT NOT NULL PRIMARY KEY,                 -- �D��G�������ID
    card_id NVARCHAR(16) NOT NULL,                           -- �~��G�d���A�s�� card_detail
    m_id INT NOT NULL,                                       -- �~��G���d�H�A�s�� member
    bill_id NVARCHAR(11),                                    -- �~��G�b��ID�A�s�� bills
    amount DECIMAL(15, 2),                                   -- �`���B
    Merchant_type VARCHAR(20),                               -- �ӷ~����
    cashback DECIMAL(15, 2),                                 -- �^�X
    transaction_time DATETIME,                               -- ����ɶ�
    description NVARCHAR(MAX),                              -- �Ƶ��y�z�]�i����סA��ĳ�� NVARCHAR(MAX) �䴩�h�y�P����r�^

	);

    -- �~�����
	ALTER TABLE credittransactions
    add CONSTRAINT FK_credittransactions_card 
        FOREIGN KEY (card_id) REFERENCES card_detail(card_id);

ALTER TABLE credittransactions
ADD CONSTRAINT FK_credittransactions_member 
        FOREIGN KEY (m_id) REFERENCES member(m_id);

ALTER TABLE credittransactions
add CONSTRAINT FK_credittransactions_bill 
        FOREIGN KEY (bill_id) REFERENCES bills(bill_id);


