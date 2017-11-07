package by.bsu.machulski.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction extends Entity {
    private long senderId;
    private long receiverId;
    private BigDecimal amount;
    private Date date;

    public Transaction() {
    }

    public Transaction(long id, long senderId, long receiverId, BigDecimal amount, Date date) {
        super(id);
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.date = date;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Transaction that = (Transaction) o;

        if (senderId != that.senderId) return false;
        if (receiverId != that.receiverId) return false;
        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        return date != null ? date.equals(that.date) : that.date == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (senderId ^ (senderId >>> 32));
        result = 31 * result + (int) (receiverId ^ (receiverId >>> 32));
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
