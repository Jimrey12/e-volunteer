package dataObject;

public class serviceSchedule {
    private int servSchedId;
    private int serviceId;
    private String date;
    private String timeStart;
    private String timeEnd;
    private String venue;
    private int slots;
    private int maxSlots;

    public serviceSchedule() {}

    public serviceSchedule(int servSchedId, int serviceId, String date, String timeStart, String timeEnd, String venue, int slots, int maxSlots) {
        this.servSchedId = servSchedId;
        this.serviceId = serviceId;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.venue = venue;
        this.slots = slots;
        this.maxSlots = maxSlots;
    }

    public void setServSchedId(int servSchedId) {
        this.servSchedId = servSchedId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public void setMaxSlots(int maxSlots) {
        this.maxSlots = maxSlots;
    }

    public int getServSchedId() {
        return servSchedId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public String getDate() {
        return date;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public String getVenue() {
        return venue;
    }

    public int getSlots() {
        return slots;
    }

    public int getMaxSlots() {
        return maxSlots;
    }
}