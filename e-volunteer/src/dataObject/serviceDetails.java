package dataObject;

public class serviceDetails {
    private int volId;
    private int servSchedID;
    private String status;

    public serviceDetails() {}

    public serviceDetails(int volId, int servSchedID, String status) {
        this.volId = volId;
        this.servSchedID = servSchedID;
        this.status = status;
    }

    public void setVolId(int volId) {
        this.volId = volId;
    }

    public void setServSchedID(int servSchedID) {
        this.servSchedID = servSchedID;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getVolId() {
        return volId;
    }

    public int getServSchedID() {
        return servSchedID;
    }

    public String getStatus() {
        return status;
    }
}