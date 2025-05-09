package dataObject;

public class beneficiary {
    private int benID;
    private String benFname;
    private String benLname;
    private String benAddress;
    private int benContact;
    private String benType;

    public beneficiary() {}

    public beneficiary(int benID, String benFname, String benLname, String benAddress, int benContact, String benType) {
        this.benID = benID;
        this.benFname = benFname;
        this.benLname = benLname;
        this.benAddress = benAddress;
        this.benContact = benContact;
        this.benType = benType;
    }

    public void setBenID(int benID) {
        this.benID = benID;
    }

    public void setBenFname(String benFname) {
        this.benFname = benFname;
    }

    public void setBenLname(String benLname) {
        this.benLname = benLname;
    }

    public void setBenAddress(String benAddress) {
        this.benAddress = benAddress;
    }

    public void setBenContact(int benContact) {
        this.benContact = benContact;
    }

    public void setBenType(String benType) {
        this.benType = benType;
    }

    public int getBenID() {
        return benID;
    }

    public String getBenFname() {
        return benFname;
    }

    public String getBenLname() {
        return benLname;
    }

    public String getBenAddress() {
        return benAddress;
    }

    public int getBenContact() {
        return benContact;
    }

    public String getBenType() {
        return benType;
    }
}