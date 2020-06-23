create table user
(
  id            int auto_increment
    primary key,
  account_id    varchar(100) null,
  name          varchar(50)  null,
  token         char(36)     null,
  create_time   bigint       null,
  modified_time bigint       null,
  avatar varchar(64)       null
);