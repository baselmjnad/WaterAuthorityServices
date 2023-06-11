package Classes;

public class RequestDetails {
    public Integer id;
    public Byte[] document;
    public String newSubAddress;
    public String newSubType;
    public Integer requestId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte[] getDocument() {
        return document;
    }

    public void setDocument(Byte[] document) {
        this.document = document;
    }

    public String getNewSubAddress() {
        return newSubAddress;
    }

    public void setNewSubAddress(String newSubAddress) {
        this.newSubAddress = newSubAddress;
    }

    public String getNewSubType() {
        return newSubType;
    }

    public void setNewSubType(String newSubType) {
        this.newSubType = newSubType;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }
}
