import java.sql.*;
import java.util.*;

class Librarian {
    private Connection con;

    public Librarian(Connection c) {
        con = c;
    }

    /**
     * Perform operations of a Librarian once.
     *
     * @return 0 on success, else return other int
     */
    public int act() {
        System.out.println("-----Operations for librarian menu-----");
        System.out.println("What kinds of operation would you like to perform?");
        System.out.println("1. Book Borrowing");
        System.out.println("2. Book Returning");
        System.out.println("3. Return to the main menu");

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Your Choice: ");
        int choice = Integer.parseInt(sc.nextLine());

        if (choice != 1 && choice != 2) {
            return choice;
        }

        System.out.print("Enter The User ID: ");
        String userId = sc.nextLine();

        System.out.print("Enter The Call Number: ");
        String callNumber = sc.nextLine();

        System.out.print("Enter The Copy Number: ");
        int copyNumber = Integer.parseInt(sc.nextLine());

        switch (choice) {
        case 1:
            borrowBookCopy(userId, callNumber, copyNumber);
            break;
        case 2:
            returnBookCopy(userId, callNumber, copyNumber);
            break;
        }

        return 0;
    }

    private void borrowBookCopy(String userId, String callNumber, int copyNumber) {
        //TODO
        System.out.println("Book borrowing performed successfully!!!");
    }

    private void returnBookCopy(String userId, String callNumber, int copyNumber) {
        //TODO
        System.out.println("Book returning performed successfully!!!");
    }

    public static void main(String[] args) {
        Librarian l = new Librarian(null);
        while (l.act() == 0) {}
    }
}
