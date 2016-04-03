package SS1.management;

import utils.TextIO;

public class Enrolment_Management extends Management {

    final String tableName = "enrolment";
    final String  fileName = "enrols";

    /**
     *  Enrolment menu
     */
    public void menuEnrolment(){
        try {
            int select;
            while (true){
                TextIO.putln("---------------- ENROLMENTS MANAGE MENU ----------------");
                TextIO.putln("1. Add an enrolment");
                TextIO.putln("2. Enter grade for a student's enrolment");
                TextIO.putln("3. Display a list of all enrolments to HTML ");
                TextIO.putln("4. Display a list of all enrolments sorted in descending order of grade to HTML ");
                TextIO.putln("5. Display a list of enrolments to console ");
                TextIO.putln("6. Display a list of all enrolments to TXT");
                TextIO.putln("7. Back to management menu");
                TextIO.putln("0. Exit");
                TextIO.put("Your choice is: ");
                select = TextIO.getlnInt();
                switch (select){
                    case 1:
                        this.addRow(tableName);
                        break;
                    case 2:
                        this.enterGrade(tableName);
                        break;
                    case 3:
                        this.writeToHTML(this.displayAll(tableName), fileName);
                        break;
                    case 4:
                        this.writeToHTML(this.OrderbyDESC(tableName,"finalgrade"),"enrols_sorted");
                        break;
                    case 5:
                        this.displayConsole(tableName);
                        break;
                    case 6:
                        this.writeToTXT(tableName);
                        break;
                    case 7:
                        this.menuManagement();
                        break;
                    case 0:
                        System.exit(0);
                        break;
                    default:
                        TextIO.putln("Invalid input. Please enter again.\n");
                        this.menuEnrolment();
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
