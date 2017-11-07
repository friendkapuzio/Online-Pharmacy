package by.bsu.machulski.util;

import by.bsu.machulski.dto.PrescriptionDTO;
import by.bsu.machulski.entity.Prescription;
import by.bsu.machulski.entity.Product;
import by.bsu.machulski.entity.User;

public class PrescriptionDTOTransformer {
    public static void addDoctor(PrescriptionDTO prescriptionDTO, User doctor) {
        prescriptionDTO.setDoctorName(doctor.getName());
        prescriptionDTO.setDoctorEmail(doctor.getEmail());
    }

    public static void addPatient(PrescriptionDTO prescriptionDTO, User patient) {
        prescriptionDTO.setPatientName(patient.getName());
        prescriptionDTO.setPatientEmail(patient.getEmail());
    }

    public static PrescriptionDTO toDTO(Prescription prescription, Product product) {
        PrescriptionDTO prescriptionDTO = new PrescriptionDTO();
        prescriptionDTO.setId(prescription.getId());
        prescriptionDTO.setPatientId(prescription.getPatientId());
        prescriptionDTO.setDoctorId(prescription.getDoctorId());
        prescriptionDTO.setProductId(prescription.getProductId());
        prescriptionDTO.setAmount(prescription.getAmount());
        prescriptionDTO.setExpirationDate(prescription.getExpirationDate());
        prescriptionDTO.setIsUsed(prescription.getIsUsed());
        prescriptionDTO.setProductName(product.getName());
        prescriptionDTO.setIsProductAvailable(!product.getIsDeleted());
        prescriptionDTO.setProductPrice(product.getPrice());
        prescriptionDTO.setProductQuantity(product.getAvailableQuantity());
        return prescriptionDTO;
    }
}
