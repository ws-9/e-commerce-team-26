CREATE TABLE "User" (
    userID SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address TEXT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE Admin (
    userID INT PRIMARY KEY,
    isEnabled BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (userID) REFERENCES "User"(userID) ON DELETE CASCADE
);

CREATE TABLE Seller (
    userID INT PRIMARY KEY,
    businessName VARCHAR(255) NOT NULL,
    description TEXT,
    isVerified BOOLEAN DEFAULT FALSE,
    taxID VARCHAR(100),
    contactEmail VARCHAR(255),
    FOREIGN KEY (userID) REFERENCES "User"(userID) ON DELETE CASCADE
);

CREATE TABLE Category (
    categoryID SERIAL PRIMARY KEY,
    categoryName VARCHAR(255) NOT NULL
);

CREATE TABLE Product (
    productID SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stockQty INT NOT NULL,
    sellerID INT,
    categoryID INT,
    FOREIGN KEY (sellerID) REFERENCES Seller(userID) ON DELETE SET NULL,
    FOREIGN KEY (categoryID) REFERENCES Category(categoryID) ON DELETE SET NULL
);

CREATE TABLE "Order" (
    orderID SERIAL PRIMARY KEY,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    shippingAddress TEXT,
    userID INT,
    FOREIGN KEY (userID) REFERENCES "User"(userID) ON DELETE SET NULL
);

CREATE TABLE OrderItem (
    orderItemID SERIAL PRIMARY KEY,
    qty INT NOT NULL,
    priceAtPurchase DECIMAL(10, 2) NOT NULL,
    orderID INT,
    productID INT,
    FOREIGN KEY (orderID) REFERENCES "Order"(orderID) ON DELETE CASCADE,
    FOREIGN KEY (productID) REFERENCES Product(productID) ON DELETE SET NULL
);
