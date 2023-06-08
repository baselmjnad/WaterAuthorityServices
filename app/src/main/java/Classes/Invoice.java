package Classes;

public class Invoice {
    public Integer id;
    public Integer invoiceYear;
    public Integer invoiceCycle;
    public Integer invoiceValue;
    public Boolean invoiceStatus;
    public Subscription subscription;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInvoiceYear() {
        return invoiceYear;
    }

    public void setInvoiceYear(Integer invoiceYear) {
        this.invoiceYear = invoiceYear;
    }

    public Integer getInvoiceCycle() {
        return invoiceCycle;
    }

    public void setInvoiceCycle(Integer invoiceCycle) {
        this.invoiceCycle = invoiceCycle;
    }

    public Integer getInvoiceValue() {
        return invoiceValue;
    }

    public void setInvoiceValue(Integer invoiceValue) {
        this.invoiceValue = invoiceValue;
    }

    public Boolean getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(Boolean invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }
}
