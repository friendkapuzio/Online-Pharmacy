package by.bsu.machulski.dto;

import java.math.BigDecimal;
import java.util.Date;

public class PrescriptionDTO extends AbstractDTO {
    private long patientId;
    private long doctorId;
    private long productId;
    private int amount;
    private Date expirationDate;
    private boolean isUsed;
    private String productName;
    private String patientName;
    private String patientEmail;
    private String doctorName;
    private String doctorEmail;
    private boolean isProductAvailable;
    private BigDecimal productPrice;
    private int productQuantity;

    public PrescriptionDTO() {
    }

    public PrescriptionDTO(long id, long patientId, long doctorId, long productId, int amount, Date expirationDate,
                           boolean isUsed, String productName, String patientName, String patientEmail, String doctorName,
                           String doctorEmail, boolean isProductAvailable, BigDecimal productPrice, int productQuantity) {
        super(id);
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.productId = productId;
        this.amount = amount;
        this.expirationDate = expirationDate;
        this.isUsed = isUsed;
        this.productName = productName;
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.doctorName = doctorName;
        this.doctorEmail = doctorEmail;
        this.isProductAvailable = isProductAvailable;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }

    public boolean getIsProductAvailable() {
        return isProductAvailable;
    }

    public void setIsProductAvailable(boolean productAvailable) {
        isProductAvailable = productAvailable;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrescriptionDTO that = (PrescriptionDTO) o;

        if (patientId != that.patientId) return false;
        if (doctorId != that.doctorId) return false;
        if (productId != that.productId) return false;
        if (amount != that.amount) return false;
        if (isUsed != that.isUsed) return false;
        if (isProductAvailable != that.isProductAvailable) return false;
        if (productQuantity != that.productQuantity) return false;
        if (expirationDate != null ? !expirationDate.equals(that.expirationDate) : that.expirationDate != null)
            return false;
        if (productName != null ? !productName.equals(that.productName) : that.productName != null) return false;
        if (patientName != null ? !patientName.equals(that.patientName) : that.patientName != null) return false;
        if (patientEmail != null ? !patientEmail.equals(that.patientEmail) : that.patientEmail != null) return false;
        if (doctorName != null ? !doctorName.equals(that.doctorName) : that.doctorName != null) return false;
        if (doctorEmail != null ? !doctorEmail.equals(that.doctorEmail) : that.doctorEmail != null) return false;
        return productPrice != null ? productPrice.equals(that.productPrice) : that.productPrice == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (patientId ^ (patientId >>> 32));
        result = 31 * result + (int) (doctorId ^ (doctorId >>> 32));
        result = 31 * result + (int) (productId ^ (productId >>> 32));
        result = 31 * result + amount;
        result = 31 * result + (expirationDate != null ? expirationDate.hashCode() : 0);
        result = 31 * result + (isUsed ? 1 : 0);
        result = 31 * result + (productName != null ? productName.hashCode() : 0);
        result = 31 * result + (patientName != null ? patientName.hashCode() : 0);
        result = 31 * result + (patientEmail != null ? patientEmail.hashCode() : 0);
        result = 31 * result + (doctorName != null ? doctorName.hashCode() : 0);
        result = 31 * result + (doctorEmail != null ? doctorEmail.hashCode() : 0);
        result = 31 * result + (isProductAvailable ? 1 : 0);
        result = 31 * result + (productPrice != null ? productPrice.hashCode() : 0);
        result = 31 * result + productQuantity;
        return result;
    }
}
