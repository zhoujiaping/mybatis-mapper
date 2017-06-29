drop table if exists sys_user;
create table sys_user(
id serial8 primary key,
name varchar,
nick varchar,
password varchar
);
insert into sys_user(name,nick,password)values
('zhoujiaping','chengxuyuan','123456'),
('avril lavigne','pengkegongzhu','987654'),
('lene marlin','nuoweijingling','000000'),
('night with','fenlangejunvwang','258369'),
('eminem','god','qwerty');