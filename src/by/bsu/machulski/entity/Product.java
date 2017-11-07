package by.bsu.machulski.entity;

import java.math.BigDecimal;

public class Product extends Entity {
    private String name;
    private BigDecimal price;
    private int availableQuantity;
    private String productForm;
    private String formDescription;
    private boolean isPrescriptionRequired;
    private boolean isDeleted;

    public Product() {
    }

    public Product(long id, String name, BigDecimal price, int availableQuantity, String productForm,
                   String formDescription, boolean isPrescriptionRequired, boolean isDeleted) {
        super(id);
        this.name = name;
        this.price = price;
        this.availableQuantity = availableQuantity;
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

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
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
        if (!super.equals(o)) return false;

        Product product = (Product) o;

        if (availableQuantity != product.availableQuantity) return false;
        if (isPrescriptionRequired != product.isPrescriptionRequired) return false;
        if (isDeleted != product.isDeleted) return false;
        if (name != null ? !name.equals(product.name) : product.name != null) return false;
        if (price != null ? !price.equals(product.price) : product.price != null) return false;
        if (productForm != null ? !productForm.equals(product.productForm) : product.productForm != null) return false;
        return formDescription != null ? formDescription.equals(product.formDescription) : product.formDescription == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + availableQuantity;
        result = 31 * result + (productForm != null ? productForm.hashCode() : 0);
        result = 31 * result + (formDescription != null ? formDescription.hashCode() : 0);
        result = 31 * result + (isPrescriptionRequired ? 1 : 0);
        result = 31 * result + (isDeleted ? 1 : 0);
        return result;
    }
}
