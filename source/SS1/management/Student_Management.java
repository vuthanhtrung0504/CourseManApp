package SS1.management;

import utils.TextIO;

public class Student_Management extends Management {

    final String tableName="student";
    final String fileName="students";

    /**
     * Student menu
     */
    public void menuStudent(){
        try {
            int select;
           while (true) {
               TextIO.putln("---------------- STUDENTS MANAGE MENU ----------------");
               TextIO.putln("1. Add a new student");
               TextIO.putln("2. Edit information of an existing student");
               TextIO.putln("3. Delete a student");
               TextIO.putln("4. Display a list of all students to HTML");
               TextIO.putln("5. Display a list of all students to console");
               TextIO.putln("6. Display a list of all students to TXT");
               TextIO.putln("7. Back to management menu");
               TextIO.putln("0. Exit");
               TextIO.put("Your choice is: ");
               select = TextIO.getlnInt();
               switch (select) {
                   case 1:
                       this.addRow(tableName);
                       break;
                   case 2:
                       this.editRow(tableName);
                       break;
                   case 3:
                       this.deleteRow(tableName);
                       break;
                   case 4:
                       this.writeToHTML(this.displayAll(tableName), fileName);
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
                       this.menuStudent();
                       break;
               }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
