use account_db;
delete from T_ACCOUNT;

use product_db;
delete from T_PRODUCT;

use user_db;
delete from T_USER;

use saga;
delete from command;
delete from tccfinishedevent;
delete from tccparticipateevent;
delete from txevent;
delete from txtimeout;