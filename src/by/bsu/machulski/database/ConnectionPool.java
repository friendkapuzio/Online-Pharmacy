package by.bsu.machulski.database;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionPool {
    private static final Logger LOGGER =  LogManager.getLogger(ConnectionPool.class);
    private static ConnectionPool connectionPool;
    private static AtomicBoolean isCreated = new AtomicBoolean(false);
    private static ReentrantLock lock = new ReentrantLock();
    private final int CAPACITY;
    private static AtomicInteger brokenConnectionsNumber = new AtomicInteger(0);
    private BlockingQueue<ProxyConnection> connections;

    private ConnectionPool() {
        PoolConfigurator configurator = new PoolConfigurator();
        CAPACITY = configurator.getPoolCapacity();
        connections = new ArrayBlockingQueue<ProxyConnection>(CAPACITY);
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        } catch (SQLException e) {
            LOGGER.log(Level.FATAL, e);
            throw new RuntimeException();
        }
        String url = configurator.getURL();
        Properties properties = configurator.getProperties();
        for (int i = 0; i < CAPACITY; ++i) {
            try {
                connections.add(new ProxyConnection(DriverManager.getConnection(url, properties)));
            } catch (SQLException e) {
                LOGGER.log(Level.ERROR, "Connection has not been added.", e);
                brokenConnectionsNumber.incrementAndGet();
            }
        }
        if (brokenConnectionsNumber.get() == CAPACITY) {
            LOGGER.log(Level.FATAL, "No connections");
            throw new RuntimeException();
        }
    }

    public static ConnectionPool getInstance() {
        if (!isCreated.get()) {
            lock.lock();
            try {
                if (!isCreated.get()) {
                    connectionPool = new ConnectionPool();
                    isCreated.getAndSet(true);
                }
            } finally {
                lock.unlock();
            }
        }
        return connectionPool;
    }

    public ProxyConnection takeConnection() {
        ProxyConnection connection = null;
        while (connection == null) {
            if (brokenConnectionsNumber.get() == CAPACITY) {
                LOGGER.log(Level.FATAL, "No connections");
                throw new RuntimeException();
            }
            try {
                connection = connections.take();
                if (!connection.isValid(0)) {
                    try {
                        connection.realClose();
                    } catch (SQLException e) {
                        LOGGER.log(Level.ERROR, "Error during closing connection", e);
                    } finally {
                        connection = null;
                        PoolConfigurator configurator = new PoolConfigurator();
                        String url = configurator.getURL();
                        Properties properties = configurator.getProperties();
                        connections.add(new ProxyConnection(DriverManager.getConnection(url, properties)));
                    }
                }
            } catch (SQLException e) {
                LOGGER.log(Level.ERROR, "Cannot create connection");
                brokenConnectionsNumber.incrementAndGet();
            } catch (InterruptedException e) {
                LOGGER.log(Level.ERROR, e);
            }
        }
        return connection;
    }

    void releaseConnection(ProxyConnection connection) {
        try {
            connections.put(connection);
        } catch (InterruptedException e) {
            LOGGER.log(Level.ERROR, e);
        }
    }

    public void closePool() {
        try {
            for (int i = 0; i < CAPACITY; ++i) {
                connections.take().realClose();
            }
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver driver = drivers.nextElement();
                DriverManager.deregisterDriver(driver);
            }
        } catch (InterruptedException | SQLException e) {
            LOGGER.log(Level.ERROR, e);
        }
    }

    private void recreateConnections() {

    }
}
