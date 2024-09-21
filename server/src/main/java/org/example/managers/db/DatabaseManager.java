package org.example.managers.db;

import org.example.db.DatabaseRequests;
import org.example.exceptions.db.UserAlreadyExistsSQLException;
import org.example.model.data.*;
import org.example.network.dto.User;
import org.example.utils.io.console.Console;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayDeque;
import java.util.Properties;
import java.util.Random;

public class DatabaseManager {
    private Connection connection;
    private static final MessageDigest MD;
    private static final String HASH_ALGORITHM;
    private static final String DATABASE_URL;
    private static final String PEPPER;
    private static final String DATABASE_USERNAME;
    private static final String DATABASE_PASSWORD;
    private static String SALT;

    static {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("client\\src\\main\\resources\\application.properties"));
            PEPPER = properties.getProperty("PEPPER");
            HASH_ALGORITHM = properties.getProperty("HASH_ALGORITHM");
            DATABASE_URL = properties.getProperty("DATABASE_URL");
            DATABASE_USERNAME = properties.getProperty("DATABASE_USERNAME");
            DATABASE_PASSWORD = properties.getProperty("DATABASE_PASSWORD");
            MD = MessageDigest.getInstance(HASH_ALGORITHM);
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    {
        try {
            connection = DriverManager.getConnection(
                    DATABASE_URL,
                    DATABASE_USERNAME,
                    DATABASE_PASSWORD
            );

            connection
                    .prepareStatement(DatabaseRequests.createAllTables())
                    .execute();
        } catch (SQLException e) {
            Console.printError(e.getMessage());
        }
    }

    public void addUser(User user) throws SQLException {
        String username = user.getUsername();
        SALT = this.generateRandomString();
        String password = PEPPER + user.getPassword() + SALT;

        PreparedStatement ps = connection.prepareStatement(DatabaseRequests.addUser());
        if (checkUserExists(username)) throw new UserAlreadyExistsSQLException();
        ps.setString(1, username);
        ps.setString(2, getSHAHash(password));
        ps.setString(3, SALT);
        ps.execute();
    }

    public boolean confirmUser(User user) throws SQLException {
        String username = user.getUsername();
        PreparedStatement getUser = connection.prepareStatement(DatabaseRequests.getUserByUsername());
        getUser.setString(1, username);

        ResultSet resultSet = getUser.executeQuery();
        if (resultSet.next()) {
            String salt = resultSet.getString("salt");
            String toCheckPass = getSHAHash(PEPPER + user.getPassword() + salt);
            return toCheckPass.equals(resultSet.getString("password"));
        }
        return false;
    }

