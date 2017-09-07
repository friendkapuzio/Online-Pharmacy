package by.bsu.machulski.dao;

import by.bsu.machulski.database.ProxyConnection;
import by.bsu.machulski.entity.Entity;
import by.bsu.machulski.exception.DAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public abstract class AbstractDAO<T extends Entity> {
    private static final Logger LOGGER = LogManager.getLogger(AbstractDAO.class);
    protected ProxyConnection connection;

    public AbstractDAO(ProxyConnection connection) {
        this.connection = connection;
    }

    public abstract List<T> findAll();

    public abstract Optional<T> findById(long id) throws DAOException;
}
