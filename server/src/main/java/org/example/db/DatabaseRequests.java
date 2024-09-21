package org.example.db;

public class DatabaseRequests {
    public static String createAllTables() {
        return """
                DO $$ BEGIN
                CREATE TYPE color AS ENUM(
                        'GREEN',
                        'BLACK',
                        'WHITE',
                        'RED',
                        'BLUE',
                        'ORANGE'
                );
                CREATE TYPE country AS ENUM(
                        'RUSSIA',
                        'USA',
                        'ITALY',
                        'THAILAND',
                        'SOUTH_KOREA',
                        );
                CREATE TYPE difficulty AS ENUM(
                        'VERY_HARD',
                        'IMPOSSIBLE',
                        'HOPELESS'
                );
                EXCEPTION
                WHEN duplicate_object THEN null;
                END $$;
                CREATE TABLE IF NOT EXISTS authors (
                        id SERIAL PRIMARY KEY,
                        name TEXT NOT NULL,
                        weight FLOAT NOT NULL,
                        eye_color COLOR NOT NULL,
                        hair_color COLOR NOT NULL,
                        nationality COUNTRY NOT NULL
                    );
                CREATE TABLE IF NOT EXISTS users (
                        id SERIAL PRIMARY KEY,
                        username TEXT,
                        password TEXT,
                        salt TEXT
                    );
                CREATE TABLE IF NOT EXISTS lab_works (
                        id SERIAL PRIMARY KEY,
                        name TEXT NOT NULL ,
                        coord_x INTEGER NOT NULL,
                        coord_y FLOAT NOT NULL ,
                        creation_date DATE NOT NULL ,
                        minimal_point INTEGER,
                        difficulty DIFFICULTY NOT NULL,
                        person_id REFERENCES people(id),
                        user_id REFERENCES users(id)
                    );
                """;
    }

    public static String addUser() {
        return """
                INSERT INTO users(login, password, salt)
                VALUES (?, ?, ?);
                RETURNING id;
                """;
    }

    public static String addAuthor() {
        return """
                INSERT INTO people(name, weight, height, eye_color, heir_color, nationality)
                VALUES (?, ?, ?, ?, ?, ?)
                RETURNING id;
                """;
    }

    public static String addLabWork() {
        return """
                INSERT INTO lab_works(name, coord_x, coord_y, creation_date, minimal_point, difficulty, author_id, user_id)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                RETURNING id;
                """;
    }

    public static String getUserIdByUsername() {
        return """
                SELECT id FROM users WHERE (username = ?);
                """;
    }

    public static String getUserByUsername() {
        return """
                SELECT * FROM users WHERE (username = ?);
                """;
    }

    public static String getAuthorIdByLabWorkId() {
        return """
                SELECT author_id FROM lab_works WHERE (id = ?);
                """;
    }

    public static String getAllLabWorks() {
        return """
                SELECT * FROM lab_works;
                """;
    }

    public static String getAllLabWorksJoinByAuthorId() {
        return """
                SELECT * FROM lab_works JOIN authors ON lab_works.author_id = authors.id;
                """;
    }

    public static String deleteLabWorkByUserId() {
        return """
                DELETE FROM lab_works WHERE (user_id = ?);
                """;
    }

    public static String updateLabWorkByIdAndUserId() {
        return """
                UPDATE lab_works
                SET (name, coord_x, coord_y, creation_date, minimal_point, difficulty)
                 = (?, ?, ?, ?, ?, ?)
                WHERE (id = ?) AND (user_id = ?)
                RETURNING id;
                """;
    }

    public static String updateAuthorById() {
        return """
                UPDATE authors
                SET (name, weight, eye_color, hair_color, nationality)
                 = (?, ?, ?, ?, ?)
                WHERE (id = ?)
                RETURNING id;
                """;
    }
}