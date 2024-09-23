package org.example.managers.db;

import org.example.db.DatabaseRequests;
import org.example.exceptions.collection.InvalidLabIdException;
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
    private static Connection connection;
    private static final MessageDigest MD;
    private static final String HASH_ALGORITHM;
    private static final String DATABASE_URL;
    private static final String PEPPER;
    private static final String DATABASE_USERNAME;
    private static final String DATABASE_PASSWORD;

    static {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("server\\src\\main\\resources\\application.properties"));
            PEPPER = properties.getProperty("PEPPER");
            HASH_ALGORITHM = properties.getProperty("HASH_ALGORITHM");
            DATABASE_URL = properties.getProperty("DATABASE_URL");
            DATABASE_USERNAME = properties.getProperty("DATABASE_USERNAME");
            DATABASE_PASSWORD = properties.getProperty("DATABASE_PASSWORD");
            MD = MessageDigest.getInstance(HASH_ALGORITHM);
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

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
            Console.printError(e.getSQLState());
            e.printStackTrace();
        }
    }

    public static boolean addUser(User user) {
        try {
            String username = user.getUsername();
            String SALT = generateRandomString();
            String password = PEPPER + user.getPassword() + SALT;

            PreparedStatement ps = connection.prepareStatement(DatabaseRequests.addUser());
            if (checkUserExists(username)) throw new UserAlreadyExistsSQLException();
            ps.setString(1, username);
            ps.setString(2, getSHAHash(password));
            ps.setString(3, SALT);
            ps.execute();

            return true;
        } catch (SQLException exception) {
            Console.printError(exception.getMessage());
            return false;
        }
    }

    public static boolean confirmUser(User user) {
        try {
            String username = user.getUsername();
            PreparedStatement ps = connection.prepareStatement(DatabaseRequests.getUserByUsername());
            ps.setString(1, username);

            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                String salt = resultSet.getString("salt");
                String toCheckPass = getSHAHash(PEPPER + user.getPassword() + salt);
                return toCheckPass.equals(resultSet.getString("password"));
            }
        } catch (SQLException exception) {
            Console.printError(exception.getMessage());
        }
        return false;
    }

    private static boolean checkUserExists(String username) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(DatabaseRequests.getUserByUsername());
        ps.setString(1, username);
        return ps.executeQuery().next();
    }

    private static void setAuthorPreparedStatement(PreparedStatement ps, Person author)
            throws SQLException {
        ps.setString(1, author.getName());
        ps.setFloat(2, author.getWeight());
        ps.setObject(3, author.getEyeColor(), Types.OTHER);
        ps.setObject(4, author.getHairColor(), Types.OTHER);
        ps.setObject(5, author.getNationality(), Types.OTHER);
    }

    private static int addAuthor(Person author) throws SQLException {
        PreparedStatement psCheck = connection.prepareStatement(DatabaseRequests.checkIfAuthorExists());
        setAuthorPreparedStatement(psCheck, author);
        ResultSet rsCheck = psCheck.executeQuery();
        if (rsCheck.next()) return rsCheck.getInt("id");

        PreparedStatement psAdd = connection.prepareStatement(DatabaseRequests.addAuthor());
        setAuthorPreparedStatement(psAdd, author);
        ResultSet rsAdd = psAdd.executeQuery();
        rsAdd.next();
        return rsAdd.getInt("id");
    }

    public static int getUserIdByUsername(User user) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(DatabaseRequests.getUserIdByUsername());
        ps.setString(1, user.getUsername());

        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt("id");
    }

    public static LabWork getLabWorkById(int id) throws SQLException, RuntimeException {
        PreparedStatement ps = connection.prepareStatement(DatabaseRequests.getLabWOrkById());
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        if (rs.next())
            return createLabWork(rs);
        throw new InvalidLabIdException();
    }

    public static void addLabWork(LabWork labWork, User user) throws RuntimeException {
        try {
            int authorId = addAuthor(labWork.getAuthor());
            int userId = getUserIdByUsername(user);

            PreparedStatement ps = connection.prepareStatement(DatabaseRequests.addLabWork());

            ps.setString(1, labWork.getName());
            ps.setInt(2, labWork.getCoordinates().getX());
            ps.setFloat(3, labWork.getCoordinates().getY());
            ps.setDate(4, java.sql.Date.valueOf(labWork.getCreationDate().toLocalDate()));
            ps.setInt(5, labWork.getMinimalPoint());
            ps.setObject(6, labWork.getDifficulty().getDifficultyName(), Types.OTHER);
            ps.setInt(7, authorId);
            ps.setInt(8, userId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) labWork.setId(rs.getInt("id"));
        } catch (SQLException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    private static void updateAuthorById(int id, Person author) {
        try {
            PreparedStatement ps = connection.prepareStatement(DatabaseRequests.updateAuthorById());

            ps.setString(1, author.getName());
            ps.setFloat(2, author.getWeight());
            ps.setObject(3, author.getEyeColor(), Types.OTHER);
            ps.setObject(4, author.getHairColor(), Types.OTHER);
            ps.setObject(5, author.getNationality(), Types.OTHER);
            ps.setInt(6, id);

            ResultSet rs = ps.executeQuery();
            rs.next();
        } catch (SQLException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    public static void updateLabWorkByIdAndUserId(long labWorkId, LabWork labWork, User user)
            throws RuntimeException {
        try {
            PreparedStatement ps = connection.prepareStatement(DatabaseRequests.updateLabWorkByIdAndUserId());
            int userId = getUserIdByUsername(user);
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

            ResultSet rs = ps.executeQuery();
            rs.next();
        } catch (SQLException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    private static int getAuthorIdByLabWorkId(long labWorkId) {
        try {
            PreparedStatement ps = connection.prepareStatement(DatabaseRequests.getAuthorIdByLabWorkId());
            ps.setLong(1, labWorkId);
            return ps.executeQuery().getInt(1);
        } catch (SQLException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    public static void deleteAllLabWorksByUserId(User user) throws RuntimeException {
        try {
            PreparedStatement ps = connection.prepareStatement(DatabaseRequests.deleteAllLabWorkByUserId());
            int userId = getUserIdByUsername(user);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            rs.next();
        } catch (SQLException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    public static void deleteLabWorkById(long id, User user) {
        try {
            PreparedStatement ps = connection.prepareStatement(DatabaseRequests.deleteLabWorkById());
            ps.setLong(1, id);
            ps.setLong(2, getUserIdByUsername(user));
            ps.executeQuery().next();
        } catch (SQLException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    public static ArrayDeque<LabWork> loadCollection(User user)
            throws SQLException {
        PreparedStatement ps = connection.prepareStatement(DatabaseRequests.getAllLabWorksJoinAuthorIdByUserId());

        int userId = getUserIdByUsername(user);
        ps.setInt(1, userId);

        ResultSet resultSet = ps.executeQuery();
        ArrayDeque<LabWork> collection = new ArrayDeque<>();

        while (resultSet.next()) collection.add(createLabWork(resultSet));

        return collection;
    }

    public static LabWork createLabWork(ResultSet resultSet) throws SQLException {
        Date date = resultSet.getDate("creation_date");
        LocalDateTime creationDate = Instant
                .ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        Difficulty difficulty = Difficulty.valueOf(resultSet.getString("difficulty"));
        Color eyeColor = Color.valueOf(resultSet.getString("eye_color"));
        Color hairColor = Color.valueOf(resultSet.getString("hair_color"));
        Country nationality = Country.valueOf(resultSet.getString("nationality"));

        return new LabWork(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                new Coordinates(
                        resultSet.getInt("coord_x"),
                        resultSet.getFloat("coord_y")
                ),
                creationDate,
                resultSet.getInt("minimal_point"),
                difficulty,
                new Person(
                        resultSet.getString("name"),
                        resultSet.getFloat("weight"),
                        eyeColor,
                        hairColor,
                        nationality
                ),
                resultSet.getInt("user_id")
        );
    }

    private static String generateRandomString() {
        Random random = new Random();
        int randomSize = PEPPER.length();
        StringBuilder sb = new StringBuilder(random.nextInt(randomSize));
        for (int i = 0; i < randomSize; i++) sb.append(PEPPER.charAt(random.nextInt(randomSize)));
        return sb.toString();
    }

    private static String getSHAHash(String input) {
        byte[] inputBytes = input.getBytes();
        MD.update(inputBytes);
        byte[] hashBytes = MD.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}