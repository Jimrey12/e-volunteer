package dataObject;

public class service {
    private int serviceID;
    private String serviceDetails;
    private int maxNumVol;

    public service() {}

    public service(int serviceID, String serviceDetails, int maxNumVol) {
        this.serviceID = serviceID;
        this.serviceDetails = serviceDetails;
        this.maxNumVol = maxNumVol;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public void setServiceDetails(String serviceDetails) {
        this.serviceDetails = serviceDetails;
    }

    public void setMaxNumVol(int maxNumVol) {
        this.maxNumVol = maxNumVol;
    }

    public int getServiceID() {
        return serviceID;
    }

    public String getServiceDetails() {
        return serviceDetails;
    }

    public int getMaxNumVol() {
        return maxNumVol;
    }
}