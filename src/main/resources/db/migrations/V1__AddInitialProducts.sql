create schema if not exists product;

create table if not exists product.products (
    id serial primary key,
    name varchar(255) not null,
    description text not null,
    monthly_fee decimal(10, 2) not null,
    category        VARCHAR(50),
    min_amount      NUMERIC(15,2),
    max_amount      NUMERIC(15,2),
    interest_rate   NUMERIC(5,2),
    term_months     INTEGER,
    is_active       BOOLEAN DEFAULT TRUE,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO  product.products (name, description, monthly_fee, category, min_amount, max_amount, interest_rate, term_months, is_active)
VALUES
('Standard Savings Account',
 'A basic savings account offering 4% interest per annum with no monthly fees and easy access to your funds.',
 0.00, 'Savings', 0.00, 500000.00, 4.00, NULL, TRUE),

('32 Day Savings Account',
 'A notice savings account requiring 32 days notice for withdrawals, offering higher interest rates than instant-access accounts to reward disciplined saving.',
 0.00, 'Savings', 1000.00, 1000000.00, 6.50, NULL, TRUE),

('24 Hour Savings Account',
 'A high-liquidity savings account allowing withdrawals within 24 hours notice, balancing competitive interest rates with quick access to your funds.',
 0.00, 'Savings', 500.00, 750000.00, 5.00, NULL, TRUE),

('Fixed Deposit Account',
 'A fixed-term savings account that locks in your funds for a chosen period, offering premium interest rates in exchange for a fixed commitment.',
 0.00, 'Savings', 5000.00, 5000000.00, 8.75, 12, TRUE),

('Tax-Free Savings Account',
 'A government-regulated savings account allowing up to R36,000 per year in tax-free contributions, with all interest and growth completely exempt from tax.',
 0.00, 'Savings', 500.00, 36000.00, 5.50, NULL, TRUE),

('Money Market Account',
 'A high-yield savings account investing in short-term money market instruments, offering competitive returns with same-day liquidity.',
 0.00, 'Savings', 10000.00, 10000000.00, 7.25, NULL, TRUE),

('Gold Credit Card',
 'A premium credit card featuring travel rewards, comprehensive travel insurance, purchase protection, and exclusive lifestyle benefits.',
 500.00, 'Credit Card', 0.00, 50000.00, 22.50, NULL, TRUE),

('Platinum Credit Card',
 'An elite credit card offering unlimited airport lounge access, concierge services, higher credit limits, and accelerated rewards points on all purchases.',
 950.00, 'Credit Card', 0.00, 250000.00, 20.00, NULL, TRUE),

('Student Credit Card',
 'A low-fee credit card designed for students, offering cashback on everyday purchases, budgeting tools, and a low credit limit to encourage responsible spending.',
 0.00, 'Credit Card', 0.00, 5000.00, 18.00, NULL, TRUE),

('Business Credit Card',
 'A corporate credit card with expense management tools, higher credit limits, employee card options, and rewards tailored for business travel and procurement.',
 750.00, 'Credit Card', 0.00, 500000.00, 19.50, NULL, TRUE),

('Personal Loan',
 'A flexible personal loan with competitive fixed interest rates, customisable repayment terms, and no early settlement penalties.',
 0.00, 'Loan', 1000.00, 300000.00, 15.50, 84, TRUE),

('Vehicle Loan',
 'A secured vehicle finance loan with competitive interest rates, flexible repayment periods up to 72 months, and available for new or used vehicles.',
 0.00, 'Loan', 50000.00, 2000000.00, 11.75, 72, TRUE),

('Home Loan',
 'A long-term mortgage loan for purchasing residential property, offering competitive variable or fixed interest rates with repayment terms of up to 30 years.',
 0.00, 'Loan', 200000.00, 20000000.00, 11.25, 360, TRUE),

('Debt Consolidation Loan',
 'A single loan designed to consolidate multiple debts into one manageable monthly repayment, reducing overall interest costs and simplifying your finances.',
 0.00, 'Loan', 5000.00, 250000.00, 14.00, 72, TRUE),

('Business Loan',
 'A secured or unsecured business loan offering flexible repayment structures, designed to fund business growth, equipment, or working capital needs.',
 0.00, 'Loan', 50000.00, 10000000.00, 13.50, 120, TRUE),

('Student Loan',
 'An affordable education loan covering tuition, accommodation, and study materials, with repayments deferred until after graduation.',
 0.00, 'Loan', 10000.00, 500000.00, 10.50, 120, TRUE),

('Cheque Account',
 'A full-featured transactional account with a debit card, internet banking, unlimited swipes, and comprehensive overdraft facility.',
 150.00, 'Transactional', 0.00, NULL, 0.00, NULL, TRUE),

('Premium Current Account',
 'An all-inclusive transactional account with zero-rated transactions, free international transfers, dedicated relationship banker, and premium travel benefits.',
 599.00, 'Transactional', 0.00, NULL, 0.00, NULL, TRUE),

('Student Account',
 'A no-fee transactional account for full-time students, offering free swipes, free cash withdrawals, and access to student lifestyle rewards.',
 0.00, 'Transactional', 0.00, NULL, 0.00, NULL, TRUE),

('Business Current Account',
 'A robust business transactional account with cash management tools, bulk payment capabilities, and integration with major accounting platforms.',
 299.00, 'Transactional', 0.00, NULL, 0.00, NULL, TRUE),

('Life Cover',
 'A comprehensive life insurance policy providing a lump-sum payout to beneficiaries in the event of death, with optional disability and critical illness riders.',
 350.00, 'Insurance', 100000.00, 10000000.00, 0.00, NULL, TRUE),

('Short-Term Insurance',
 'A flexible short-term insurance product covering household contents, personal valuables, and vehicles against theft, damage, and natural disasters.',
 450.00, 'Insurance', 0.00, NULL, 0.00, NULL, TRUE);
