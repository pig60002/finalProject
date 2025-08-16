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
(1,'王阿南','A123456789','男','kevin','123456','中壢市新生路二段126號','0912345678','1999-12-31','aaaaa@gmail.com','2025-06-21',1),
(2,'李梨子','Z223455789','女','henry','123456','中壢市大園區新埔街15號','0922344678','1988-03-31','kkk@gmail.com','2025-06-20',1),
(3,'陳美鳳','U223464789','女','candy','123456','桃園市新北區新埔街15號','0922355678','1992-04-21','keek@gmail.com','2025-06-20',1);
Create table worker(
	w_id int not null primary key,
	w_name nvarchar(10) not null,
	w_account nvarchar(20) not null,
	w_password nvarchar(20) not null,
	role_id int
)
insert into worker values
(1,'管理者','w1111','w123456',1),
(2,'貸款作業員','w2222','w123456',2),
(3,'信用卡作業員','w3333','w123456',3);

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
(1,'帳戶','帳戶頁面控管'),
(2,'貸款','貸款頁面控管'),
(3,'信用卡','信用卡頁面控管'),
(4,'基金','基金頁面控管'),
(5,'結帳','結帳頁面控管');

Create table "role"(
	role_id int not null primary key,
	role_name nvarchar(20)
)

insert into "role" values
(1,'管理員'),
(2,'貸款'),
(3,'信用卡');

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

帳戶
** 銀行帳戶10碼 : 7(銀行)XXX(業務類型)00001(流水號五碼)X(亂碼產生)
業務類型 : 100 -> 活存帳戶 

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
('7100000015',1,'活期存款','NT',120000,'2025-06-24','啟用'),
('7100000028',2,'活期存款','NT',1500000,'2025-06-24','啟用'),
('7100000039',3,'活期存款','NT',25000000,'2025-06-24','啟用'),
('7100000047',4,'活期存款','NT',3200000,'2025-06-24','啟用'),
('7100000051',5,'活期存款','NT',6510000,'2025-06-24','啟用')
('7999999987',999,'柚子銀行','NT',9999999999,'2025-06-24','啟用')

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

基金

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
繳費

CREATE TABLE bills ( 
bill_id NVARCHAR(11) NOT NULL PRIMARY KEY, -- 帳單編號：固定11碼 
m_id INT NOT NULL, -- 會員編號 
account_id NVARCHAR(12), -- 付款帳號 
card_id NVARCHAR(16), -- 卡號：信用卡才有值 
b_type NVARCHAR(20), -- 帳單類型：信用卡、水費、電費 
provider NVARCHAR(50), -- 帳單來源單位名稱 
status NVARCHAR(10), -- 帳單狀態：未付款, 已付款, 逾期, 取消
b_amount DECIMAL(15, 2) NOT NULL, -- 總金額 
minimum_payment DECIMAL(15, 2), -- 信用卡最低應繳金額：信用卡才有值 
paid_amount DECIMAL(15, 2), -- 已付金額 
card_settle_date DATE, -- 信用卡結帳日：信用卡才有值 
due_date DATE, -- 繳款截止日 
payment_date DATE, -- 付款日期 
payment_method NVARCHAR(20), -- 繳費方式 
auto_pay NVARCHAR(20), -- 是否自動扣款 
note NVARCHAR(200), -- 備註 
createdDate DATE -- 建立時間
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
-- 帳單1：未付款
('00000000001', 1, '王阿南', '111111111111', '4111111111111111', '信用卡', '資展銀行', '未付款',
 12000.00, 1200.00, 0.00, '2025-06-30', '2025-07-15', NULL, '帳戶扣款繳費', '否', 'NULL', '2025-06-01'),

-- 帳單2：已付款
('00000000002', 2, '李梨子', '111111111112', '4222222222222222', '信用卡', '資展銀行', '已付款',
 8500.00, 850.00, 8500.00, '2025-06-30', '2025-07-15', '2025-07-14', '帳戶扣款繳費', '否', '準時繳款', '2025-06-02'),

-- 帳單3：未付款
('00000000003', 3, '陳美鳳', '111111111113', '4333333333333333', '信用卡', '資展銀行', '未付款',
 15300.00, 1530.00, 15300.00, '2025-06-30', '2025-07-15', NULL, '帳戶扣款繳費', '否', 'NULL', '2025-06-05');


























假資料-card_detail 
CREATE TABLE card_detail (
    card_id NVARCHAR NOT NULL PRIMARY KEY,                   -- 卡號 (主鍵)
    m_id INT NOT NULL,                                  -- 持卡人 (外鍵，參考 member.m_id)
    card_type INT ,                             -- 卡別（普通欄位，非外鍵）
    cvv_code NVARCHAR(4),                                -- 安全碼
    expiration_date DATE,                               -- 到期日
    credit_limit DECIMAL,                               -- 額度
    currentBalance DECIMAL(15, 2),                      -- 剩餘額度
    status NVARCHAR(20),                                 -- 狀態
    issued_date DATE,                                   -- 發卡日
);

    CONSTRAINT FK_card_detail_member 
        FOREIGN KEY (m_id) REFERENCES member(m_id);


INSERT INTO card_detail (
    card_id, m_id, card_type, cvv_code, expiration_date,
    credit_limit, currentBalance, status, issued_date
)
VALUES 
-- 第一筆：VISA
(1000100010001000, 3, 1, '123', '2026-12-31', 100000, 75000.50, '啟用', '2023-05-01'),

-- 第二筆：JCB
(2000200020002000, 5, 2, '456', '2025-08-31', 80000, 20000.00, '啟用', '2022-11-15');



CREATE TABLE credittransactions (
    transaction_id INT NOT NULL PRIMARY KEY,                 -- 主鍵：交易紀錄ID
    card_id NVARCHAR(16) NOT NULL,                           -- 外鍵：卡號，連結 card_detail
    m_id INT NOT NULL,                                       -- 外鍵：持卡人，連結 member
    bill_id NVARCHAR(11),                                    -- 外鍵：帳單ID，連結 bills
    amount DECIMAL(15, 2),                                   -- 總金額
    Merchant_type VARCHAR(20),                               -- 商業類型
    cashback DECIMAL(15, 2),                                 -- 回饋
    transaction_time DATETIME,                               -- 交易時間
    description NVARCHAR(MAX),                              -- 備註描述（可選長度，建議用 NVARCHAR(MAX) 支援多語與長文字）

	);

    -- 外鍵約束
	ALTER TABLE credittransactions
    add CONSTRAINT FK_credittransactions_card 
        FOREIGN KEY (card_id) REFERENCES card_detail(card_id);

ALTER TABLE credittransactions
ADD CONSTRAINT FK_credittransactions_member 
        FOREIGN KEY (m_id) REFERENCES member(m_id);

ALTER TABLE credittransactions
add CONSTRAINT FK_credittransactions_bill 
        FOREIGN KEY (bill_id) REFERENCES bills(bill_id);


