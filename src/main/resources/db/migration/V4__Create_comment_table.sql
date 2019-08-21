create table comment
(
  id bigint auto_increment,
  parent_id bigint not null,
  type int not null,
  content varchar(1024) null,
  commentator bigint null,
  create_time bigint null,
  modified_time bigint null,
  like_count bigint default 0 null,
  constraint comment_pk
    primary key (id)
);