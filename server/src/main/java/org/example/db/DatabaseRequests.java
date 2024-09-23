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
                        'SOUTH_KOREA'
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
                        eye_color color NOT NULL,
                        hair_color color NOT NULL,
                        nationality country NOT NULL
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
                        difficulty difficulty NOT NULL,
                        author_id INTEGER REFERENCES authors(id),
                        user_id INTEGER REFERENCES users(id)
                    );
                """;
    }

    public static String addUser() {
        return """
                INSERT INTO users(username, password, salt)
                VALUES (?, ?, ?)
                RETURNING id;
                """;
    }

    public static String checkIfAuthorExists() {
        return """
                SELECT id FROM authors
                   WHERE name = ? AND weight = ?
                        AND eye_color = ? AND hair_color = ?
                        AND nationality = ?;
                """;
    }

    public static String countLabWorks() {
        return """
                SELECT COUNT(*) FROM lab_works WHERE (user_id = ?);
                """;
    }

    public static String addAuthor() {
        return """
                INSERT INTO authors(name, weight, eye_color, hair_color, nationality)
                VALUES (?, ?, ?, ?, ?)
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

    public static String getLabWOrkById() {
        return """
                SELECT * FROM lab_works WHERE (id = ?);
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
                SELECT * FROM lab_works WHERE (user_id = ?);
                """;
    }

    public static String getAllLabWorksJoinAuthorIdByUserId() {
        return """
                SELECT * FROM lab_works
                JOIN authors ON lab_works.author_id = authors.id
                WHERE (lab_works.user_id = ?);
                """;
    }

    public static String deleteAllLabWorkByUserId() {
        return """
                DELETE FROM lab_works WHERE (user_id = ?) RETURNING user_id;
                """;
    }

    public static String deleteLabWorkById() {
        return """
                DELETE FROM lab_works WHERE id = ? AND user_id = ? RETURNING id;
                """;
    }

    public static String updateLabWorkByIdAndUserId() {
        return """
                UPDATE lab_works
                SET (name, coord_x, coord_y, creation_date, minimal_point, difficulty)
                 = (?, ?, ?, ?, ?, ?)
                WHERE id = ? AND user_id = ?
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