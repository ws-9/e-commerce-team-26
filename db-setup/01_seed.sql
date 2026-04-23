-- Default admin: email=admin@example.com, password=Admin@123
INSERT INTO users (email, password, name, address, role)
VALUES ('admin@example.com',
        '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW',
        'Site Admin',
        '123 Admin Street',
        'ADMIN');

INSERT INTO admins (id, enabled)
VALUES (1, true);
