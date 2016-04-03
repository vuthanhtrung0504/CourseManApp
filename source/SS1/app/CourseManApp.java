package SS1.app;

import SS1.management.Management;

import java.sql.SQLException;

public class CourseManApp {

    private static Management management = new Management();

    /**
     *  run menu management
     */
    public static void runApp()
    {
        management.menuManagement();
    }

    public static void main(String[] args) throws SQLException {
        CourseManApp.runApp();
    }
}
