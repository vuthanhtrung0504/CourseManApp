package SS1.management;

import utils.TextIO;

public class Reports extends Management {
    /**
     * Report menu
     */
    public void menuReports(){
        try {
            int select;
            while (true){
                TextIO.putln("---------------- REPORTS MENU ----------------");
                TextIO.putln("1. Display a list of all courses of a given student");
                TextIO.putln("2. Display all students of a given course");
                TextIO.putln("3. Display a list of all students who have failed at least one course");
                TextIO.putln("4. Back to management menu");
                TextIO.putln("0. Exit");
                TextIO.put("Your choice is: ");
                select = TextIO.getlnInt();
                switch (select){
                    case 1:
                        this.displayConsole("student");
                        this.writeToHTML(this.enrolStudent(),"my_enrols");
                        break;
                    case 2:
                        this.displayConsole("course");
                        this.writeToHTML(this.enrolCourse(),"course_enrols");
                        break;
                    case 3:
                        this.writeToHTML(this.failGrade(),"fails");
                        break;
                    case 4:
                        this.menuManagement();
                        break;
                    case 0:
                        System.exit(0);
                        break;
                    default:
                        TextIO.putln("Invalid input. Please enter again.\n");
                        this.menuReports();
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
