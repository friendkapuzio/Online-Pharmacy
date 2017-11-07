package by.bsu.machulski.logic;

import by.bsu.machulski.dao.TransactionDAO;
import by.bsu.machulski.dao.UserDAO;
import by.bsu.machulski.database.ConnectionPool;
import by.bsu.machulski.database.ProxyConnection;
import by.bsu.machulski.entity.User;
import by.bsu.machulski.exception.DAOException;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.type.UserOperationError;
import by.bsu.machulski.validator.CardValidator;
import by.bsu.machulski.validator.UserValidator;
import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class UserLogic {
    public boolean block(String id) throws LogicException {
        try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
            return new UserDAO(connection).changeIsBlocked(Long.parseLong(id), true);
        } catch (DAOException e) {
            throw new LogicException("Failure to block user, id: " + id, e);
        }
    }

    public EnumSet<UserOperationError> changeEmail(long id, String newEmail) throws LogicException {
        EnumSet<UserOperationError> errors = EnumSet.noneOf(UserOperationError.class);
        UserValidator userValidator = new UserValidator();
        if (!userValidator.checkEmail(newEmail)) {
            errors.add(UserOperationError.INCORRECT_EMAIL);
        }
        if (errors.isEmpty()) {
            try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
                UserDAO userDAO = new UserDAO(connection);
                if (userDAO.findByEmail(newEmail).isPresent()) {
                    errors.add(UserOperationError.EXISTING_EMAIL);
                } else {
                    boolean isChanged = userDAO.changeEmail(id, newEmail);
                    if (!isChanged) {
                        errors.add(UserOperationError.ERROR);
                    }
                }
            } catch (DAOException e) {
                throw new LogicException("Failure to change user's email, id: " + id, e);
            }
        }
        return errors;
    }

    public EnumSet<UserOperationError> changeName(long id, String newName) throws LogicException {
        EnumSet<UserOperationError> errors = EnumSet.noneOf(UserOperationError.class);
        UserValidator userValidator = new UserValidator();
        if (!userValidator.checkUsername(newName)) {
            errors.add(UserOperationError.INCORRECT_NAME);
        }
        if (errors.isEmpty()) {
            try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
                UserDAO userDAO = new UserDAO(connection);
                boolean isChanged = userDAO.changeName(id, newName);
                if (!isChanged) {
                    errors.add(UserOperationError.ERROR);
                }
            } catch (DAOException e) {
                throw new LogicException("Failure to change user's name, id: " + id, e);
            }
        }
        return errors;
    }

    public EnumSet<UserOperationError> changePassword(long id, String currentPassword,
                                                      String newPassword, String confirmNewPassword) throws LogicException {
        EnumSet<UserOperationError> errors = EnumSet.noneOf(UserOperationError.class);
        UserValidator userValidator = new UserValidator();
        if (!userValidator.checkPassword(currentPassword)) {
            errors.add(UserOperationError.INCORRECT_PASSWORD);
        }
        if (!userValidator.checkPassword(newPassword)) {
            errors.add(UserOperationError.INCORRECT_NEW_PASSWORD);
        }
        if (!userValidator.checkPasswordConformation(newPassword, confirmNewPassword)) {
            errors.add(UserOperationError.MISMATCHED_PASSWORDS);
        }
        if (errors.isEmpty()) {
            try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
                String passwordHash = DigestUtils.md5Hex(currentPassword);
                UserDAO userDAO = new UserDAO(connection);
                if (!userDAO.findByIdAndPassword(id, passwordHash).isPresent()) {
                    errors.add(UserOperationError.WRONG_PASSWORD);
                } else {
                    String newPasswordHash = DigestUtils.md5Hex(newPassword);
                    boolean isChanged = userDAO.changePassword(id, newPasswordHash);
                    if (!isChanged) {
                        errors.add(UserOperationError.ERROR);
                    }
                }
            } catch (DAOException e) {
                throw new LogicException("Failure to change user's password, id: " + id, e);
            }
        }
        return errors;
    }

    public boolean changeRole(String id, String newRole) throws LogicException {
        try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
            return new UserDAO(connection).changeRole(Long.parseLong(id), newRole);
        } catch (DAOException e) {
            throw new LogicException("Failure to change user's role, id: " + id, e);
        }
    }

    public List<User> findBlockedUsersByEmail(String searchText) throws LogicException {
        try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
            return new UserDAO(connection).findBlockedUsersByEmail(searchText);
        } catch (DAOException e) {
            throw new LogicException("Failure to find blocked users, request text: " + searchText, e);
        }
    }

    public Optional<User> findById(long id) throws LogicException {
        try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
            return new UserDAO(connection).findById(id);
        } catch (DAOException e) {
            throw new LogicException("Failure to find user by id, id: " + id, e);
        }
    }

    public List<User> findUsersByEmail(String searchText) throws LogicException {
        try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
            return new UserDAO(connection).findUsersByEmail(searchText);
        } catch (DAOException e) {
            throw new LogicException("Failure to find users, request text: " + searchText, e);
        }
    }

    public Optional<User> identify(String email, String password) throws LogicException {
        Optional<User> user = Optional.empty();
        UserValidator userValidator = new UserValidator();
        if (userValidator.checkEmail(email) && userValidator.checkPassword(password)) {
            try  (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
                UserDAO userDAO = new UserDAO(connection);
                String passwordHash = DigestUtils.md5Hex(password);
                user = userDAO.findByEmailAndPassword(email, passwordHash);
            } catch (DAOException e) {
                throw new LogicException("Failure to identify user, email: " + email, e);
            }
        }
        return user;
    }

    public EnumSet<UserOperationError> rechargeBalance(long id, String cardNumber, String validMonth, String validYear,
                                                       String cardVerificationCode, String cardHolder, String amount) throws LogicException {
        EnumSet<UserOperationError> errors = EnumSet.noneOf(UserOperationError.class);
        CardValidator cardValidator = new CardValidator();
        if (!cardValidator.checkNumber(cardNumber) || !cardValidator.checkDate(validMonth, validYear) ||
                !cardValidator.checkCode(cardVerificationCode) || !cardValidator.checkHolder(cardHolder)) {
            errors.add(UserOperationError.INCORRECT_CARD_INFORMATION);
        }
        if (!new UserValidator().checkAmount(amount)) {
            errors.add(UserOperationError.INCORRECT_AMOUNT);
        }
        BigDecimal parsedAmount = new BigDecimal(amount);
        if (!cardValidator.checkMoney(parsedAmount)) {
            errors.add(UserOperationError.INSUFFICIENT_FUNDS);
        }
        if (errors.isEmpty()) {
            try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
                UserDAO userDAO = new UserDAO(connection);
                boolean isAdded = userDAO.modifyBalance(id, parsedAmount);
                if (!isAdded) {
                    errors.add(UserOperationError.ERROR);
                }
            } catch (DAOException e) {
                throw new LogicException("Failure to recharge user's balance, id: " + id, e);
            }
        }
        return errors;
    }

    public EnumSet<UserOperationError> register(String name, String email, String password, String confirmPassword)
            throws LogicException {
        EnumSet<UserOperationError> errors = EnumSet.noneOf(UserOperationError.class);
        UserValidator userValidator = new UserValidator();
        if (!userValidator.checkUsername(name)) {
            errors.add(UserOperationError.INCORRECT_NAME);
        }
        if (!userValidator.checkEmail(email)) {
            errors.add(UserOperationError.INCORRECT_EMAIL);
        }
        if (!userValidator.checkPassword(password)) {
            errors.add(UserOperationError.INCORRECT_PASSWORD);
        }
        if (!userValidator.checkPasswordConformation(password, confirmPassword)) {
            errors.add(UserOperationError.MISMATCHED_PASSWORDS);
        }
        if (errors.isEmpty()) {
            try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
                UserDAO userDAO = new UserDAO(connection);
                if (userDAO.findByEmail(email).isPresent()) {
                    errors.add(UserOperationError.EXISTING_EMAIL);
                }
                else {
                    String passwordHash = DigestUtils.md5Hex(password);
                    boolean isAdded = userDAO.add(name, email, passwordHash);
                    if (!isAdded) {
                        errors.add(UserOperationError.ERROR);
                    }
                }
            } catch (DAOException e) {
                throw new LogicException("Failure to register user", e);
            }
        }
        return errors;
    }

    public EnumSet<UserOperationError> transferMoney(long id, String receiverEmail, String amount, String password)
            throws LogicException {
        EnumSet<UserOperationError> errors = EnumSet.noneOf(UserOperationError.class);
        UserValidator userValidator = new UserValidator();
        if (!userValidator.checkEmail(receiverEmail)) {
            errors.add(UserOperationError.INCORRECT_EMAIL);
        }
        if (!userValidator.checkAmount(amount)) {
            errors.add(UserOperationError.INCORRECT_AMOUNT);
        }
        if (!userValidator.checkPassword(password)) {
            errors.add(UserOperationError.INCORRECT_PASSWORD);
        }
        ProxyConnection connection = ConnectionPool.getInstance().takeConnection();
        try {
            UserDAO userDAO = new UserDAO(connection);
            String passwordHash = DigestUtils.md5Hex(password);
            BigDecimal parsedAmount = new BigDecimal(amount);
            Optional<User> optionalSender = userDAO.findByIdAndPassword(id, passwordHash);
            if (!optionalSender.isPresent()) {
                errors.add(UserOperationError.WRONG_PASSWORD);
            } else if (optionalSender.get().getBalance().compareTo(parsedAmount) < 0) {
                errors.add(UserOperationError.INSUFFICIENT_FUNDS);
            }
            Optional<User> optionalReceiver = userDAO.findByEmail(receiverEmail);
            if (!optionalReceiver.isPresent()) {
                errors.add(UserOperationError.NONEXISTENT_USER);
            } else if (optionalReceiver.get().getIsBlocked()) {
                errors.add(UserOperationError.BLOCKED_USER);
            }
            if (errors.isEmpty()) {
                connection.setAutoCommit(false);
                boolean isSent = userDAO.modifyBalance(optionalSender.get().getId(), parsedAmount.negate());
                boolean isReceived = userDAO.modifyBalance(optionalReceiver.get().getId(), parsedAmount);
                boolean isTransactionAdded = new TransactionDAO(connection).add(optionalSender.get().getId(), optionalReceiver.get().getId(), parsedAmount);
                if (isSent && isReceived && isTransactionAdded) {
                    connection.commit();
                } else {
                    connection.rollback();
                    errors.add(UserOperationError.ERROR);
                }
                connection.setAutoCommit(true);
            }
        } catch(SQLException | DAOException e){
            try {
                connection.rollback();
                connection.setAutoCommit(true);
                throw new LogicException("Failure to complete transaction", e);
            } catch (SQLException e1) {
                throw new LogicException("Failure to rollback transaction", e1);
            }
        } finally {
            connection.close();
        }
        return errors;
    }

    public boolean unblock(String id) throws LogicException {
        try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
            return new UserDAO(connection).changeIsBlocked(Long.parseLong(id), false);
        } catch (DAOException e) {
            throw new LogicException("Failure to unblock user, id: " + id, e);
        }
    }

    public EnumSet<UserOperationError> withdrawMoney(long id, String cardNumber, String amount, String password)
            throws LogicException {
        EnumSet<UserOperationError> errors = EnumSet.noneOf(UserOperationError.class);
        UserValidator userValidator = new UserValidator();
        CardValidator cardValidator = new CardValidator();
        if (!cardValidator.checkNumber(cardNumber)) {
            errors.add(UserOperationError.INCORRECT_CARD_INFORMATION);
        }
        if (!userValidator.checkAmount(amount)) {
            errors.add(UserOperationError.INCORRECT_AMOUNT);
        }
        if (!userValidator.checkPassword(password)) {
            errors.add(UserOperationError.INCORRECT_PASSWORD);
        }
        try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
            UserDAO userDAO = new UserDAO(connection);
            String passwordHash = DigestUtils.md5Hex(password);
            BigDecimal parsedAmount = new BigDecimal(amount);
            Optional<User> optionalUser = userDAO.findByIdAndPassword(id, passwordHash);
            if (!optionalUser.isPresent()) {
                errors.add(UserOperationError.WRONG_PASSWORD);
            } else if (optionalUser.get().getBalance().compareTo(parsedAmount) < 0) {
                errors.add(UserOperationError.INSUFFICIENT_FUNDS);
            }
            if (errors.isEmpty()) {
                boolean isWithdrawn = userDAO.modifyBalance(id, parsedAmount.negate());
                if (!isWithdrawn) {
                    errors.add(UserOperationError.ERROR);
                }
            }
        } catch(DAOException e) {
            throw new LogicException("Failure to withdraw money, id: " + id, e);
        }
        return errors;
    }
}
