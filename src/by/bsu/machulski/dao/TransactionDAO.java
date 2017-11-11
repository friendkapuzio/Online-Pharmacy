package by.bsu.machulski.dao;

import by.bsu.machulski.database.ProxyConnection;
import by.bsu.machulski.entity.Transaction;
import by.bsu.machulski.exception.DAOException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class TransactionDAO extends AbstractDAO<Transaction> {
    private static final Logger LOGGER = LogManager.getLogger(TransactionDAO.class);
    private static final String TRANSACTION_ID = "transaction_id";
    private static final String SENDER_ID = "sender_id";
    private static final String RECEIVER_ID = "receiver_id";
    private static final String DATE = "date";
    private static final String AMOUNT = "amount";
    private static final String TRANSACTION_COLUMNS = "`transaction_id`, `sender_id`, `receiver_id`, `date`, `amount`";
    private static final String FIND_BY_USER_AND_YEAR = "SELECT " + TRANSACTION_COLUMNS + " FROM pharmacy.transactions WHERE (`sender_id`=? OR `receiver_id`=?) AND year(`date`)=?;";;
    private static final String INSERT_TRANSACTION = "INSERT INTO `pharmacy`.`transactions` (`sender_id`, `receiver_id`, `amount`) VALUES (?, ?, ?);";
    private static final String SELECT_BY_ID = "SELECT " + TRANSACTION_COLUMNS + " FROM `pharmacy`.`transactions` WHERE `transaction_id`=?;";

    public TransactionDAO(ProxyConnection connection) {
        super(connection);
    }

    public boolean add(long senderId, long receiverId, BigDecimal amount) throws DAOException {
        boolean isAdded = false;
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TRANSACTION)) {
            preparedStatement.setLong(1, senderId);
            preparedStatement.setLong(2, receiverId);
            preparedStatement.setBigDecimal(3, amount);
            if (preparedStatement.executeUpdate() == 1) {
                isAdded = true;
            } else {
                LOGGER.log(Level.WARN, "Failure to insert new transaction");
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to insert new transaction", e);
        }
        return isAdded;
    }

    @Override
    public Optional<Transaction> findById(long id) throws DAOException {
        Transaction transaction = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                transaction = createTransaction(rs);
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to find transaction by id, id: " + id, e);
        }
        return Optional.ofNullable(transaction);
    }

    public List<Transaction> findByUserAndYear(long userId, int year) throws DAOException {
        List<Transaction> transactions = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_USER_AND_YEAR)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, userId);
            preparedStatement.setInt(3, year);
            ResultSet rs = preparedStatement.executeQuery();
            transactions = new ArrayList<>();
            while (rs.next()) {
                transactions.add(createTransaction(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to find transactions by user and year, user id " + userId + ", year: " + year, e);
        }
        return transactions;
    }

    private Transaction createTransaction(ResultSet rs) throws SQLException {
        long transactionId = rs.getLong(TRANSACTION_ID);
        long senderId = rs.getLong(SENDER_ID);
        long receiverId = rs.getLong(RECEIVER_ID);
        BigDecimal amount = rs.getBigDecimal(AMOUNT);
        Date date = rs.getDate(DATE);
        return new Transaction(transactionId, senderId, receiverId, amount, date);
    }
}
