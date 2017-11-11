package by.bsu.machulski.dao;

import by.bsu.machulski.database.ProxyConnection;
import by.bsu.machulski.entity.Prescription;
import by.bsu.machulski.exception.DAOException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PrescriptionDAO extends AbstractDAO<Prescription> {
    private static final Logger LOGGER = LogManager.getLogger(PrescriptionDAO.class);
    private static final String PRESCRIPTION_ID = "prescription_id";
    private static final String PATIENT_ID = "patient_id";
    private static final String DOCTOR_ID = "doctor_id";
    private static final String PRODUCT_ID = "product_id";
    private static final String AMOUNT = "amount";
    private static final String EXPIRATION_DATE = "expiration_date";
    private static final String IS_USED = "is_used";
    private static final String PRESCRIPTION_COLUMNS = "`prescription_id`, `patient_id`, `doctor_id`, `product_id`, `amount`, `expiration_date`, `is_used`";
    private static final String INSERT_PRODUCT =
            "INSERT INTO `pharmacy`.`prescriptions` (`patient_id`, `doctor_id`, `product_id`, `amount`, `expiration_date`) VALUES (?, ?, ?, ?, ?);";
    private static final String SELECT_ACTUAL_BY_PATIENT_AND_PRODUCT = "SELECT " + PRESCRIPTION_COLUMNS + " FROM `pharmacy`.`prescriptions` " +
            "WHERE `patient_id`=? AND `product_id`=? AND `is_used`=0 AND `expiration_date` >= CURDATE()";
    private static final String SELECT_ACTUAL_BY_PATIENT_ID = "SELECT " + PRESCRIPTION_COLUMNS + " FROM `pharmacy`.`prescriptions` " +
            "WHERE `patient_id`=? AND `is_used`=0 AND `expiration_date` >= CURDATE()";
    private static final String SELECT_BY_DOCTOR_ID = "SELECT " + PRESCRIPTION_COLUMNS + " FROM `pharmacy`.`prescriptions` " +
            "WHERE `doctor_id`=?;";
    private static final String SELECT_BY_ID = "SELECT " + PRESCRIPTION_COLUMNS + " FROM `pharmacy`.`prescriptions` WHERE `prescription_id`=?;";
    private static final String SELECT_PAST_BY_PATIENT_ID = "SELECT " + PRESCRIPTION_COLUMNS + " FROM `pharmacy`.`prescriptions` " +
            "WHERE `patient_id`=? AND (`is_used`=1 OR `expiration_date` < CURDATE())";
    private static final String UPDATE_AMOUNT = "UPDATE `pharmacy`.`prescriptions` SET `amount`=? WHERE `prescription_id`=?;";
    private static final String UPDATE_EXPIRATION_DATE = "UPDATE `pharmacy`.`prescriptions` SET `expiration_date`=? WHERE `prescription_id`=?;";
    private static final String UPDATE_IS_USED = "UPDATE `pharmacy`.`prescriptions` SET `is_used`=? WHERE `prescription_id`=?;";

    public PrescriptionDAO(ProxyConnection connection) {
        super(connection);
    }

    public boolean create(long patientId, long doctorId, long productId, int productAmount, Date expirationDate) throws DAOException {
        boolean isCreated = false;
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRODUCT)) {
            preparedStatement.setLong(1, patientId);
            preparedStatement.setLong(2, doctorId);
            preparedStatement.setLong(3, productId);
            preparedStatement.setInt(4, productAmount);
            preparedStatement.setDate(5, expirationDate);
            if (preparedStatement.executeUpdate() == 1) {
                isCreated = true;
            } else {
                LOGGER.log(Level.WARN, "Failure to create prescription");
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to create prescription", e);
        }
        return isCreated;
    }

    public List<Prescription> findActualByUser(long id) throws DAOException {
        List<Prescription> prescriptions = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ACTUAL_BY_PATIENT_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                prescriptions.add(createPrescription(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to find actual user's prescriptions, user id: " + id, e);
        }
        return prescriptions;
    }

    public List<Prescription> findActualByUserAndProduct(long userId, long productId) throws DAOException {
        List<Prescription> prescriptions = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ACTUAL_BY_PATIENT_AND_PRODUCT)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, productId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                prescriptions.add(createPrescription(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to find actual user's prescriptions, user id: " + userId, e);
        }
        return prescriptions;
    }

    public List<Prescription> findByDoctor(long doctorId) throws DAOException {
        List<Prescription> prescriptions = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_DOCTOR_ID)) {
            preparedStatement.setLong(1, doctorId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                prescriptions.add(createPrescription(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to find doctor's prescriptions, doctor id: " + doctorId, e);
        }
        return prescriptions;
    }

    @Override
    public Optional<Prescription> findById(long id) throws DAOException {
        Prescription prescription = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                prescription = createPrescription(rs);
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to find prescription by id, id: " + id, e);
        }
        return Optional.ofNullable(prescription);
    }

    public List<Prescription> findPastByUser(long id) throws DAOException {
        List<Prescription> prescriptions = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PAST_BY_PATIENT_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                prescriptions.add(createPrescription(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to find past user's prescriptions, user id: " + id, e);
        }
        return prescriptions;
    }

    public boolean updateAmount(long prescriptionId, int newAmount) throws DAOException {
        boolean isUpdated = false;
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_AMOUNT)) {
            preparedStatement.setInt(1, newAmount);
            preparedStatement.setLong(2, prescriptionId);
            if (preparedStatement.executeUpdate() == 1) {
                isUpdated = true;
            } else {
                LOGGER.log(Level.WARN, "Failure to update product amount, prescription id " + prescriptionId);
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to update product amount, prescription id " + prescriptionId, e);
        }
        return isUpdated;
    }

    public boolean updateExpirationDate(long prescriptionId, Date newExpirationDate) throws DAOException {
        boolean isUpdated = false;
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_EXPIRATION_DATE)) {
            preparedStatement.setDate(1, newExpirationDate);
            preparedStatement.setLong(2, prescriptionId);
            if (preparedStatement.executeUpdate() == 1) {
                isUpdated = true;
            } else {
                LOGGER.log(Level.WARN, "Failure to update prescription's expiration date, prescription id " + prescriptionId);
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to update prescription's expiration date, prescription id " + prescriptionId, e);
        }
        return isUpdated;
    }

    public boolean updateIsUsed(long prescriptionId, boolean newIsUsed) throws DAOException {
        boolean isUpdated = false;
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_IS_USED)) {
            preparedStatement.setBoolean(1, newIsUsed);
            preparedStatement.setLong(2, prescriptionId);
            if (preparedStatement.executeUpdate() == 1) {
                isUpdated = true;
            } else {
                LOGGER.log(Level.WARN, "Failure to update prescription's is used status, prescription id " + prescriptionId);
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to update prescription's is used status, prescription id " + prescriptionId, e);
        }
        return isUpdated;
    }

    private Prescription createPrescription(ResultSet rs) throws SQLException {
        long prescriptionId = rs.getLong(PRESCRIPTION_ID);
        long patientId = rs.getLong(PATIENT_ID);
        long doctorId = rs.getLong(DOCTOR_ID);
        long productId = rs.getLong(PRODUCT_ID);
        int amount = rs.getInt(AMOUNT);
        Date expirationDate = rs.getDate(EXPIRATION_DATE);
        boolean isUsed = rs.getBoolean(IS_USED);
        return new Prescription(prescriptionId, patientId, doctorId, productId, amount, expirationDate, isUsed);
    }
}
