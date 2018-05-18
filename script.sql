-- we don't know how to generate database BUSINESS (class Database) :(
create table Customers
(
  customerNumber         int         not null
    primary key,
  customerName           varchar(50) not null,
  contactLastName        varchar(50),
  contactFirstName       varchar(50),
  phone                  varchar(50) not null,
  addressLine1           varchar(50),
  addressLine2           varchar(50),
  city                   varchar(50),
  state                  varchar(50),
  postalCode             varchar(15),
  country                varchar(50),
  salesRepEmployeeNumber int,
  creditLimit            float       not null
)
go

create unique index Customers_customerNumber_uindex
  on Customers (customerNumber)
go

create table Orders
(
  orderNumber    int         not null
    primary key,
  orderDate      datetime    not null,
  requiredDate   datetime    not null,
  shippedDate    datetime    not null,
  status         varchar(15) not null,
  comments       text        not null,
  customerNumber int         not null
    constraint Orders_Customers_customerNumber_fk
    references Customers
)
go

create unique index Orders_orderNumber_uindex
  on Orders (orderNumber)
go

create table Payments
(
  customerNumber int
    constraint PAYMENTS_Customers_customerNumber_fk
    references Customers,
  checkNumber    varchar(50) not null
    primary key,
  paymentDate    datetime,
  amount         float
)
go

create unique index PAYMENTS_checkNumber_uindex
  on Payments (checkNumber)
go

create table ProductLines
(
  productLine     varchar(50) not null
    primary key,
  textDescription text        not null,
  htmlDescription text        not null,
  image           text        not null
)
go

create table Products
(
  productCode        varchar(15) not null
    primary key,
  productName        varchar(70),
  productLine        varchar(50)
    constraint Products_ProductLines_productLine_fk
    references ProductLines,
  productScale       varchar(10),
  productVendor      varchar(50),
  productDescription text,
  quantityInStock    int,
  buyPrice           float,
  MSRP               float
)
go

create unique index Products_productCode_uindex
  on Products (productCode)
go

create table OrderDetails
(
  orderNumber     int         not null
    constraint OrderDetails_orderNumber_pk
    primary key
    constraint OrderDetails_Orders_orderNumber_fk
    references Orders,
  productCode     varchar(15) not null
    constraint OrderDetails_Products_productCode_fk
    references Products,
  quantityOrdered int         not null,
  priceEach       float       not null,
  orderLineNumber smallint
)
go

create unique index OrderDetails_orderNumber_uindex
  on OrderDetails (orderNumber)
go

create unique index table_name_productLine_uindex
  on ProductLines (productLine)
go

create table Offices
(
  officeCode   varchar(10) not null
    primary key,
  city         varchar(50) not null,
  phone        varchar(50) not null,
  addressLine1 varchar(50) not null,
  addressLine2 varchar(50) not null,
  state        varchar(50) not null,
  country      varchar(50) not null,
  postalCode   varchar(15) not null,
  territory    varchar(10) not null
)
go

create table Employees
(
  employeeNumber int          not null
    primary key,
  lastName       varchar(50)  not null,
  firstName      varchar(50)  not null,
  extension      varchar(10),
  email          varchar(100) not null,
  officeCode     varchar(10)  not null
    constraint Employees_Offices_officeCode_fk
    references Offices,
  reportsTo      int          not null,
  jobTitle       varchar(50)  not null
)
go

create unique index Employees_employeeNumber_uindex
  on Employees (employeeNumber)
go

create unique index Offices_officeCode_uindex
  on Offices (officeCode)
go


