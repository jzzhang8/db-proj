import java.sql.*;
import java.util.*;

class LibraryDirector {
    private Connection con;

    public LibraryDirector(Connection c) {
        con = c;
    }

    /**
     * Perform operations of a LibraryDirector once.
     *
     * @return 0 on success, else return other int
     */
    public int act() {
        System.out.println("-----Operations for library director menu-----");
        System.out.println("What kinds of operation would you like to perform?");
        System.out.println("1. List all un-returned book copies which are checked-out within a period");
        System.out.println("2. Return to the main menu");
        System.out.print("Enter Your Choice: ");

        Scanner sc = new Scanner(System.in);
        int choice = Integer.parseInt(sc.nextLine());

        if (choice != 1) {
            return choice;
        }

        System.out.print("Type in the starting date [dd/mm/yyyy]: ");
        String startingDate = sc.nextLine();

        System.out.print("Type in the ending date [dd/mm/yyyy]: ");
        String endingDate = sc.nextLine();

        listUnreturnedBookCopy(startingDate, endingDate);
        return 0;
    }

    private void listUnreturnedBookCopy(String startingDate, String endingDate) {
        System.out.println("List of UnReturned Book:");
        System.out.println("|LibUID|CallNum|CopyNum|Checkout|");

        //TODO

        System.out.println("End of Query");
    }

    public static void main(String[] args) {
        LibraryDirector ld = new LibraryDirector(null);
        while (ld.act() == 0) {}
    }
}
