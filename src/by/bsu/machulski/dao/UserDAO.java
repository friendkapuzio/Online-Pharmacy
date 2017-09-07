package by.bsu.machulski.dao;

import by.bsu.machulski.database.ProxyConnection;
import by.bsu.machulski.entity.User;
import by.bsu.machulski.exception.DAOException;
import by.bsu.machulski.type.UserRole;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class UserDAO extends AbstractDAO<User> {
    private static final int ID_INDEX = 1;
    private static final int NAME_INDEX = 2;
    private static final int EMAIL_INDEX = 3;
    private static final int PASSWORD_INDEX = 4;
    private static final int DATE_INDEX = 5;
    private static final int ROLE_INDEX = 6;
    private static final int BALANCE_INDEX = 7;
    private static final int IS_BLOCKED_INDEX = 8;
    private static final String USER_COLUMNS = "`user_id`, `name`, `email`, `password`, `create_date`, `role`, `balance`, `is_blocked`";
    private static final String SELECT_ALL = "SELECT " + USER_COLUMNS + " FROM `pharmacy`.`users`;";
    private static final String SELECT_BY_ID = "SELECT " + USER_COLUMNS + " FROM `pharmacy`.`users` WHERE `user_id`=?;";
    private static final String SELECT_BY_EMAIL = "SELECT " + USER_COLUMNS + " FROM `pharmacy`.`users` WHERE `email`=?;";
    private static final String SELECT_BY_EMAIL_AND_PASSWROD = "SELECT " + USER_COLUMNS + " FROM `pharmacy`.`users` WHERE `email`=? AND `password`=?;";
    private static final String INSERT_USER = "INSERT INTO `pharmacy`.`users` (`name`, `email`, `password`, `create_date`) VALUES (?, ?, ?, ?);";

    public UserDAO(ProxyConnection connection) {
        super(connection);
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public Optional<User> findById(long id) {
        return null;
    }

    public Optional<User> findByEmail(String keyEmail) throws DAOException {
        User user = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_EMAIL)) {
            preparedStatement.setString(1, keyEmail);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                user = createUser(rs);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return Optional.ofNullable(user);
    }

    public Optional<User> findByEmailAndPassword(String email, String password) throws DAOException {
        User user = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_EMAIL_AND_PASSWROD)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                user = createUser(rs);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return Optional.ofNullable(user);
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
            }
        } catch (SQLException e) {
            throw new DAOException(e);
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
}
