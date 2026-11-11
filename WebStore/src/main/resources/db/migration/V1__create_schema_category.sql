create schema if not exists store;

create table if not exists store.category (
     id BIGSERIAL PRIMARY KEY,
     name VARCHAR(100) NOT NULL UNIQUE
);