create table if not exists product.user_products
(
    id serial primary key,
    product_id int not null,
    customer_id int not null
);