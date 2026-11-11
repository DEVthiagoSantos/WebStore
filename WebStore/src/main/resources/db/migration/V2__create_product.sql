create table if not exists store.product (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    image VARCHAR(255),
    category_id BIGINT NOT NULL,

    CONSTRAINT fk_product_category
               FOREIGN KEY (category_id)
               REFERENCES store.category(id)
);