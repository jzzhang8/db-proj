  
import java.sql.*;
import java.util.*;
import java.io.*;

class Administrator {
	private Connection con;
	private String[] tablenames;

	public Administrator(Connection con_in){
		this.con = con_in;
		this.tablenames = new String[6];
		tablenames[0] = "category";
		tablenames[1] = "libuser";
		tablenames[2] = "book";
		tablenames[3] = "copy";
		tablenames[4] = "borrow";
		tablenames[5] = "authorship";
	}

	public void createTables() throws SQLException{
		String[] ctable = new String[6];
		ctable[0] = "CREATE TABLE category( cid integer primary key, max integer, period integer);";
		ctable[1] = "CREATE TABLE libuser( libuid varchar(10) primary key, name varchar(25), address varchar(100), cid integer);";
		ctable[2] = "CREATE TABLE book( callnum varchar(8) primary key, title varchar(30), publish date );";
		ctable[3] = "CREATE TABLE copy( callnum varchar(8), copynum integer);";
		ctable[4] = "CREATE TABLE borrow(libuid integer, callnum varchar(8), copynum integer, checkout date, return date);";
		ctable[5] = "CREATE TABLE authorship(aname varchar(25), callnum varchar(8) );";

		Statement stmt = con.createStatement();
		DatabaseMetaData meta = con.getMetaData();

		// check whether table exists
		for(int i = 0; i < 6; i++){
			ResultSet table_exists = meta.getTables(null, null, tablenames[i], null);
			if(table_exists.next()){
				System.out.println("\""+tablenames[i]+"\" table already exists! Delete all tables before create them.");
				return;
			}
		}

		System.out.print("Processing...");
		for (int i = 0; i < 6 ; i++) {
			stmt.executeUpdate(ctable[i]);
		}
		System.out.println("Done! Database is initialized!");

	}

	public void deleteTables() throws SQLException{
		String[] dtable = new String[6];
		dtable[0] = "DROP TABLE IF EXISTS category;";
		dtable[1] = "DROP TABLE IF EXISTS libuser;";
		dtable[2] = "DROP TABLE IF EXISTS book;";
		dtable[3] = "DROP TABLE IF EXISTS copy;";
		dtable[4] = "DROP TABLE IF EXISTS borrow;";
		dtable[5] = "DROP TABLE IF EXISTS authorship;";

		Statement stmt = con.createStatement();

		System.out.print("Processing...");
		for(int i = 0; i < 6 ; i++){
			stmt.executeUpdate(dtable[i]);
		}
		System.out.println("Done! Database is removed!");

	}

