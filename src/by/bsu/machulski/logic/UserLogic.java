package by.bsu.machulski.logic;

import by.bsu.machulski.dao.UserDAO;
import by.bsu.machulski.database.ConnectionPool;
import by.bsu.machulski.database.ProxyConnection;
import by.bsu.machulski.entity.User;
import by.bsu.machulski.exception.DAOException;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.type.RegisterStatus;
import by.bsu.machulski.util.UserValidator;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.EnumSet;
import java.util.Optional;

public class UserLogic {
    public Optional<User> identify(String email, String password) throws LogicException {
        Optional<User> user = Optional.empty();
        UserValidator validator = new UserValidator();
        if (validator.checkEmail(email) && validator.checkPassword(password)) {
            try  (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
                UserDAO userDAO = new UserDAO(connection);
                String passwordHash = DigestUtils.md5Hex(password);
                user = userDAO.findByEmailAndPassword(email, passwordHash);
            } catch (DAOException e) {
                throw new LogicException(e);
            }
        }
        return user;
    }

    public EnumSet<RegisterStatus> registerUser(String name, String email, String password, String confirmPassword)
            throws LogicException {
        EnumSet<RegisterStatus> result = EnumSet.noneOf(RegisterStatus.class);
        UserValidator validator = new UserValidator();
        if (!validator.checkUsername(name)) {
            result.add(RegisterStatus.INCORRECT_NAME);
        }
        if (!validator.checkEmail(email)) {
            result.add(RegisterStatus.INCORRECT_EMAIL);
        }
        if (!validator.checkPassword(password)) {
            result.add(RegisterStatus.INCORRECT_PASSWORD);
        }
        if (!validator.checkPasswordConformation(password, confirmPassword)) {
            result.add(RegisterStatus.MISMATCHED_PASSWORDS);
        }
        if (result.isEmpty()) {
            try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
                UserDAO userDAO = new UserDAO(connection);
                if (userDAO.findByEmail(email).isPresent()) {
                    result.add(RegisterStatus.EXISTING_EMAIL);
                }
                else {
                    String passwordHash = DigestUtils.md5Hex(password);
                    boolean isAdded = userDAO.add(name, email, passwordHash);
                    if (!isAdded) {
                        result.add(RegisterStatus.ERROR);
                    }
                }
            } catch (DAOException e) {
                throw new LogicException(e);
            }
        }
        return result;
    }
}
