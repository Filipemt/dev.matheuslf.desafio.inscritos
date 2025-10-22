CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       CONSTRAINT uk_users_email UNIQUE (email)
);

CREATE TABLE project (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         description VARCHAR(255),
                         start_date DATE,
                         end_date DATE
);

CREATE TABLE task (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      title VARCHAR(255) NOT NULL,
                      description VARCHAR(255),
                      status VARCHAR(255),
                      priority VARCHAR(255),
                      due_date DATE,
                      project_id BIGINT,
                      CONSTRAINT fk_task_project FOREIGN KEY (project_id) REFERENCES project(id)
);

