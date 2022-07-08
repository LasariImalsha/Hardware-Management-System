DROP DATABASE IF EXISTS Hardware;
CREATE DATABASE IF NOT EXISTS Hardware;
SHOW DATABASES ;
USE Hardware;
SHOW TABLES ;
#===================================================================================================
DROP TABLE IF EXISTS Customer;
CREATE TABLE IF NOT EXISTS Customer(
    customerId VARCHAR(8),
    name VARCHAR(20) NOT NULL DEFAULT 'Unknown',
    address VARCHAR(45),
    contact VARCHAR(20),
    CONSTRAINT PRIMARY KEY (customerId)
);
#===================================================================================================
DROP TABLE IF EXISTS `Order`;
CREATE TABLE IF NOT EXISTS `Order`(
    orderId VARCHAR(8),
    customerId VARCHAR(8),
    orderDate DATE,
    time VARCHAR(15),
    cost DECIMAL(8,2),
    CONSTRAINT PRIMARY KEY (orderId),
    CONSTRAINT FOREIGN KEY (customerId) REFERENCES Customer(customerId) ON DELETE CASCADE ON UPDATE CASCADE
);
#===================================================================================================
DROP TABLE IF EXISTS `Order Detail`;
CREATE TABLE IF NOT EXISTS `Order Detail`(
    orderId VARCHAR(8),
    itemCode VARCHAR(8),
    unitPrice DECIMAL(8,2),
    quantity INT NOT NULL DEFAULT 0,
    price DECIMAL(8,2),
    CONSTRAINT PRIMARY KEY (itemCode, orderid),
    CONSTRAINT FOREIGN KEY (itemCode) REFERENCES Item(itemCode) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY (orderid) REFERENCES `order`(orderid) ON DELETE CASCADE ON UPDATE CASCADE
);
#===================================================================================================
DROP TABLE IF EXISTS Item;
CREATE TABLE IF NOT EXISTS Item(
	itemCode VARCHAR(8),
    name VARCHAR(20) NOT NULL,
    description VARCHAR(50) NOT NULL DEFAULT 'Empty',
    preservedTime VARCHAR(30) NOT NULL,
    qtyOnHand INT NOT NULL DEFAULT 0,
    unitPrice DECIMAL(8,2) NOT NULL DEFAULT 0.00,
    unitSalePrice DECIMAL(8,2) NOT NULL DEFAULT 0.00,
    CONSTRAINT PRIMARY KEY (itemCode)
);
#===================================================================================================
DROP TABLE IF EXISTS Stock;
CREATE TABLE IF NOT EXISTS Stock(
    batchNo VARCHAR(8),
    supplierId VARCHAR(8),
    stockDate DATE,
    time VARCHAR(15),
    cost DECIMAL(8,2),
    CONSTRAINT PRIMARY KEY (batchNo),
    CONSTRAINT FOREIGN KEY (supplierId) REFERENCES Supplier(supplierId) ON DELETE CASCADE ON UPDATE CASCADE
);
#===================================================================================================
DROP TABLE IF EXISTS `Stock Detail`;
CREATE TABLE IF NOT EXISTS `Stock Detail`(
    batchNo VARCHAR (8),
    itemCode VARCHAR (8),
    unitPrice DECIMAL(8,2),
    quantity INT NOT NULL DEFAULT 0,
    price DECIMAL(8,2),
    CONSTRAINT PRIMARY KEY (batchNo,itemCode),
    CONSTRAINT FOREIGN KEY (itemCode) REFERENCES Item(itemCode) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY (batchNo) REFERENCES Stock(batchNo) ON DELETE CASCADE ON UPDATE CASCADE
);
#===================================================================================================
DROP TABLE IF EXISTS Supplier;
CREATE TABLE IF NOT EXISTS Supplier(
    supplierId VARCHAR(8),
    name VARCHAR(20) NOT NULL DEFAULT 'Unknown',
    address VARCHAR(45),
    contact VARCHAR(20),
    CONSTRAINT PRIMARY KEY (supplierId)
);
#===================================================================================================
DROP TABLE IF EXISTS `Price Detail`;
CREATE TABLE IF NOT EXISTS `Price Detail`(
    itemCode VARCHAR(8),
    name VARCHAR(20) NOT NULL,
    date DATE,
    time VARCHAR(15),
    unitPrice DECIMAL(8,2),
    unitSalePrice DECIMAL(8,2),
    CONSTRAINT PRIMARY KEY (itemCode, name, date, time)
);
#===================================================================================================
DROP TABLE IF EXISTS `Login Detail`;
CREATE TABLE IF NOT EXISTS `Login Detail`(
    userId VARCHAR(8),
    loginDate VARCHAR(15) DEFAULT '-',
    loginTime VARCHAR(15) DEFAULT '-',
    logoutDate VARCHAR(15) DEFAULT '-',
    logoutTime VARCHAR(15) DEFAULT '-'
);
#===================================================================================================
DROP TABLE IF EXISTS `User Detail`;
CREATE TABLE IF NOT EXISTS `User Detail`(
    userId VARCHAR(8),
    username VARCHAR(20),
    password VARCHAR(20),
    role VARCHAR(20),
    CONSTRAINT PRIMARY KEY (username)
);
#===================================================================================================
DROP TABLE IF EXISTS Employee;
CREATE TABLE IF NOT EXISTS Employee(
    employeeId VARCHAR(8),
    userId VARCHAR(8),
    name VARCHAR(20),
    address VARCHAR(45),
    contact VARCHAR(20),
    occupation VARCHAR(20),
    salary DECIMAL(8,2) DEFAULT 0.00,
    CONSTRAINT PRIMARY KEY (employeeId)
);
#===================================================================================================
SHOW TABLES ;
DESCRIBE Customer;
DESCRIBE `Order`;
DESCRIBE `Order Detail`;
DESCRIBE  Item;
DESCRIBE Stock;
DESCRIBE `Stock Detail`;
DESCRIBE Supplier;
DESCRIBE `User Detail`;
DESCRIBE `Price Detail`;
DESCRIBE `Login Detail`;
DESCRIBE Employee;
#===================================================================================================
SELECT * FROM Customer;
SELECT * FROM `Order`;
SELECT * FROM `Order Detail`;
SELECT * FROM Item;
SELECT * FROM Stock;
SELECT * FROM `Stock Detail`;
SELECT * FROM Supplier;
SELECT * FROM `User Detail`;
SELECT * FROM `Price Detail`;
SELECT * FROM `Login Detail`;
SELECT * FROM Employee;