	public void loadData() throws IOException, SQLException{
		DatabaseMetaData meta = con.getMetaData();
		Statement stmt = con.createStatement();

		// check whether table exists
		for(int i = 0; i < 6; i++){
			ResultSet table_exists = meta.getTables(null, null, tablenames[i], null);
			if(!table_exists.next()){
				System.out.println("\""+tablenames[i]+"\" table not exist! Create all tables before load the data.");
				return;
			}
		}
		
		Scanner fcategory = new Scanner(new File("category.txt"));
		while(fcategory.hasNextLine()){
			String line = fcategory.nextLine();
			String[] lineSplit = line.split("\t");

			int cid = Integer.valueOf(lineSplit[0]);
			int max = Integer.valueOf(lineSplit[1]);
			int period = Integer.valueOf(lineSplit[2]);
			PreparedStatement pstmt = con.prepareStatement("INSERT INTO category VALUES (?, ?, ?);");
			pstmt.setInt(1,cid);
			pstmt.setInt(2,max);
			pstmt.setInt(3,period);

			pstmt.executeUpdate();

		}

		Scanner fuser = new Scanner(new File("user.txt"));
		while(fuser.hasNextLine()){
			String line = fuser.nextLine();
			String[] lineSplit = line.split("\t");

			String libuid = lineSplit[0];
			String name = lineSplit[1];
			String address = lineSplit[2];
			int cid = Integer.valueOf(lineSplit[3]);
			PreparedStatement pstmt = con.prepareStatement("INSERT INTO libuser VALUES (?, ?, ?, ?);");
			pstmt.setString(1,libuid);
			pstmt.setString(2,name);
			pstmt.setString(3,address);
			pstmt.setInt(4,cid);

			pstmt.executeUpdate();

		}


		Scanner fbook = new Scanner(new File("book.txt"));
		while(fbook.hasNextLine()){
			String line = fbook.nextLine();
			String[] lineSplit = line.split("\t");

			String callnum = lineSplit[0];
			int numOfCopies = Integer.valueOf(lineSplit[1]);
			String title = lineSplit[2];
			String authors = lineSplit[3];
			String publish = lineSplit[4]; // date for publish
			PreparedStatement pstmt_book = con.prepareStatement("INSERT INTO book VALUES (?, ?, to_date(?, 'dd/mm/yyyy'));");
			pstmt_book.setString(1,callnum);
			pstmt_book.setString(2,title);
			pstmt_book.setString(3,publish);

			pstmt_book.executeUpdate();

			String[] authorsSplit = authors.split(",");
			for(int i = 0; i < authorsSplit.length; i++){
				PreparedStatement pstmt_authorship = con.prepareStatement("INSERT INTO authorship VALUES (?, ?);");
				String aname = authorsSplit[i];
				pstmt_authorship.setString(1, aname);	
				pstmt_authorship.setString(2, callnum);	

				pstmt_authorship.executeUpdate();
			}
			


			

		}


		Scanner fcheck_out = new Scanner(new File("check_out.txt"));
		while(fcheck_out.hasNextLine()){
			String line = fbook.nextLine();
			String[] lineSplit = line.split("\t");

			String callnum = lineSplit[0];
			int copynum = Integer.valueOf(lineSplit[1]);
			String libuid = lineSplit[2];
			String checkout = lineSplit[3];
			String return_date = lineSplit[4];
			// insert into borrow and copy
			String borrow_str_after_setNull;
			if(return_date.equals("null")){
				borrow_str_after_setNull = "INSERT INTO borrow VALUES (?, ?, ?, to_date(?, 'dd/mm/yyyy'), null);";
			}else{
				borrow_str_after_setNull = "INSERT INTO borrow VALUES (?, ?, ?, to_date(?, 'dd/mm/yyyy'), to_date(?, 'dd/mm/yyyy'));";
			} 

			PreparedStatement pstmt_borrow = con.prepareStatement(borrow_str_after_setNull);
			PreparedStatement pstmt_copy = con.prepareStatement("INSERT INTO copy VALUES (?, ?);");

			pstmt_borrow.setString(1, libuid);
			pstmt_borrow.setString(2, callnum);
			pstmt_borrow.setInt(3, copynum);
			pstmt_borrow.setString(4, checkout);
			if(!return_date.equals("null")){ //if return date is not null
				pstmt_borrow.setString(5, return_date);	
			}
			
			pstmt_borrow.executeUpdate();

			pstmt_copy.setString(1, callnum);
			pstmt_copy.setInt(2, copynum);

			pstmt_copy.executeUpdate();
		}


	}

	public void showRecordNum() throws SQLException{

		for(int i = 0; i < 6; i++){
			String pstr = String.format("SELECT COUNT(*) FROM %s;", tablenames[i]);
			PreparedStatement pstmt = con.prepareStatement(pstr);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				int num = rs.getInt(1);
				System.out.println(tablenames[i]+": "+num);
			}else{
				System.out.println(tablenames[i]+": show record number error\n");
			}
		}

	}

	public void operation() throws IOException, SQLException{
		Scanner sc = new Scanner(System.in);

		String menuInfo = "-----Operations for administrator menu-----\nWhat kinds of operation would you like to perform?\n1. Create all tables\n2. Delete all tables\n3. Load from datafile\n4. Show number of records in each table\n5. Return to the main menu\n"; 

		while(true){
			System.out.print(menuInfo);
			System.out.print("Enter Your Choice: ");
			String choice = sc.nextLine();
            if(choice.equals("1")){
            	createTables();
            }else if(choice.equals("2")){
            	deleteTables();
            }else if(choice.equals("3")){
            	loadData();
            }else if(choice.equals("4")){
            	showRecordNum();
            }else if(choice.equals("5")){
            	return;
            }else{
                // invalid choice 
                System.out.println("Invalid choice!");
                continue;
            }
		}
	}


}
