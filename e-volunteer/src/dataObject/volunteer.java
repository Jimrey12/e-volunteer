package dataObject;

public class volunteer {
    private int volId;
    private String volFname;
    private String volLname;
    private String volContact;

    public volunteer() {}

    public volunteer(int volId, String volFname, String volLname, String volContact) {
        this.volId = volId;
        this.volFname = volFname;
        this.volLname = volLname;
        this.volContact = volContact;
    }

    public void setVolId(int volId) {
        this.volId = volId;
    }

    public void setVolFname(String volFname) {
        this.volFname = volFname;
    }

    public void setVolLname(String volLname) {
        this.volLname = volLname;
    }

    public void setVolContact(String volContact) {
        this.volContact = volContact;
    }

    public int getVolId() {
        return volId;
    }

    public String getVolFname() {
        return volFname;
    }

    public String getVolLname() {
        return volLname;
    }

    public String getVolContact() {
        return volContact;
    }
}