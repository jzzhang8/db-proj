  
import java.sql.*;
import java.util.Scanner;
import java.io.*;

class Main {


    public static void main(String[] args) throws IOException, SQLException{
        String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db8";
        String dbUsername = "Group8";
        String dbPassword = "8puorg";

        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
        } catch (ClassNotFoundException e) {
            System.out.println("[Error]: Java MySQL DB Driver not found!!");
            System.exit(0);
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("SQL Connection error!");
            System.exit(0);
        }

        Administrator admin = new Administrator(con);
        // LibraryUser libusr = new LibraryUser(con);
        // Librarian librarian = new Librarian(con);
        // LibraryDirector libdir = new LibraryDirector(con);

        System.out.println("Welcome to library inquiry system!\n");
        String menuInfo = "-----Main menu-----\nWhat kinds of operation would you like to perform?\n1. Operations for administrator\n2. Operations for library user\n3. Operations for librarian\n4. Operations for library director\n5. Exit this program\n";
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.print(menuInfo);
            System.out.print("Enter Your Choice: ");
            String choice = sc.nextLine();
            if(choice.equals("1")){
                admin.operation();
            }else if(choice.equals("2")){
                //libusr.operation();
            }else if(choice.equals("3")){
                //librarian.operation();
            }else if(choice.equals("4")){
                //libdir.operation();
            }else if(choice.equals("5")){
                System.exit(0);
            }else{
                // invalid choice 
                System.out.println("Invalid choice!");
                continue;
            }

        }


    }
}
