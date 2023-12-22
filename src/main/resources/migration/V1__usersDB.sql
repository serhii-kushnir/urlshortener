CREATE TABLE IF NOT EXISTS users (
                                     user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     name VARCHAR(100) NOT NULL,
                                     password_hash VARCHAR(8) NOT NULL
);

CREATE TABLE IF NOT EXISTS shortlink (
                                         linkid BIGINT PRIMARY KEY AUTO_INCREMENT,
                                         user_id BIGINT NOT NULL,
                                         originalurl VARCHAR(255) NOT NULL,
                                         shorturl VARCHAR(255) NOT NULL,
                                         startdate DATE NOT NULL,
                                         finaldate DATE NOT NULL,
                                         visit INT NOT NULL
);