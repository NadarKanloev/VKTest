create table users
(
    id       bigint       not null
        primary key,
    email    varchar(255) not null
        constraint uk_6dotkott2kjsp8vw4d0m25fb7
            unique,
    password varchar(255) not null,
    role     varchar(255) not null
        constraint users_role_check
            check ((role)::text = ANY
        ((ARRAY ['ROLE_ADMIN'::character varying, 'ROLE_POSTS_VIEWER'::character varying, 'ROLE_POSTS_EDITOR'::character varying, 'ROLE_USER'::character varying, 'ROLE_ALBUMS'::character varying])::text[])),
    username varchar(255) not null
        constraint uk_r43af9ap4edm43mmtq01oddj6
            unique
);
INSERT INTO public.users (id, email, password, role, username) VALUES (1, 'admin@mail.ru', '$2a$10$ByKWvDh1ZhwYVgdlReen.ujBA0st9ISNJpRJi.kKLK/LgI2MHcY7i', 'ROLE_ADMIN', 'admin');
INSERT INTO public.users (id, email, password, role, username) VALUES (2, 'editor@mail.ru', '$2a$10$C/QynnA100MAfZpWu6juDu5BWXL7VrtZ9SUAf1GkRhW0/n5xwJ7f2', 'ROLE_POSTS_EDITOR', 'editor');
INSERT INTO public.users (id, email, password, role, username) VALUES (3, 'viewer@mail.ru', '$2a$10$E8B5qmMJU/BAh890iRi.3eiFNUerWTODfTigm8QOxCY9TPpOZp7zG', 'ROLE_POSTS_VIEWER', 'viewer');
INSERT INTO public.users (id, email, password, role, username) VALUES (4, 'user@mail.ru', '$2a$10$hsGT4WtIrxBZCR4/kYq0fuhxbocjSjJ25TIBvYgA59Vdq5jrUqz/S', 'ROLE_USER', 'user1');
INSERT INTO public.users (id, email, password, role, username) VALUES (5, 'Nadar@mail.ru', '$2a$10$TYnHgBeu4B.ltpRBSYKcD.TRGXJsde9ApUmYBkZKTb8O/J4xS.MMm', 'ROLE_USER', 'Nadar');
INSERT INTO public.users (id, email, password, role, username) VALUES (6, 'Album@mail.ru', '$2a$10$ML1sznGEKnSHSdSEuqCpo.TQFw5Bq/q9OASoCsLGuNfHm1/9lSAmC', 'ROLE_ALBUMS', 'Album');