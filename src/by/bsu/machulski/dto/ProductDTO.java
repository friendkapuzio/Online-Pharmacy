package by.bsu.machulski.dto;

import java.math.BigDecimal;

public class ProductDTO extends AbstractDTO {
    private String name;
    private BigDecimal price;
    private int amount;
    private String productForm;
    private String formDescription;
    private boolean isPrescriptionRequired;
    private boolean isDeleted;

    public ProductDTO() {
    }

    public ProductDTO(long id, String name, BigDecimal price, int amount, String productForm,
                      String formDescription, boolean isPrescriptionRequired, boolean isDeleted) {
        super(id);
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.productForm = productForm;
        this.formDescription = formDescription;
        this.isPrescriptionRequired = isPrescriptionRequired;
        this.isDeleted = isDeleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getProductForm() {
        return productForm;
    }

    public void setProductForm(String productForm) {
        this.productForm = productForm;
    }

    public String getFormDescription() {
        return formDescription;
    }

    public void setFormDescription(String formDescription) {
        this.formDescription = formDescription;
    }

    public boolean getIsPrescriptionRequired() {
        return isPrescriptionRequired;
    }

    public void setIsPrescriptionRequired(boolean prescriptionRequired) {
        isPrescriptionRequired = prescriptionRequired;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductDTO that = (ProductDTO) o;

        if (amount != that.amount) return false;
        if (isPrescriptionRequired != that.isPrescriptionRequired) return false;
        if (isDeleted != that.isDeleted) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        if (productForm != null ? !productForm.equals(that.productForm) : that.productForm != null) return false;
        return formDescription != null ? formDescription.equals(that.formDescription) : that.formDescription == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + amount;
        result = 31 * result + (productForm != null ? productForm.hashCode() : 0);
        result = 31 * result + (formDescription != null ? formDescription.hashCode() : 0);
        result = 31 * result + (isPrescriptionRequired ? 1 : 0);
        result = 31 * result + (isDeleted ? 1 : 0);
        return result;
    }
}
