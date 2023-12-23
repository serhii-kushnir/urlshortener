CREATE TABLE IF NOT EXISTS users (
                                     user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     name VARCHAR(100) NOT NULL,
                                     password_hash VARCHAR(8) NOT NULL
);

CREATE TABLE IF NOT EXISTS shortlink (
                                         linkid BIGINT PRIMARY KEY AUTO_INCREMENT,
                                         user_id BIGINT,
                                         originalurl VARCHAR(255) NOT NULL,
                                         tokenurl VARCHAR(255) NOT NULL,
                                         shorturl VARCHAR(255) NOT NULL,
                                         startdate DATETIME NOT NULL,
                                         finaldate DATETIME NOT NULL,
                                         visit INT NOT NULL,
                                         FOREIGN KEY (user_id) REFERENCES users(user_id)
);