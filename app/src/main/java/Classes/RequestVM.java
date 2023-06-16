package Classes;

import java.util.Date;

public class RequestVM {
    public ServicesRequest request;
    public Date finishDate;
    public Department rejectedBy;
    public String rejectNotes;

    public ServicesRequest getRequest() {
        return request;
    }

    public void setRequest(ServicesRequest request) {
        this.request = request;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public Department getRejectedBy() {
        return rejectedBy;
    }

    public void setRejectedBy(Department rejectedBy) {
        this.rejectedBy = rejectedBy;
    }

    public String getRejectNotes() {
        return rejectNotes;
    }

    public void setRejectNotes(String rejectNotes) {
        this.rejectNotes = rejectNotes;
    }
}
