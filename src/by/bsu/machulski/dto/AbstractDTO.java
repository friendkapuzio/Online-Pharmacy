package by.bsu.machulski.dto;

public class AbstractDTO {
    private long id;

    public AbstractDTO() {
    }

    public AbstractDTO(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
