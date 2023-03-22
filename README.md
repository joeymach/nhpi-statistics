# NHPI housing statistics
Setting up local database
```
brew services start mysql 

mysql -u root

create database nhpi; 

use nhpi;

CREATE TABLE nhpi(year INT, month INT, city VARCHAR(500), province VARCHAR(150), value DECIMAL(5, 1));
```

Then run Database/Main.java file to establish connection with database and populate database with csv data. 


### How nhpi_avg_date.csv was created
```
mysql -u root -B -e "use nhpi; SELECT year, month, AVG(value) from nhpi WHERE value > 0 group by year, month;" > nhpi_avg_date.csv  
```
