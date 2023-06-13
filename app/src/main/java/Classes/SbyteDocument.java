package Classes;

public class SbyteDocument {
    public Integer id;
    public ServicesRequest request;
    public byte[] document;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ServicesRequest getRequest() {
        return request;
    }

    public void setRequest(ServicesRequest request) {
        this.request = request;
    }

    public byte[] getDocument() {
        return document;
    }

    public void setDocument(byte[] document) {
        this.document = document;
    }
}
