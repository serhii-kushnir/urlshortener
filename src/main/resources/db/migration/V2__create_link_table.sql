CREATE TABLE links (
                       short_link varchar(8) PRIMARY KEY CHECK (length(short_link) = 8),
                       link VARCHAR(100) NOT NULL,
                       open_count INT NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       user_id INT NOT NULL,
                       FOREIGN KEY (user_id) REFERENCES users(id),
                       CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_user_id ON links (user_id);