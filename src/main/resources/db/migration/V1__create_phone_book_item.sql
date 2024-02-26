create table phone_book_item
(
    id              BIGINT       NOT NULL auto_increment,
    version         BIGINT       NOT NULL,
    name            VARCHAR(255),
    family_name     VARCHAR(255),
    phone           VARCHAR(255),
    primary key (id)
);
