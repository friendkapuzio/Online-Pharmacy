package by.bsu.machulski.entity;

import java.sql.Date;

public class Prescription extends Entity {
    private long patientId;
    private long doctorId;
    private long productId;
    private int amount;
    private Date expirationDate;
    private boolean isUsed;

    public Prescription() {
    }

    public Prescription(long id, long patientId, long doctorId, long productId, int amount, Date expirationDate, boolean isUsed) {
        super(id);
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.productId = productId;
        this.amount = amount;
        this.expirationDate = expirationDate;
        this.isUsed = isUsed;
    }

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(long doctorId) {
        this.doctorId = doctorId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(boolean used) {
        isUsed = used;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Prescription prescription = (Prescription) o;

        if (patientId != prescription.patientId) return false;
        if (doctorId != prescription.doctorId) return false;
        if (productId != prescription.productId) return false;
        if (amount != prescription.amount) return false;
        if (isUsed != prescription.isUsed) return false;
        return expirationDate != null ? expirationDate.equals(prescription.expirationDate) : prescription.expirationDate == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (patientId ^ (patientId >>> 32));
        result = 31 * result + (int) (doctorId ^ (doctorId >>> 32));
        result = 31 * result + (int) (productId ^ (productId >>> 32));
        result = 31 * result + amount;
        result = 31 * result + (expirationDate != null ? expirationDate.hashCode() : 0);
        result = 31 * result + (isUsed ? 1 : 0);
        return result;
    }
}
