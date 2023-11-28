insert into user_entity values (
    1, 'test@gmail.com', '$2a$10$3rHtBb6evmxvzwdvuQdEl.9PwDPkfJbT55D7/SX27wFhxsMx1EZk2', 'ADMIN', 'test'
);

insert into user_entity values (
    2, 'user@gmail.com', '$2a$10$SufWK0ICXCtCzugc5L0zXe52Bzc9Gz5bmjtXR2j3dIhuRdP9Xakt6', 'USER', 'user'
);

select * from user_entity;