package dataObject;

public class resources {
    private int resID;
    private String resName;
    private int resQuan;
    private double resFund;
    private String resDateAllocated;
    private int serviceId;

    public resources() {}

    public resources(int resID, String resName, int resQuan, double resFund, String resDateAllocated, int serviceId) {
        this.resID = resID;
        this.resName = resName;
        this.resQuan = resQuan;
        this.resFund = resFund;
        this.resDateAllocated = resDateAllocated;
        this.serviceId = serviceId;
    }

    public void setResID(int resID) {
        this.resID = resID;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public void setResQuan(int resQuan) {
        this.resQuan = resQuan;
    }

    public void setResFund(double resFund) {
        this.resFund = resFund;
    }

    public void setResDateAllocated(String resDateAllocated) {
        this.resDateAllocated = resDateAllocated;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getResID() {
        return resID;
    }

    public String getResName() {
        return resName;
    }

    public int getResQuan() {
        return resQuan;
    }

    public double getResFund() {
        return resFund;
    }

    public String getResDateAllocated() {
        return resDateAllocated;
    }

    public int getServiceId() {
        return serviceId;
    }
}