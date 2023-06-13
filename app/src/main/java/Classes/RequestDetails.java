package Classes;

public class RequestDetails {
    public Integer id;
    public byte[] document;
    public String newSubAddress;
    public String newSubType;


    public byte[] getDocument() {
        return document;
    }

    public void setDocument(byte[] document) {
        this.document = document;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

}