    private boolean checkUserExists(String username) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(DatabaseRequests.getUserByUsername());
        ps.setString(1, username);
        return ps.executeQuery().next();
    }

    private int addAuthor(Person author) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(DatabaseRequests.addAuthor());

        ps.setString(1, author.getName());
        ps.setFloat(2, author.getWeight());
        ps.setObject(3, author.getEyeColor(), Types.OTHER);
        ps.setObject(4, author.getHairColor(), Types.OTHER);
        ps.setObject(5, author.getNationality(), Types.OTHER);

        return ps.executeQuery().getInt(1);
    }

    private int getUserIdByUsername(String username) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(DatabaseRequests.getUserIdByUsername());
        ps.setString(1, username);
        return ps.executeQuery().getInt("id");
    }

    /**
     * Метод добавляет лабораторную работу в базу данных
     *
     * @param labWork лабораторная работа
     * @param user    активный пользователь
     * @return возвращает id добавленной лабораторной работы
     */
    public int addLabWork(LabWork labWork, User user) throws SQLException {
        int authorId = addAuthor(labWork.getAuthor());
        int userId = labWork.getUserId();

        PreparedStatement ps = connection.prepareStatement(DatabaseRequests.addLabWork());

        ps.setString(1, labWork.getName());
        ps.setInt(2, labWork.getCoordinates().getX());
        ps.setFloat(3, labWork.getCoordinates().getY());
        ps.setDate(4, java.sql.Date.valueOf(labWork.getCreationDate().toLocalDate()));
        ps.setInt(5, labWork.getMinimalPoint());
        ps.setObject(6, labWork.getDifficulty(), Types.OTHER);
        ps.setInt(7, authorId);
        ps.setInt(8, userId);

        return ps.executeQuery().getInt("id");
    }

    private void updateAuthorById(int id, Person author) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(DatabaseRequests.updateLabWorkByIdAndUserId());

        ps.setString(1, author.getName());
        ps.setFloat(2, author.getWeight());
        ps.setObject(3, author.getEyeColor(), Types.OTHER);
        ps.setObject(4, author.getHairColor(), Types.OTHER);
        ps.setObject(5, author.getNationality(), Types.OTHER);

        ps.executeQuery();
    }

    public boolean updateLabWorkByIdAndUserId(long labWorkId, LabWork labWork, User user) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(DatabaseRequests.updateLabWorkByIdAndUserId());
        int userId = labWork.getUserId();
        int authorId = getAuthorIdByLabWorkId(labWorkId);

        updateAuthorById(authorId, labWork.getAuthor());

        ps.setString(1, labWork.getName());
        ps.setInt(2, labWork.getCoordinates().getX());
        ps.setFloat(3, labWork.getCoordinates().getY());
        ps.setDate(4, java.sql.Date.valueOf(labWork.getCreationDate().toLocalDate()));
        ps.setInt(5, labWork.getMinimalPoint());
        ps.setObject(6, labWork.getDifficulty(), Types.OTHER);
        ps.setLong(7, labWorkId);
        ps.setLong(8, userId);

        return ps.executeQuery().next();
    }

    private int getAuthorIdByLabWorkId(long labWorkId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(DatabaseRequests.getAuthorIdByLabWorkId());
        ps.setLong(1, labWorkId);
        return ps.executeQuery().getInt(1);
    }

    public boolean deleteAllLabWorksByUserId(User user) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(DatabaseRequests.deleteLabWorkByUserId());
        int userId = getUserIdByUsername(user.getUsername());
        ps.setInt(1, userId);
        return ps.executeQuery().next();
    }

    public ArrayDeque<LabWork> loadCollection() throws SQLException {
        PreparedStatement ps = connection.prepareStatement(DatabaseRequests.getAllLabWorksJoinByAuthorId());
        ResultSet resultSet = ps.executeQuery();
        ArrayDeque<LabWork> collection = new ArrayDeque<>();

        while (resultSet.next()) {
            Date date = resultSet.getDate("creation_date");
            LocalDateTime creationDate = Instant.ofEpochMilli(date.getTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            collection.add(new LabWork(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    new Coordinates(
                            resultSet.getInt("coord_x"),
                            resultSet.getFloat("coord_y")
                    ),
                    creationDate,
                    resultSet.getInt("minimal_point"),
                    resultSet.getObject("difficulty", Difficulty.class),
                    new Person(
                            resultSet.getString("name"),
                            resultSet.getFloat("weight"),
                            resultSet.getObject("eye_color", Color.class),
                            resultSet.getObject("hair_color", Color.class),
                            resultSet.getObject("nationality", Country.class)
                    ),
                    resultSet.getInt("user_id")
            ));
        }

        return collection;
    }

    private String generateRandomString() {
        Random random = new Random();
        int randomSize = SALT.length();
        StringBuilder sb = new StringBuilder(random.nextInt(randomSize));
        for (int i = 0; i < randomSize; i++) sb.append(SALT.charAt(random.nextInt(randomSize)));
        return sb.toString();
    }

    private String getSHAHash(String input) {
        byte[] inputBytes = input.getBytes();
        MD.update(inputBytes);
        byte[] hashBytes = MD.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}