CREATE TABLE IF NOT EXISTS links (
    short_link varchar(8) PRIMARY KEY CHECK (length(short_link) = 8),
    link varchar(100) NOT NULL,
    open_count INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
