# eecs3311project
Setting up local database
```
brew services start mysql 

mysql -u root

create database nhpi; 

use nhpi;

CREATE TABLE nhpi(year INT, month INT, city VARCHAR(500), province VARCHAR(150), value DECIMAL(5, 1));
```
