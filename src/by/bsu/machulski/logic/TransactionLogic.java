package by.bsu.machulski.logic;

import by.bsu.machulski.dao.TransactionDAO;
import by.bsu.machulski.dao.UserDAO;
import by.bsu.machulski.database.ConnectionPool;
import by.bsu.machulski.database.ProxyConnection;
import by.bsu.machulski.dto.TransactionDTO;
import by.bsu.machulski.entity.Transaction;
import by.bsu.machulski.exception.DAOException;
import by.bsu.machulski.exception.LogicException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TransactionLogic {
    public List<TransactionDTO> find(long userId, String transactionsYear) throws LogicException {
        List<TransactionDTO> transactionDTOs = null;
        try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
            TransactionDAO transactionDAO = new TransactionDAO(connection);
            UserDAO userDAO = new UserDAO(connection);
            List<Transaction> transactions = transactionDAO.findByUserAndYear(userId, Integer.parseInt(transactionsYear));
            transactionDTOs = new ArrayList<>();
            for (Transaction transaction:transactions) {
                TransactionDTO transactionDTO = new TransactionDTO();
                transactionDTO.setId(transaction.getId());
                transactionDTO.setSenderEmail(userDAO.findById(transaction.getSenderId()).get().getEmail());
                transactionDTO.setReceiverEmail(userDAO.findById(transaction.getReceiverId()).get().getEmail());
                transactionDTO.setDate(transaction.getDate());
                transactionDTO.setAmount(transaction.getAmount());
                transactionDTOs.add(transactionDTO);
            }
            transactionDTOs.sort(Comparator.comparing(TransactionDTO::getDate).reversed());
        } catch (DAOException e) {
            throw new LogicException("Failure to find transactions, user id: " + userId + ", transactions year: " + transactionsYear, e);
        }
        return transactionDTOs;
    }
}
