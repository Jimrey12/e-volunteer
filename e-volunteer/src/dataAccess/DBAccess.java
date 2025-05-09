package dataAccess;

import connectionSQL.DBConnection;
import dataObject.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBAccess {

    public static boolean loginAdmin(int adminId, String password) {
        try {
            String query = "SELECT * FROM admin WHERE AdminID = ? AND Password = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(query);
            ps.setInt(1, adminId);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                admin adminObj = new admin();
                adminObj.setAdminID(rs.getInt("AdminID"));
                adminObj.setAdFname(rs.getString("AdminFname"));
                adminObj.setAdLname(rs.getString("AdminLname"));
                adminObj.setEmail(rs.getString("Email"));
                adminObj.setPassword(rs.getString("Password"));
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("loginAdmin failed: " + e.getMessage());
            return false;
        }
    }

    public static boolean loginVolunteer(int volunteerId, String contact) {
        try {
            String query = "SELECT * FROM volunteer WHERE VolunteerID = ? AND VolContact = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(query);
            ps.setInt(1, volunteerId);
            ps.setString(2, contact);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                volunteer volObj = new volunteer();
                volObj.setVolId(rs.getInt("VolunteerID"));
                volObj.setVolFname(rs.getString("VolFname"));
                volObj.setVolLname(rs.getString("VolLname"));
                volObj.setVolContact(rs.getString("VolContact"));
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("loginVolunteer failed: " + e.getMessage());
            return false;
        }
    }

    public static volunteer signupVolunteer(String fname, String lname, String birthDate, String contact) {
        try {
            String query = "{CALL SignupVolunteer(?, ?, ?, ?, ?)}";
            CallableStatement stmt = DBConnection.getConnection().prepareCall(query);
            stmt.setString(1, fname);
            stmt.setString(2, lname);
            stmt.setDate(3, Date.valueOf(birthDate));
            stmt.setString(4, contact);
            stmt.registerOutParameter(5, Types.INTEGER);
            stmt.execute();

            int volunteerId = stmt.getInt(5);
            if (volunteerId > 0) {
                volunteer volObj = new volunteer();
                volObj.setVolId(volunteerId);
                volObj.setVolFname(fname);
                volObj.setVolLname(lname);
                volObj.setVolContact(contact);
                return volObj;
            }
            return null;
        } catch (SQLException e) {
            System.err.println("signupVolunteer failed: " + e.getMessage());
            return null;
        }
    }

    public static List<service> getServices() {
        try {
            Statement st = DBConnection.getConnection().createStatement();
            ResultSet rs = st.executeQuery("SELECT ServiceID, ServiceDetails, MaxNumVolunteers FROM service");
            List<service> services = new ArrayList<>();
            while (rs.next()) {
                service svc = new service();
                svc.setServiceID(rs.getInt("ServiceID"));
                svc.setServiceDetails(rs.getString("ServiceDetails"));
                svc.setMaxNumVol(rs.getInt("MaxNumVolunteers"));
                services.add(svc);
            }
            return services;
        } catch (SQLException e) {
            System.err.println("getServices failed: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static List<service> getServices(String searchTerm) {
        try {
            String query = "SELECT ServiceID, ServiceDetails, MaxNumVolunteers FROM service WHERE ServiceID LIKE ? OR ServiceDetails LIKE ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(query);
            ps.setString(1, "%" + searchTerm + "%");
            ps.setString(2, "%" + searchTerm + "%");
            ResultSet rs = ps.executeQuery();
            List<service> services = new ArrayList<>();
            while (rs.next()) {
                service svc = new service();
                svc.setServiceID(rs.getInt("ServiceID"));
                svc.setServiceDetails(rs.getString("ServiceDetails"));
                svc.setMaxNumVol(rs.getInt("MaxNumVolunteers"));
                services.add(svc);
            }
            return services;
        } catch (SQLException e) {
            System.err.println("getServices with search failed: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static boolean addService(String serviceDetails, int maxNumVolunteers) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            String query = "INSERT INTO service (ServiceDetails, MaxNumVolunteers) VALUES (?, ?)";
            ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, serviceDetails);
            ps.setInt(2, maxNumVolunteers);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    conn.commit();
                    return true;
                }
            }
            conn.rollback();
            return false;
        } catch (SQLException e) {
            System.err.println("addService failed: " + e.getMessage());
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Rollback failed: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Cleanup failed: " + e.getMessage());
            }
        }
    }

    public static boolean deleteService(int serviceId) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Check for dependencies in serviceschedule, beneficiary, and resource
            String checkQuery = "SELECT (SELECT COUNT(*) FROM serviceschedule WHERE ServiceID = ?) + " +
                    "(SELECT COUNT(*) FROM beneficiary WHERE ServiceID = ?) + " +
                    "(SELECT COUNT(*) FROM resource WHERE ServiceID = ?) AS total";
            PreparedStatement psCheck = conn.prepareStatement(checkQuery);
            psCheck.setInt(1, serviceId);
            psCheck.setInt(2, serviceId);
            psCheck.setInt(3, serviceId);
            ResultSet rs = psCheck.executeQuery();
            if (rs.next() && rs.getInt("total") > 0) {
                System.err.println("Cannot delete service: It is referenced by schedules, beneficiaries, or resources.");
                return false;
            }

            // Delete the service
            String query = "DELETE FROM service WHERE ServiceID = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, serviceId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                conn.commit();
                System.out.println("Service deleted successfully with ID: " + serviceId);
                return true;
            }
            conn.rollback();
            System.err.println("Failed to delete service: No rows affected.");
            return false;
        } catch (SQLException e) {
            System.err.println("deleteService failed: " + e.getMessage());
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Rollback failed: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Cleanup failed: " + e.getMessage());
            }
        }
    }

    public static boolean updateService(int serviceId, String serviceDetails, int maxNumVolunteers) {
        try {
            String query = "UPDATE service SET ServiceDetails = ?, MaxNumVolunteers = ? WHERE ServiceID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(query);
            ps.setString(1, serviceDetails);
            ps.setInt(2, maxNumVolunteers);
            ps.setInt(3, serviceId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("updateService failed: " + e.getMessage());
            return false;
        }
    }

    public static List<serviceSchedule> getSchedules() {
        try {
            String query = "SELECT ss.ServSchedID, ss.ServiceID, ss.Date, ss.`Time Start`, ss.`Time End`, ss.Venue, ss.Slots, s.MaxNumVolunteers " +
                    "FROM serviceschedule ss " +
                    "JOIN service s ON ss.ServiceID = s.ServiceID";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            List<serviceSchedule> schedules = new ArrayList<>();
            while (rs.next()) {
                serviceSchedule sched = new serviceSchedule();
                sched.setServSchedId(rs.getInt("ServSchedID"));
                sched.setServiceId(rs.getInt("ServiceID"));
                sched.setDate(rs.getDate("Date").toString());
                sched.setTimeStart(rs.getTime("Time Start").toString());
                sched.setTimeEnd(rs.getTime("Time End").toString());
                sched.setVenue(rs.getString("Venue"));
                sched.setSlots(rs.getInt("Slots"));
                sched.setMaxSlots(rs.getInt("MaxNumVolunteers"));
                schedules.add(sched);
            }
            return schedules;
        } catch (SQLException e) {
            System.err.println("getSchedules failed: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static List<serviceSchedule> searchSchedules(int serviceId, String searchTerm, int volunteerId) {
        try {
            String query;
            List<serviceSchedule> schedules = new ArrayList<>();
            PreparedStatement ps;

            if (volunteerId == 0) { // Admin mode: no volunteer restrictions
                query = "SELECT ss.ServSchedID, ss.ServiceID, ss.Date, ss.`Time Start`, ss.`Time End`, ss.Venue, ss.Slots, s.MaxNumVolunteers " +
                        "FROM serviceschedule ss " +
                        "JOIN service s ON ss.ServiceID = s.ServiceID " +
                        "WHERE (ss.ServSchedID LIKE ? OR ss.Venue LIKE ?)";
                ps = DBConnection.getConnection().prepareStatement(query);
                ps.setString(1, "%" + searchTerm + "%");
                ps.setString(2, "%" + searchTerm + "%");
            } else { // Volunteer mode: original logic
                query = "SELECT ss.ServSchedID, ss.ServiceID, ss.Date, ss.`Time Start`, ss.`Time End`, ss.Venue, ss.Slots, s.MaxNumVolunteers " +
                        "FROM serviceschedule ss " +
                        "JOIN service s ON ss.ServiceID = s.ServiceID " +
                        "WHERE ss.ServiceID = ? AND (ss.Venue LIKE ? OR ss.ServSchedID LIKE ?) " +
                        "AND ss.Slots < s.MaxNumVolunteers " +
                        "AND ss.ServSchedID NOT IN (" +
                        "   SELECT sd.ServSchedID FROM servicedetails sd " +
                        "   WHERE sd.VolunteerID = ? AND sd.Status IN ('Pending', 'Completed')" +
                        ")";
                ps = DBConnection.getConnection().prepareStatement(query);
                ps.setInt(1, serviceId);
                ps.setString(2, "%" + searchTerm + "%");
                ps.setString(3, "%" + searchTerm + "%");
                ps.setInt(4, volunteerId);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                serviceSchedule sched = new serviceSchedule();
                sched.setServSchedId(rs.getInt("ServSchedID"));
                sched.setServiceId(rs.getInt("ServiceID"));
                sched.setDate(rs.getDate("Date").toString());
                sched.setTimeStart(rs.getTime("Time Start").toString());
                sched.setTimeEnd(rs.getTime("Time End").toString());
                sched.setVenue(rs.getString("Venue"));
                sched.setSlots(rs.getInt("Slots"));
                sched.setMaxSlots(rs.getInt("MaxNumVolunteers"));
                schedules.add(sched);
            }
            return schedules;
        } catch (SQLException e) {
            System.err.println("searchSchedules failed: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static boolean updateSchedule(int schedId, int serviceId, String date, String timeStart, String timeEnd, String venue) {
        try {
            String query = "UPDATE serviceschedule SET ServiceID = ?, Date = ?, `Time Start` = ?, `Time End` = ?, Venue = ? WHERE ServSchedID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(query);
            ps.setInt(1, serviceId);
            ps.setDate(2, Date.valueOf(date));
            ps.setTime(3, Time.valueOf(timeStart));
            ps.setTime(4, Time.valueOf(timeEnd));
            ps.setString(5, venue);
            ps.setInt(6, schedId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("updateSchedule failed: " + e.getMessage());
            return false;
        }
    }

    public static boolean bookService(int volunteerId, int scheduleId) {
        Connection conn = null;
        PreparedStatement psCheck = null;
        PreparedStatement psSlotCheck = null;
        PreparedStatement psBook = null;
        PreparedStatement psUpdate = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Check if volunteer already booked this schedule
            String checkQuery = "SELECT COUNT(*) FROM servicedetails WHERE VolunteerID = ? AND ServSchedID = ? AND Status IN ('Pending', 'Completed')";
            psCheck = conn.prepareStatement(checkQuery);
            psCheck.setInt(1, volunteerId);
            psCheck.setInt(2, scheduleId);
            ResultSet rsCheck = psCheck.executeQuery();
            if (rsCheck.next() && rsCheck.getInt(1) > 0) {
                return false; // Already booked
            }

            // Check if slots are available
            String slotCheckQuery = "SELECT ss.Slots, s.MaxNumVolunteers FROM serviceschedule ss " +
                    "JOIN service s ON ss.ServiceID = s.ServiceID WHERE ss.ServSchedID = ?";
            psSlotCheck = conn.prepareStatement(slotCheckQuery);
            psSlotCheck.setInt(1, scheduleId);
            ResultSet rsSlot = psSlotCheck.executeQuery();
            if (!rsSlot.next() || rsSlot.getInt("Slots") >= rsSlot.getInt("MaxNumVolunteers")) {
                return false; // No slots available
            }

            // Book the service
            String bookQuery = "INSERT INTO servicedetails (VolunteerID, ServSchedID, Status) VALUES (?, ?, 'Pending')";
            psBook = conn.prepareStatement(bookQuery);
            psBook.setInt(1, volunteerId);
            psBook.setInt(2, scheduleId);
            psBook.executeUpdate();

            // Update slots
            String updateQuery = "UPDATE serviceschedule SET Slots = Slots + 1 WHERE ServSchedID = ?";
            psUpdate = conn.prepareStatement(updateQuery);
            psUpdate.setInt(1, scheduleId);
            psUpdate.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Rollback failed: " + ex.getMessage());
            }
            System.err.println("bookService failed: " + e.getMessage());
            return false;
        } finally {
            try {
                if (psCheck != null) psCheck.close();
                if (psSlotCheck != null) psSlotCheck.close();
                if (psBook != null) psBook.close();
                if (psUpdate != null) psUpdate.close();
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Cleanup failed: " + e.getMessage());
            }
        }
    }

    public static List<Object[]> getParticipations(int volunteerId, String searchTerm) {
        try {
            String query = "SELECT sd.ServSchedID, sd.VolunteerID, sd.Status, s.ServiceDetails, ss.Date, " +
                    "ss.`Time Start`, ss.`Time End`, ss.Venue " +
                    "FROM servicedetails sd " +
                    "JOIN serviceschedule ss ON sd.ServSchedID = ss.ServSchedID " +
                    "JOIN service s ON ss.ServiceID = s.ServiceID " +
                    "WHERE sd.VolunteerID = ? AND (sd.ServSchedID LIKE ? OR s.ServiceDetails LIKE ?)";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(query);
            ps.setInt(1, volunteerId);
            ps.setString(2, "%" + searchTerm + "%");
            ps.setString(3, "%" + searchTerm + "%");
            ResultSet rs = ps.executeQuery();
            List<Object[]> participations = new ArrayList<>();
            while (rs.next()) {
                Object[] participation = new Object[8];
                participation[0] = rs.getInt("ServSchedID");
                participation[1] = rs.getString("ServiceDetails");
                participation[2] = rs.getDate("Date").toString();
                participation[3] = rs.getTime("Time Start").toString();
                participation[4] = rs.getTime("Time End").toString();
                participation[5] = rs.getString("Venue");
                participation[6] = rs.getString("Status");
                participation[7] = false; // Checkbox state
                participations.add(participation);
            }
            return participations;
        } catch (SQLException e) {
            System.err.println("getParticipations failed: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static boolean cancelParticipation(int volunteerId, int scheduleId) {
        Connection conn = null;
        PreparedStatement psUpdateDetails = null;
        PreparedStatement psUpdateSlots = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Update service details status
            String updateDetailsQuery = "UPDATE servicedetails SET Status = 'Cancelled' " +
                    "WHERE VolunteerID = ? AND ServSchedID = ? AND Status = 'Pending'";
            psUpdateDetails = conn.prepareStatement(updateDetailsQuery);
            psUpdateDetails.setInt(1, volunteerId);
            psUpdateDetails.setInt(2, scheduleId);
            int rows = psUpdateDetails.executeUpdate();
            if (rows == 0) {
                return false; // No pending participation found
            }

            // Decrement slots
            String updateSlotsQuery = "UPDATE serviceschedule SET Slots = Slots - 1 WHERE ServSchedID = ? AND Slots > 0";
            psUpdateSlots = conn.prepareStatement(updateSlotsQuery);
            psUpdateSlots.setInt(1, scheduleId);
            psUpdateSlots.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Rollback failed: " + ex.getMessage());
            }
            System.err.println("cancelParticipation failed: " + e.getMessage());
            return false;
        } finally {
            try {
                if (psUpdateDetails != null) psUpdateDetails.close();
                if (psUpdateSlots != null) psUpdateSlots.close();
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Cleanup failed: " + e.getMessage());
            }
        }
    }

    public static volunteer getProfile(int volunteerId) {
        try {
            String query = "SELECT VolunteerID, VolFname, VolLname, `Birth Date`, VolContact FROM volunteer WHERE VolunteerID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(query);
            ps.setInt(1, volunteerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                volunteer volObj = new volunteer();
                volObj.setVolId(rs.getInt("VolunteerID"));
                volObj.setVolFname(rs.getString("VolFname"));
                volObj.setVolLname(rs.getString("VolLname"));
                volObj.setVolContact(rs.getString("VolContact"));
                return volObj;
            }
            return null;
        } catch (SQLException e) {
            System.err.println("getProfile failed: " + e.getMessage());
            return null;
        }
    }

    public static boolean updateProfile(volunteer volObj) {
        try {
            String query = "UPDATE volunteer SET VolFname = ?, VolLname = ?, `Birth Date` = ?, VolContact = ? WHERE VolunteerID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(query);
            ps.setString(1, volObj.getVolFname());
            ps.setString(2, volObj.getVolLname());
            ps.setDate(3, Date.valueOf("2000-01-01")); // Placeholder
            ps.setString(4, volObj.getVolContact());
            ps.setInt(5, volObj.getVolId());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("updateProfile failed: " + e.getMessage());
            return false;
        }
    }

    public static boolean closeAccount(int volunteerId) {
        try {
            String query = "DELETE FROM volunteer WHERE VolunteerID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(query);
            ps.setInt(1, volunteerId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("closeAccount failed: " + e.getMessage());
            return false;
        }
    }

    public static List<Object[]> getBeneficiaries() {
        try {
            String query = "SELECT BeneficiaryID, BenFname, BenLname, BenAddress, BenContact, BenType, ServiceID FROM beneficiary";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            List<Object[]> beneficiaries = new ArrayList<>();
            while (rs.next()) {
                Object[] beneficiary = new Object[7];
                beneficiary[0] = rs.getInt("BeneficiaryID");
                beneficiary[1] = rs.getString("BenFname");
                beneficiary[2] = rs.getString("BenLname");
                beneficiary[3] = rs.getString("BenAddress");
                beneficiary[4] = rs.getString("BenContact");
                beneficiary[5] = rs.getString("BenType");
                beneficiary[6] = rs.getInt("ServiceID");
                beneficiaries.add(beneficiary);
            }
            return beneficiaries;
        } catch (SQLException e) {
            System.err.println("getBeneficiaries failed: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static List<Object[]> getBeneficiaries(String searchTerm) {
        try {
            String query = "SELECT BeneficiaryID, BenFname, BenLname, BenAddress, BenContact, BenType, ServiceID FROM beneficiary " +
                    "WHERE BeneficiaryID LIKE ? OR BenFname LIKE ? OR BenLname LIKE ? OR BenAddress LIKE ? OR BenType LIKE ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(query);
            ps.setString(1, "%" + searchTerm + "%");
            ps.setString(2, "%" + searchTerm + "%");
            ps.setString(3, "%" + searchTerm + "%");
            ps.setString(4, "%" + searchTerm + "%");
            ps.setString(5, "%" + searchTerm + "%");
            ResultSet rs = ps.executeQuery();
            List<Object[]> beneficiaries = new ArrayList<>();
            while (rs.next()) {
                Object[] beneficiary = new Object[7];
                beneficiary[0] = rs.getInt("BeneficiaryID");
                beneficiary[1] = rs.getString("BenFname");
                beneficiary[2] = rs.getString("BenLname");
                beneficiary[3] = rs.getString("BenAddress");
                beneficiary[4] = rs.getString("BenContact");
                beneficiary[5] = rs.getString("BenType");
                beneficiary[6] = rs.getInt("ServiceID");
                beneficiaries.add(beneficiary);
            }
            return beneficiaries;
        } catch (SQLException e) {
            System.err.println("getBeneficiaries with search failed: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static boolean addBeneficiary(String fname, String lname, String address, String contact, String type, int serviceId) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            String query = "INSERT INTO beneficiary (BenFname, BenLname, BenAddress, BenContact, BenType, ServiceID) VALUES (?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, fname);
            ps.setString(2, lname);
            ps.setString(3, address);
            ps.setString(4, contact);
            ps.setString(5, type);
            ps.setInt(6, serviceId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    conn.commit();
                    System.out.println("Beneficiary added successfully with ID: " + generatedKeys.getInt(1));
                    return true;
                }
            }
            conn.rollback();
            System.err.println("Failed to add beneficiary: No rows affected or no generated keys returned.");
            return false;
        } catch (SQLException e) {
            System.err.println("addBeneficiary failed: " + e.getMessage());
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Rollback failed: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Cleanup failed: " + e.getMessage());
            }
        }
    }

    public static boolean deleteBeneficiary(int beneficiaryId) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            String query = "DELETE FROM beneficiary WHERE BeneficiaryID = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, beneficiaryId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                conn.commit();
                System.out.println("Beneficiary deleted successfully with ID: " + beneficiaryId);
                return true;
            }
            conn.rollback();
            System.err.println("Failed to delete beneficiary: No rows affected.");
            return false;
        } catch (SQLException e) {
            System.err.println("deleteBeneficiary failed: " + e.getMessage());
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Rollback failed: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Cleanup failed: " + e.getMessage());
            }
        }
    }

    public static boolean updateBeneficiary(int beneficiaryId, String fname, String lname, String address, String contact, String type, int serviceId) {
        try {
            String query = "UPDATE beneficiary SET BenFname = ?, BenLname = ?, BenAddress = ?, BenContact = ?, BenType = ?, ServiceID = ? WHERE BeneficiaryID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(query);
            ps.setString(1, fname);
            ps.setString(2, lname);
            ps.setString(3, address);
            ps.setString(4, contact);
            ps.setString(5, type);
            ps.setInt(6, serviceId);
            ps.setInt(7, beneficiaryId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("updateBeneficiary failed: " + e.getMessage());
            return false;
        }
    }

    public static List<Object[]> getResources() {
        try {
            String query = "SELECT ResourceId, ResourceName, ResourceQuantity, ResourceFunds, ResourceDateAllocated, ServiceID FROM resource";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            List<Object[]> resources = new ArrayList<>();
            while (rs.next()) {
                Object[] resource = new Object[6];
                resource[0] = rs.getInt("ResourceId");
                resource[1] = rs.getString("ResourceName");
                resource[2] = rs.getObject("ResourceQuantity") != null ? rs.getInt("ResourceQuantity") : null;
                resource[3] = rs.getObject("ResourceFunds") != null ? rs.getDouble("ResourceFunds") : null;
                resource[4] = rs.getDate("ResourceDateAllocated").toString();
                resource[5] = rs.getInt("ServiceID");
                resources.add(resource);
            }
            return resources;
        } catch (SQLException e) {
            System.err.println("getResources failed: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static List<Object[]> getResources(String searchTerm) {
        try {
            String query = "SELECT ResourceId, ResourceName, ResourceQuantity, ResourceFunds, ResourceDateAllocated, ServiceID FROM resource " +
                    "WHERE ResourceId LIKE ? OR ResourceName LIKE ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(query);
            ps.setString(1, "%" + searchTerm + "%");
            ps.setString(2, "%" + searchTerm + "%");
            ResultSet rs = ps.executeQuery();
            List<Object[]> resources = new ArrayList<>();
            while (rs.next()) {
                Object[] resource = new Object[6];
                resource[0] = rs.getInt("ResourceId");
                resource[1] = rs.getString("ResourceName");
                resource[2] = rs.getObject("ResourceQuantity") != null ? rs.getInt("ResourceQuantity") : null;
                resource[3] = rs.getObject("ResourceFunds") != null ? rs.getDouble("ResourceFunds") : null;
                resource[4] = rs.getDate("ResourceDateAllocated").toString();
                resource[5] = rs.getInt("ServiceID");
                resources.add(resource);
            }
            return resources;
        } catch (SQLException e) {
            System.err.println("getResources with search failed: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static boolean addResource(String name, Integer quantity, Double funds, String dateAllocated, int serviceId) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            String query = "INSERT INTO resource (ResourceName, ResourceQuantity, ResourceFunds, ResourceDateAllocated, ServiceID) VALUES (?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            if (quantity != null) {
                ps.setInt(2, quantity);
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            if (funds != null) {
                ps.setDouble(3, funds);
            } else {
                ps.setNull(3, Types.DOUBLE);
            }
            ps.setDate(4, Date.valueOf(dateAllocated));
            ps.setInt(5, serviceId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    conn.commit();
                    System.out.println("Resource added successfully with ID: " + generatedKeys.getInt(1));
                    return true;
                }
            }
            conn.rollback();
            System.err.println("Failed to add resource: No rows affected or no generated keys returned.");
            return false;
        } catch (SQLException e) {
            System.err.println("addResource failed: " + e.getMessage());
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Rollback failed: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Cleanup failed: " + e.getMessage());
            }
        }
    }

    public static boolean deleteResource(int resourceId) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            String query = "DELETE FROM resource WHERE ResourceId = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, resourceId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                conn.commit();
                System.out.println("Resource deleted successfully with ID: " + resourceId);
                return true;
            }
            conn.rollback();
            System.err.println("Failed to delete resource: No rows affected.");
            return false;
        } catch (SQLException e) {
            System.err.println("deleteResource failed: " + e.getMessage());
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Rollback failed: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Cleanup failed: " + e.getMessage());
            }
        }
    }

    public static boolean updateResource(int resourceId, String name, Integer quantity, Double funds, String dateAllocated, int serviceId) {
        try {
            String query = "UPDATE resource SET ResourceName = ?, ResourceQuantity = ?, ResourceFunds = ?, ResourceDateAllocated = ?, ServiceID = ? WHERE ResourceId = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(query);
            ps.setString(1, name);
            if (quantity != null) {
                ps.setInt(2, quantity);
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            if (funds != null) {
                ps.setDouble(3, funds);
            } else {
                ps.setNull(3, Types.DOUBLE);
            }
            ps.setDate(4, Date.valueOf(dateAllocated));
            ps.setInt(5, serviceId);
            ps.setInt(6, resourceId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("updateResource failed: " + e.getMessage());
            return false;
        }
    }

        // Retrieve all participations with optional search filter
        public static List<Object[]> getAllParticipations(String searchQuery) {
            List<Object[]> participations = new ArrayList<>();
            String query = "SELECT sd.ServSchedID, sd.VolunteerID, s.ServiceDetails, ss.Date, ss.`Time Start`, ss.`Time End`, ss.Venue, sd.Status " +
                    "FROM servicedetails sd " +
                    "JOIN serviceschedule ss ON sd.ServSchedID = ss.ServSchedID " +
                    "JOIN service s ON ss.ServiceID = s.ServiceID " +
                    "WHERE s.ServiceDetails LIKE ? OR ss.Venue LIKE ? OR sd.Status LIKE ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                String searchPattern = "%" + searchQuery + "%";
                stmt.setString(1, searchPattern);
                stmt.setString(2, searchPattern);
                stmt.setString(3, searchPattern);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    participations.add(new Object[]{
                            rs.getInt("ServSchedID"),
                            rs.getInt("VolunteerID"),
                            rs.getString("ServiceDetails"),
                            rs.getDate("Date"),
                            rs.getTime("Time Start"),
                            rs.getTime("Time End"),
                            rs.getString("Venue"),
                            rs.getString("Status")
                    });
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return participations;
        }

        // Update participation status using stored procedure
        public static boolean updateParticipationStatus(int servSchedId, int volunteerId, String newStatus) {
            try (Connection conn = DBConnection.getConnection();
                 CallableStatement stmt = conn.prepareCall("{CALL UpdateParticipationStatus(?, ?, ?)}")) {
                stmt.setInt(1, servSchedId);
                stmt.setInt(2, volunteerId);
                stmt.setString(3, newStatus);
                stmt.execute();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
