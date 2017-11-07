package by.bsu.machulski.logic;

import by.bsu.machulski.dao.ProductDAO;
import by.bsu.machulski.dao.PrescriptionDAO;
import by.bsu.machulski.dao.UserDAO;
import by.bsu.machulski.database.ConnectionPool;
import by.bsu.machulski.database.ProxyConnection;
import by.bsu.machulski.dto.PrescriptionDTO;
import by.bsu.machulski.entity.Prescription;
import by.bsu.machulski.entity.Product;
import by.bsu.machulski.entity.User;
import by.bsu.machulski.exception.DAOException;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.type.PrescriptionOperationError;
import by.bsu.machulski.util.PrescriptionDTOTransformer;
import by.bsu.machulski.validator.ProductValidator;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class PrescriptionLogic {
    public EnumSet<PrescriptionOperationError> changeExpirationDate(String prescriptionIdStr, String newExpirationDateStr) throws LogicException {
        EnumSet<PrescriptionOperationError> errors = EnumSet.noneOf(PrescriptionOperationError.class);
        long prescriptionId = Long.parseLong(prescriptionIdStr);
        LocalDate newExpirationDate;
        try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
            newExpirationDate = LocalDate.parse(newExpirationDateStr);
            if (newExpirationDate.isBefore(LocalDate.now())) {
                errors.add(PrescriptionOperationError.INCORRECT_DATE);
            } else {
                boolean isUpdated = new PrescriptionDAO(connection).updateExpirationDate(prescriptionId, Date.valueOf(newExpirationDate));
                if (!isUpdated) {
                    errors.add(PrescriptionOperationError.ERROR);
                }
            }
        } catch (DateTimeParseException e) {
            errors.add(PrescriptionOperationError.INCORRECT_DATE);
        } catch (DAOException e) {
            throw new LogicException("Failure to update prescription's expiration date, prescription id: " + prescriptionId, e);
        }
        return errors;
    }

    public EnumSet<PrescriptionOperationError> create(long doctorId, String productIdStr, String productAmountStr,
                                                      String expirationDateStr, String patientIdStr) throws LogicException {
        EnumSet<PrescriptionOperationError> errors = EnumSet.noneOf(PrescriptionOperationError.class);
        if (!new ProductValidator().checkAmount(productAmountStr)) {
            errors.add(PrescriptionOperationError.INCORRECT_AMOUNT);
        }
        try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
            LocalDate expirationDate = LocalDate.parse(expirationDateStr);
            if (expirationDate.isBefore(LocalDate.now())) {
                errors.add(PrescriptionOperationError.INCORRECT_DATE);
            }
            if (errors.isEmpty()) {
                PrescriptionDAO prescriptionDAO = new PrescriptionDAO(connection);
                long patientId = Long.parseLong(patientIdStr);
                long productId = Long.parseLong(productIdStr);
                int productAmount = Integer.parseInt(productAmountStr);
                boolean isCreated = prescriptionDAO.create(patientId, doctorId, productId, productAmount, Date.valueOf(expirationDate));
                if (!isCreated) {
                    errors.add(PrescriptionOperationError.ERROR);
                }
            }
        } catch (DateTimeParseException e) {
            errors.add(PrescriptionOperationError.INCORRECT_DATE);
        } catch (DAOException e) {
            throw new LogicException("Failure to create prescription", e);
        }

        return errors;
    }

    public List<Prescription> findActualByUserAndProduct(long userId, long productId) throws LogicException {
        try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
            return new PrescriptionDAO(connection).findActualByUserAndProduct(userId, productId);
        } catch (DAOException e) {
            throw new LogicException("Failure to find prescriptions by user id, user id: " + userId, e);
        }
    }

    public List<PrescriptionDTO> findByDoctor(long userId) throws LogicException {
        List<PrescriptionDTO> prescriptionDTOs = new ArrayList<>();
        try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
            List<Prescription> prescriptions = new PrescriptionDAO(connection).findByDoctor(userId);
            ProductDAO productDAO = new ProductDAO(connection);
            UserDAO userDAO = new UserDAO(connection);
            for (Prescription prescription : prescriptions) {
                Product product = productDAO.findById(prescription.getProductId())
                        .orElseThrow(() -> new LogicException("Incorrect product information, prescription id: " + prescription.getId()));
                User patient = userDAO.findById(prescription.getPatientId())
                        .orElseThrow(() -> new LogicException("Incorrect patient information, prescription id: " + prescription.getId()));
                PrescriptionDTO prescriptionDTO = PrescriptionDTOTransformer.toDTO(prescription, product);
                PrescriptionDTOTransformer.addPatient(prescriptionDTO, patient);
                prescriptionDTOs.add(prescriptionDTO);
            }
            prescriptionDTOs.sort(Comparator.comparing(PrescriptionDTO::getExpirationDate).reversed());
        } catch (DAOException e) {
            throw new LogicException("Failure to find user's prescriptions, userId: " + userId, e);
        }
        return prescriptionDTOs;
    }

    public List<PrescriptionDTO> findByUser(long userId, String isActual) throws LogicException {
        List<PrescriptionDTO> prescriptionDTOs = new ArrayList<>();
        boolean isActualParsed = Boolean.valueOf(isActual);
        try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
            List<Prescription> prescriptions;
            if (isActualParsed) {
                prescriptions = new PrescriptionDAO(connection).findActualByUser(userId);
            } else {
                prescriptions = new PrescriptionDAO(connection).findPastByUser(userId);
            }
            ProductDAO productDAO = new ProductDAO(connection);
            UserDAO userDAO = new UserDAO(connection);
            for (Prescription prescription : prescriptions) {
                Product product = productDAO.findById(prescription.getProductId())
                        .orElseThrow(() -> new LogicException("Incorrect product information, prescription id: " + prescription.getId()));
                User doctor = userDAO.findById(prescription.getDoctorId())
                        .orElseThrow(() -> new LogicException("Incorrect doctor information, prescription id: " + prescription.getId()));
                PrescriptionDTO prescriptionDTO = PrescriptionDTOTransformer.toDTO(prescription, product);
                PrescriptionDTOTransformer.addDoctor(prescriptionDTO, doctor);
                prescriptionDTOs.add(prescriptionDTO);            }
            prescriptionDTOs.sort(Comparator.comparing(PrescriptionDTO::getExpirationDate).reversed());
        } catch (DAOException e) {
            throw new LogicException("Failure to find user's prescriptions, userId: " + userId, e);
        }
        return prescriptionDTOs;
    }
}
