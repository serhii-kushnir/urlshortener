CREATE TYPE user_role AS ENUM ('ADMIN', 'USER', 'GUEST');

CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       user_name VARCHAR(100) NOT NULL,
                       password VARCHAR(100) CHECK (password ~ '^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$') NOT NULL,
                       email VARCHAR(100) NOT NULL CHECK (email ~ '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$'),
                       role user_role NOT NULL,
                       enabled BOOLEAN NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
