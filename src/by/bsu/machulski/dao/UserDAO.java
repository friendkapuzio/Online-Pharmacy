package by.bsu.machulski.dao;

import by.bsu.machulski.database.ProxyConnection;
import by.bsu.machulski.entity.User;
import by.bsu.machulski.exception.DAOException;
import by.bsu.machulski.type.UserRole;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO extends AbstractDAO<User> {
    private static final Logger LOGGER = LogManager.getLogger(UserDAO.class);
    private static final int ID_INDEX = 1;
    private static final int NAME_INDEX = 2;
    private static final int EMAIL_INDEX = 3;
    private static final int PASSWORD_INDEX = 4;
    private static final int DATE_INDEX = 5;
    private static final int ROLE_INDEX = 6;
    private static final int BALANCE_INDEX = 7;
    private static final int IS_BLOCKED_INDEX = 8;
    private static final String USER_COLUMNS = "`user_id`, `name`, `email`, `password`, `create_date`, `role`, `balance`, `is_blocked`";
    private static final String FIND_BY_EMAIL =
            "SELECT " + USER_COLUMNS + " FROM `pharmacy`.`users` WHERE `is_blocked`=? AND `email` LIKE ?;";
    private static final String MODIFY_BALANCE = "UPDATE `pharmacy`.`users` SET `balance`=`balance`+? WHERE `user_id`=?;";
    private static final String SELECT_ALL = "SELECT " + USER_COLUMNS + " FROM `pharmacy`.`users`;";
    private static final String SELECT_BY_ID = "SELECT " + USER_COLUMNS + " FROM `pharmacy`.`users` WHERE `user_id`=?;";
    private static final String SELECT_BY_EMAIL = "SELECT " + USER_COLUMNS + " FROM `pharmacy`.`users` WHERE `email`=?;";
    private static final String SELECT_BY_EMAIL_AND_PASSWORD = "SELECT " + USER_COLUMNS + " FROM `pharmacy`.`users` WHERE `email`=? AND `password`=?;";
    private static final String SELECT_BY_ID_AND_PASSWORD = "SELECT " + USER_COLUMNS + " FROM `pharmacy`.`users` WHERE `user_id`=? AND `password`=?;";
    private static final String INSERT_USER = "INSERT INTO `pharmacy`.`users` (`name`, `email`, `password`, `create_date`) VALUES (?, ?, ?, ?);";
    private static final String UPDATE_EMAIL = "UPDATE `pharmacy`.`users` SET `email`=? WHERE `user_id`=?;";
    private static final String UPDATE_IS_BLOCKED = "UPDATE `pharmacy`.`users` SET `is_blocked`=? WHERE `user_id`=?;";
    private static final String UPDATE_NAME = "UPDATE `pharmacy`.`users` SET `name`=? WHERE `user_id`=?;";
    private static final String UPDATE_PASSWORD = "UPDATE `pharmacy`.`users` SET `password`=? WHERE `user_id`=?;";
    private static final String UPDATE_ROLE = "UPDATE `pharmacy`.`users` SET `role`=? WHERE `user_id`=?;";

    public UserDAO(ProxyConnection connection) {
        super(connection);
    }

    public boolean add(String name, String email, String password) throws DAOException {
        boolean isAdded = false;
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER)){
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.setDate(4, Date.valueOf(LocalDate.now()));
            if (preparedStatement.executeUpdate() == 1) {
                isAdded = true;
            } else {
                LOGGER.log(Level.WARN, "Failure to add user");
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to add user", e);
        }
        return isAdded;
    }

    public boolean changeEmail(long id, String newEmail) throws DAOException {
        boolean isChanged = false;
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_EMAIL)) {
            preparedStatement.setString(1, newEmail);
            preparedStatement.setLong(2, id);
            if (preparedStatement.executeUpdate() == 1) {
                isChanged = true;
            } else {
                LOGGER.log(Level.WARN, "Failure to change email, id: " + id);
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to change email, id: " + id, e);
        }
        return isChanged;
    }

    public boolean changeIsBlocked(long id, boolean isBlocked) throws DAOException {
        boolean isChanged = false;
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_IS_BLOCKED)) {
            preparedStatement.setBoolean(1, isBlocked);
            preparedStatement.setLong(2, id);
            if (preparedStatement.executeUpdate() == 1) {
                isChanged = true;
            } else {
                LOGGER.log(Level.WARN, "Failure to change isBlocked status, id: " + id);
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to change isBlocked status, id: " + id, e);
        }
        return isChanged;
    }

    public boolean changeName(long id, String newName) throws DAOException {
        boolean isChanged = false;
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_NAME)) {
            preparedStatement.setString(1, newName);
            preparedStatement.setLong(2, id);
            if (preparedStatement.executeUpdate() == 1) {
                isChanged = true;
            } else {
                LOGGER.log(Level.WARN, "Failure to change username, id: " + id);
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to change username, id: " + id, e);
        }
        return isChanged;
    }

    public boolean changePassword(long id, String newPassword) throws DAOException {
        boolean isChanged = false;
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PASSWORD)) {
            preparedStatement.setString(1, newPassword);
            preparedStatement.setLong(2, id);
            if (preparedStatement.executeUpdate() == 1) {
                isChanged = true;
            } else {
                LOGGER.log(Level.WARN, "Failure to change user's password, id: " + id);
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to change user's password, id: " + id, e);
        }
        return isChanged;
    }

    public boolean changeRole(long id, String newRole) throws DAOException {
        boolean isChanged = false;
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ROLE)) {
            preparedStatement.setString(1, newRole);
            preparedStatement.setLong(2, id);
            if (preparedStatement.executeUpdate() == 1) {
                isChanged = true;
            } else {
                LOGGER.log(Level.WARN, "Failure to change user's role, id: " + id);
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to change user's role, id: " + id, e);
        }
        return isChanged;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    public List<User> findBlockedUsersByEmail(String searchText) throws DAOException {
        return findUsersByEmail(searchText, true);
    }

    public List<User> findUsersByEmail(String searchText) throws DAOException {
        return findUsersByEmail(searchText, false);
    }

    @Override
    public Optional<User> findById(long id) throws DAOException {
        User user = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                user = createUser(rs);
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to find user by id, id: " + id, e);
        }
        return Optional.ofNullable(user);
    }

    public Optional<User> findByEmail(String email) throws DAOException {
        User user = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_EMAIL)) {
            preparedStatement.setString(1, email);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                user = createUser(rs);
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to find user by email, email: " + email, e);
        }
        return Optional.ofNullable(user);
    }

    public Optional<User> findByEmailAndPassword(String email, String password) throws DAOException {
        User user = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_EMAIL_AND_PASSWORD)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                user = createUser(rs);
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to find user by email and password, email: " + email, e);
        }
        return Optional.ofNullable(user);
    }

    public Optional<User> findByIdAndPassword(long id, String password) throws DAOException {
        User user = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID_AND_PASSWORD)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                user = createUser(rs);
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to find user by id and password, id " + id, e);
        }
        return Optional.ofNullable(user);
    }

    public boolean modifyBalance(long id, BigDecimal amount) throws DAOException {
        boolean isAdded = false;
        try (PreparedStatement preparedStatement = connection.prepareStatement(MODIFY_BALANCE)) {
            preparedStatement.setBigDecimal(1, amount);
            preparedStatement.setLong(2, id);
            if (preparedStatement.executeUpdate() == 1) {
                isAdded = true;
            } else {
                LOGGER.log(Level.WARN, "Failure to update user's balance, id: " + id);
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to update user's balance, id: " + id, e);
        }
        return isAdded;
    }

    private User createUser(ResultSet rs) throws SQLException {
        long id = rs.getLong(ID_INDEX);
        String name = rs.getString(NAME_INDEX);
        String email = rs.getString(EMAIL_INDEX);
        String password = rs.getString(PASSWORD_INDEX);
        Date registrationDate = rs.getDate(DATE_INDEX);
        UserRole role = UserRole.valueOf(rs.getString(ROLE_INDEX).toUpperCase());
        BigDecimal balance = rs.getBigDecimal(BALANCE_INDEX);
        boolean isBlocked = rs.getBoolean(IS_BLOCKED_INDEX);
        return new User(id, name, email, password, registrationDate, role, balance, isBlocked);
    }

    private List<User> findUsersByEmail(String searchText, boolean isBlocked) throws DAOException {
        List<User> users = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_EMAIL)) {
            preparedStatement.setBoolean(1, isBlocked);
            preparedStatement.setString(2, "%" + searchText + "%");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                users.add(createUser(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to find users by email, email: " + searchText, e);
        }
        return users;
    }
}
