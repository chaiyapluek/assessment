CREATE TABLE lottery (
    id VARCHAR(6) PRIMARY KEY,
    amount INTEGER NOT NULL,
    price INTEGER NOT NULL
);

CREATE TABLE users (
    id VARCHAR(10) PRIMARY KEY
);

CREATE TABLE user_ticket (
    id SERIAL PRIMARY KEY,
    user_id VARCHAR(10) NOT NULL,
    ticket_id VARCHAR(6) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (ticket_id) REFERENCES lottery(id)
);