package by.bsu.machulski.dto;

import java.math.BigDecimal;
import java.util.Date;

public class TransactionDTO extends AbstractDTO {
    private String senderEmail;
    private String receiverEmail;
    private Date date;
    private BigDecimal amount;

    public TransactionDTO() {
    }

    public TransactionDTO(long id, String senderEmail, String receiverEmail, Date date, BigDecimal amount) {
        super(id);
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.date = date;
        this.amount = amount;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionDTO that = (TransactionDTO) o;

        if (senderEmail != null ? !senderEmail.equals(that.senderEmail) : that.senderEmail != null) return false;
        if (receiverEmail != null ? !receiverEmail.equals(that.receiverEmail) : that.receiverEmail != null)
            return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        return amount != null ? amount.equals(that.amount) : that.amount == null;
    }

    @Override
    public int hashCode() {
        int result = senderEmail != null ? senderEmail.hashCode() : 0;
        result = 31 * result + (receiverEmail != null ? receiverEmail.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }
}
