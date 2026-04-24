INSERT INTO users (email, password, name, address)
VALUES ('user@email.com', '{bcrypt}$2a$12$S3dr.iMmp1yyRvGvUCMBHOJiWPpuvwLgi2gBdwExi8IOLVhsFjd/u', 'user',
        '123 User St, City, Country'),
       ('admin@email.com', '{bcrypt}$2a$12$S3dr.iMmp1yyRvGvUCMBHOJiWPpuvwLgi2gBdwExi8IOLVhsFjd/u', 'admin',
        '456 Admin Rd, City, Country'),
       ('john.doe@email.com', '{bcrypt}$2a$12$S3dr.iMmp1yyRvGvUCMBHOJiWPpuvwLgi2gBdwExi8IOLVhsFjd/u', 'John Doe',
        '789 Maple Ave, City, Country'),
       ('jane.smith@email.com', '{bcrypt}$2a$12$S3dr.iMmp1yyRvGvUCMBHOJiWPpuvwLgi2#gBdwExi8IOLVhsFjd/u', 'Jane Smith',
        '321 Oak Ln, City, Country'),
       ('robert.brown@email.com', '{bcrypt}$2a$12$S3dr.iMmp1yyRvGvUCMBHOJiWPpuvwLgi2gBdwExi8IOLVhsFjd/u',
        'Robert Brown', '654 Pine Rd, City, Country'),
       ('emily.davis@email.com', '{bcrypt}$2a$12$S3dr.iMmp1yyRvGvUCMBHOJiWPpuvwLgi2gBdwExi8IOLVhsFjd/u', 'Emily Davis',
        '987 Cedar Dr, City, Country'),
       ('michael.wilson@email.com', '{bcrypt}$2a$12$S3dr.iMmp1yyRvGvUCMBHOJiWPpuvwLgi2gBdwExi8IOLVhsFjd/u',
        'Michael Wilson', '159 Birch Ct, City, Country');

INSERT INTO admins (user_id)
SELECT id
FROM users
WHERE email = 'admin@email.com';

INSERT INTO sellers (user_id, business_name, description, tax_id, contact_email)
SELECT id, 'Johns Store', 'Quality goods from John', 'TAX-JD-001', 'john.doe@email.com'
FROM users
WHERE email = 'john.doe@email.com';

INSERT INTO sellers (user_id, business_name, description, tax_id, contact_email)
SELECT id, 'Janes Boutique', 'Fashion for everyone', 'TAX-JS-002', 'jane.smith@email.com'
FROM users
WHERE email = 'jane.smith@email.com';

INSERT INTO sellers (user_id, business_name, description, tax_id, contact_email)
SELECT id, 'Roberts Tech', 'Electronics and Gadgets', 'TAX-RB-003', 'robert.brown@email.com'
FROM users
WHERE email = 'robert.brown@email.com';

INSERT INTO sellers (user_id, business_name, description, tax_id, contact_email)
SELECT id, 'Emilys Crafts', 'Handmade with love', 'TAX-ED-004', 'emily.davis@email.com'
FROM users
WHERE email = 'emily.davis@email.com';

INSERT INTO sellers (user_id, business_name, description, tax_id, contact_email)
SELECT id, 'Michaels Outdoors', 'Gear for the explorer', 'TAX-MW-005', 'michael.wilson@email.com'
FROM users
WHERE email = 'michael.wilson@email.com';

INSERT INTO categories (name)
VALUES ('Default Category');

-- Seed products for each seller
INSERT INTO products (name, description, price, stock_qty, seller_id, category_id)
SELECT 'Vintage Watch', 'A classic time piece', 55.00, 10, s.user_id, c.id
FROM sellers s,
     categories c
WHERE s.business_name = 'Johns Store'
  AND c.name = 'Default Category';

INSERT INTO products (name, description, price, stock_qty, seller_id, category_id)
SELECT 'Leather Wallet', 'Handmade leather wallet', 25.50, 8, s.user_id, c.id
FROM sellers s,
     categories c
WHERE s.business_name = 'Johns Store'
  AND c.name = 'Default Category';

INSERT INTO products (name, description, price, stock_qty, seller_id, category_id)
SELECT 'Silk Scarf', '100% pure silk scarf', 40.00, 15, s.user_id, c.id
FROM sellers s,
     categories c
WHERE s.business_name = 'Janes Boutique'
  AND c.name = 'Default Category';

INSERT INTO products (name, description, price, stock_qty, seller_id, category_id)
SELECT 'Evening Gown', 'Elegant evening wear', 120.00, 5, s.user_id, c.id
FROM sellers s,
     categories c
WHERE s.business_name = 'Janes Boutique'
  AND c.name = 'Default Category';

INSERT INTO products (name, description, price, stock_qty, seller_id, category_id)
SELECT 'Mechanical Keyboard', 'RGB backlit gaming keyboard', 85.00, 12, s.user_id, c.id
FROM sellers s,
     categories c
WHERE s.business_name = 'Roberts Tech'
  AND c.name = 'Default Category';

INSERT INTO products (name, description, price, stock_qty, seller_id, category_id)
SELECT 'USB-C Hub', '7-in-1 multi-port adapter', 35.00, 14, s.user_id, c.id
FROM sellers s,
     categories c
WHERE s.business_name = 'Roberts Tech'
  AND c.name = 'Default Category';

INSERT INTO products (name, description, price, stock_qty, seller_id, category_id)
SELECT 'Ceramic Mug', 'Hand-painted coffee mug', 18.00, 10, s.user_id, c.id
FROM sellers s,
     categories c
WHERE s.business_name = 'Emilys Crafts'
  AND c.name = 'Default Category';

INSERT INTO products (name, description, price, stock_qty, seller_id, category_id)
SELECT 'Knit Blanket', 'Chunky wool knit blanket', 75.00, 6, s.user_id, c.id
FROM sellers s,
     categories c
WHERE s.business_name = 'Emilys Crafts'
  AND c.name = 'Default Category';

INSERT INTO products (name, description, price, stock_qty, seller_id, category_id)
SELECT 'Camping Tent', '4-person waterproof tent', 150.00, 7, s.user_id, c.id
FROM sellers s,
     categories c
WHERE s.business_name = 'Michaels Outdoors'
  AND c.name = 'Default Category';

INSERT INTO products (name, description, price, stock_qty, seller_id, category_id)
SELECT 'Hiking Boots', 'Durable all-terrain boots', 95.00, 11, s.user_id, c.id
FROM sellers s,
     categories c
WHERE s.business_name = 'Michaels Outdoors'
  AND c.name = 'Default Category';
