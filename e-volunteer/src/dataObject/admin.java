package dataObject;

public class admin {
    private int adminID;
    private String adFname;
    private String adLname;
    private String email;
    private String password;

    public admin() {}

    public admin(int adminID, String adFname, String adLname, String email, String password) {
        this.adminID = adminID;
        this.adFname = adFname;
        this.adLname = adLname;
        this.email = email;
        this.password = password;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    public void setAdFname(String adFname) {
        this.adFname = adFname;
    }

    public void setAdLname(String adLname) {
        this.adLname = adLname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAdminID() {
        return adminID;
    }

    public String getAdFname() {
        return adFname;
    }

    public String getAdLname() {
        return adLname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}