DROP TABLE CUSTOMERS,PRODUCTS,PURCHASES;

CREATE TABLE Customers
(
    persistence_id SERIAL PRIMARY KEY,
    first_name     VARCHAR(255) NOT NULL,
    last_name      VARCHAR(255) NOT NULL
);

CREATE TABLE Products
(
    persistence_id SERIAL PRIMARY KEY,
    product_name   VARCHAR(255)   NOT NULL,
    price          DECIMAL(10, 2) NOT NULL
);

CREATE TABLE Purchases
(
    persistence_id SERIAL PRIMARY KEY,
    customer_id    INT  NOT NULL,
    product_id     INT  NOT NULL,
    purchase_date  DATE NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES Customers (persistence_id),
    FOREIGN KEY (product_id) REFERENCES Products (persistence_id)
);

insert into Customers(persistence_id, first_name, last_name)
VALUES (1, 'Ivan', 'Petrov'),
       (2, 'Alex', 'Ivanov'),
       (3, 'Petr', 'Sidorov'),
       (4, 'Semen', 'Vladimirov'),
       (5, 'Anton', 'Semenov');

insert into Products(persistence_id, product_name, price)
values (1, 'Mineral water', 25.00),
       (2, 'Chocolate', 50.00),
       (3, 'Vodka', 200.00),
       (4, 'Cigarettes', 100.00),
       (5, 'Ice cream', 30.00),
       (6, 'Bread', 10.00),
       (7, 'Candy', 40.00);

insert into Purchases(persistence_id, customer_id, product_id, purchase_date)
VALUES (1, 1, 1, '2023-09-14'),
       (2, 2, 2, '2023-09-30'),
       (3, 3, 3, '2023-06-10'),
       (4, 4, 4, '2023-06-14'),
       (5, 5, 5, '2023-10-02'),
       (6, 1, 7, '2023-10-02'),
       (7, 2, 7, '2023-09-23'),
       (8, 3, 1, '2023-09-23'),
       (9, 4, 2, '2023-09-17'),
       (10, 5, 3, '2023-09-26'),
       (11, 1, 4, '2023-08-20'),
       (12, 2, 5, '2023-08-16'),
       (13, 3, 6, '2023-08-11'),
       (14, 4, 7, '2023-09-07'),
       (15, 5, 1, '2023-09-05'),
       (16, 1, 2, '2023-09-19');


