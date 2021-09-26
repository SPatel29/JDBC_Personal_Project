import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.*;


/*
    What this project does:
        - Connect to an SQL Database using JDBC
        - JDBC allows us to
            * Create SQL Statements in Java
                - such as create, insert, delete, specifiy primary key, foregin key and more.
            * Add additional featuers such as, authorization and ease of executing sql statements via console
                - authorization/privilages are specified above
            * Create Databases
        - Add Privileges to Employee and Admin
        - Uses console to grant users a text-based User Interphase. Helpful for ease of adding sql statements
        - Uses Scanner class to look for user inputs, which provides a more efficient way of inputting data
        - Use looping and other java features to help provide a good user experience

 */

//ADMIN CREDENTIALS:
//host: localhost
//database: postgres
//port: 5432
//username: postgres
//password: 98568216 (Same password as the sql terminal)
//local client: PostgreSQL 13

//Admin Privilages:
    //Create a Databse
    //Add an Employee to database
    //Remove an Employee from database
    //Add Benefits
    //Add Dependent
    //Log out

//Employee Privilages:
    //Request THEIR information using their ID
    //Update their employee information using their ID
    //Log out

//An Employee and Admin have different menus
//Each menu has what privilages is authorized to them

//a stmt is an SQL statement. This helps us add things to our database, or make an entinerely new one
// a conn is a JDBC connection. This helps us connect to an existing database



public class JDBC_Project {
    public static void main (String[] args) throws SQLException {
        boolean employee_signed_in = false;
        boolean admin_signed_in = false;
        boolean logged_in = false;

        String url = "jdbc:postgresql://localhost:5432/postgres";
        Connection conn = DriverManager.getConnection(url, "postgres", "98568216");
        Statement stmt = conn.createStatement();

        do{
            //main menu in do while because what if User wants to log out immediately
            System.out.println("What would you like to do?");
            System.out.println("1. Log in as admin");
            System.out.println("2. Log in as employee");
            System.out.println("3. Log out");

            Scanner scan = new Scanner(System.in);
            String command = scan.nextLine();

            if (command.equals("1")){
                System.out.println("Logging in as an admin!");
                admin_signed_in = true;
                while (admin_signed_in){
                    admin_signed_in = admin_menu(stmt, conn);
                }
                logged_in = false;
            }
            else if (command.equals("2")){
                System.out.println("Logging in as an employee!");
                employee_signed_in = true;
                while (employee_signed_in){
                    employee_signed_in = employee_menu(stmt);
                }

            }
            else if (command.equals("3")){
                System.out.println("Logging out!");
                logged_in = false;
            }
            else{
                System.out.println("Please enter a valid command");
            }

        }while (logged_in); //will keep displaying a message if someone, whether it be a user or admin is logged in.

    }

    //start of datbase creation

    public static void create_insurance_plan(Statement stmt) {
        try {
            String table = "create table insurance_plan(" +
                    "           name varchar(30) primary key," +
                    "           premium_cost bigint" +
                    "       );";
            stmt.executeUpdate(table);
            System.out.println("Insurance Plan Table Created!");
        }catch (Exception ex){
            System.out.println("Error -- "+ex.getMessage() + "\n Please enter a new command");
        }
    }

    public static void create_state(Statement stmt){
        try{

            String table = "create table state("+
                    "           name varchar(30) primary key,"+
                    "           state_tax int"+
                    ");";

            stmt.executeUpdate(table);
            System.out.println("State Table Created!");

        }catch (Exception ex){
            System.out.println("Error -- "+ex.getMessage() + "\n Please enter a new command");
        }
    }

    public static void create_dependent(Statement stmt){
        try{

            String table = "create table dependent("+
                    "           dependent_snn bigint primary key,"+
                    "           relation_to_employee varchar(30),"+
                    "           name varchar(30)"+
                    ");";

            stmt.executeUpdate(table);
            System.out.println("Dependent Table Created!");

        }catch (Exception ex){
            System.out.println("Error -- "+ex.getMessage() + "\n Please enter a new command");
        }
    }

    public static void create_employee(Statement stmt){
        try{

            String table = "create table employee(" +
                    "           employee_ssn bigint primary key," +
                    "           employee_id bigint," +
                    "           name varchar(30)," +
                    "           salary_type varchar(30)," +
                    "           job_title varchar(30)," +
                    "           bonus bigint," +
                    "           yearly_income bigint," +
                    "           state_name varchar(30),"+
                    "           address varchar(30),"+
                    "           insurance_plan_name varchar(30),"+
                    "           foreign key (state_name) references state(name)," +
                    "           foreign key (insurance_plan_name) references insurance_plan(name)"+
                    ");";

            stmt.executeUpdate(table);
            System.out.println("Employee table created!");

        }catch (Exception ex){
            System.out.println("Error -- "+ex.getMessage() + "\n Please enter a new command");
        }
    }

    public static void create_benefits(Statement stmt){
        try{
            String table = "create table benefits("+
                    "           health_plan varchar(30) primary key,"+
                    "           contribution_401k bigint,"+
                    "           attorney_plan varchar(30),"+
                    "           life_insurance bigint,"+
                    "           dental bigint,"+
                    "           vision bigint"+
                    ");";

            stmt.executeUpdate(table);
            System.out.println("Job benefits table created!");
        }catch(Exception ex){
            System.out.println("Error -- "+ex.getMessage() + "\n Please enter a new command");

        }
    }

    public static void create_employee_benefits(Statement stmt){
        try{

            String table = "create table employee_benefits(" +
                    "           employee_ssn bigint,"+
                    "           health_plan varchar(30),"+
                    "           foreign key (health_plan) references benefits(health_plan)," +
                    "           foreign key (employee_ssn) references employee(employee_ssn)"+
                    ");";

            stmt.executeUpdate(table);
            System.out.println("Employee Benefits table created!");

        }catch (Exception ex){
            System.out.println("Error -- "+ex.getMessage() + "\n Please enter a new command");
        }
    }

    public static void create_employee_dependent(Statement stmt){
        try{

            String table = "create table employee_dependent("+
                    "       dependent_ssn bigint,"+
                    "       employee_ssn bigint,"+
                    "       foreign key (employee_ssn) references employee(employee_ssn),"+
                    "       foreign key (dependent_ssn) references dependent(dependent_snn)"+
                    ");";

            stmt.executeUpdate(table);
            System.out.println("Employee Dependent Table Created!");

        }catch(Exception ex){
            System.out.println("Error -- "+ex.getMessage() + "\n Please enter a new command");
        }
    }

    public static void create_dependent_benefits(Statement stmt){
        try{

            String table = "create table dependent_benefits("+
                    "           dependent_ssn bigint,"+
                    "           health_plan varchar(30),"+
                    "           foreign key (dependent_ssn) references dependent(dependent_snn),"+
                    "           foreign key (health_plan) references benefits(health_plan)"+
                    ");";

            stmt.executeUpdate(table);
            System.out.println("Dependent Table Created!");

        }catch (Exception ex){
            System.out.println("Error -- "+ex.getMessage() + "\n Please enter a new command");
        }
    }
    //End of Database Function Creations

    //Start of User functionality
    private static boolean admin_menu(Statement stmt, Connection conn){
        System.out.println("Admin Privilages include:");
        System.out.println("---------------------------------------");
        System.out.println("1. Create a database");
        System.out.println("2. Add an employee to the database");
        System.out.println("3. Remove an employee to the database");
        System.out.println("4. Add Benefits");
        System.out.println("5. Add Dependent");
        System.out.println("6. Log out!");
        String add ="";
        Scanner scan = new Scanner(System.in);
        String command = scan.nextLine();
        if (command.equals("1")){ //command 1 is databse creation
            try{
                //creates all necessary databases. Needed before modifying a database.
                create_insurance_plan(stmt);
                create_state(stmt);
                create_dependent(stmt);
                create_employee(stmt);
                create_benefits(stmt);
                create_employee_benefits(stmt);
                create_employee_dependent(stmt);
                create_dependent_benefits(stmt);
                System.out.println("-------------------"); //for clairity
            }catch (Exception ex){
                System.out.println("Error -- "+ex.getMessage() + "\n Please enter a new command");
            }
        }
        else if (command.equals("2")){ //Command 2 is adding an employee to the employee database
            try {

                //Integer parseInt parses the string to int since our database column is of int type
                System.out.println("State:");
                String state_name = scan.nextLine();
                System.out.println("State Tax: ");
                String state_tax = scan.nextLine();
                add = "insert into state (name, state_tax) values('"+state_name+"', '"+Integer.parseInt(state_tax)+"');" ;
                stmt.executeUpdate(add);
                System.out.println("Employee State added successfully");
                System.out.println("\n\n\n");

                System.out.println("Insurance Plan Name:");
                String insurance_plan_name = scan.nextLine();
                System.out.println("Premium Cost Name:");
                String premium_cost = scan.nextLine();
                add = "insert into insurance_plan (name, premium_cost) values('"+insurance_plan_name+"', '"+Integer.parseInt(premium_cost)+"');" ;
                stmt.executeUpdate(add);
                System.out.println("Insurance Plan Added!");

                System.out.println("SSN:");
                String ssn = scan.nextLine();

                System.out.println("ID:");
                String id = scan.nextLine();

                System.out.println("Name:");
                String name = scan.nextLine();

                System.out.println("Salary Type:");
                String salary_type = scan.nextLine();

                System.out.println("Job Title:");
                String job_title = scan.nextLine();

                System.out.println("Bonus:");
                String bonus = scan.nextLine();

                System.out.println("Yearly Income");
                String yearly_income = scan.nextLine();

                System.out.println("Address:");
                String address = scan.nextLine();

                add = "insert into employee (employee_ssn, employee_id, name, salary_type, job_title, bonus, yearly_income,state_name, address, " +
                        "insurance_plan_name) values('"+Integer.parseInt(ssn)+"' , '"+Integer.parseInt(id)+"', '"+name+"','"+salary_type+"', '"+job_title+"', '"+Integer.parseInt(bonus)+"', '"+Integer.parseInt(yearly_income)+"', '"+state_name+"', '"+address+"', '"+insurance_plan_name+"');  ";

                stmt.executeUpdate(add);
                System.out.println("Employee added");
            }catch (Exception ex){
                System.out.println("Error -- "+ex.getMessage() + "\n Please enter a new command");
            }

        }
        else if (command.equals("3")){ //Command 3 is deleting an employee from database
            try{
                System.out.println("ID:");
                String id = scan.nextLine();
                String remove = "delete from employee where employee_id = '"+Integer.parseInt(id)+"';";
                stmt.executeUpdate(remove);
                System.out.println("Employee removed!");
            }catch (Exception ex){
                System.out.println("Error -- "+ex.getMessage() + "\n Please enter a new command");
            }
        }
        else if (command.equals("4")){ //Command 4 is adding a health plan
            try{
                System.out.println("Health Plan:");
                String plan = scan.nextLine();
                System.out.println("Contribution 401k");
                String contrib_401k = scan.nextLine();
                System.out.println("Attorney:");
                String attorney = scan.nextLine();
                System.out.println("Life Insurance:");
                String life_insurance = scan.nextLine();
                System.out.println("Dental:");
                String dental = scan.nextLine();
                System.out.println("Vision:");
                String vision = scan.nextLine();

                add = "insert into benefits (health_plan, contribution_401k, attorney_plan, life_insurance, dental, vision" +
                        ") values('"+plan+"' , '"+Integer.parseInt(contrib_401k)+"', '"+attorney+"','"+Integer.parseInt(life_insurance)+"', '"+Integer.parseInt(dental) +"', '"+Integer.parseInt(vision)+"');  ";
                stmt.executeUpdate(add);
                System.out.println("Benefits Added!");
            }catch (Exception ex){
                System.out.println("Error -- "+ex.getMessage() + "\n Please enter a new command");
            }
        }
        else if (command.equals("5")){ //Command 5 is adding all dependent information
            try{

                System.out.println("Enter Dependent SSN:");
                String ssn = scan.nextLine();
                System.out.println("Enter relation to Employee");
                String relation = scan.nextLine();
                System.out.println("Enter name of dependent");
                String name = scan.nextLine();
                System.out.println("Enter Health Plan");
                String plan = scan.nextLine();
                System.out.println("Enter Employee SSN");
                String employee_ssn = scan.nextLine();

                add = "insert into dependent (dependent_snn, relation_to_employee, name" +
                        ") values('"+Integer.parseInt(ssn)+"' , '"+relation+"', '"+name+"');  ";
                stmt.executeUpdate(add);
                System.out.println("Added Dependent!");

                add = "insert into dependent_benefits (dependent_ssn, health_plan" +
                        ") values('"+Integer.parseInt(ssn)+"' , '"+plan+"');  ";
                stmt.executeUpdate(add);
                System.out.println("Added Dependent Benefits!");

                add = "insert into employee_dependent (dependent_ssn, employee_ssn" +
                        ") values('"+Integer.parseInt(ssn)+"' , '"+Integer.parseInt(employee_ssn)+"');  ";
                stmt.executeUpdate(add);
                System.out.println("Added Dependent Info from Employee!");

            }catch (Exception ex){
                System.out.println("Error -- "+ex.getMessage() + "\n Please enter a new command");
            }
        }
        else{ //out quit option
            try{
                stmt.close(); //closing our sql statements for safety
                conn.close(); //closing database connections for safety
            }catch (Exception ex){
                System.out.println("Error -- "+ex.getMessage() + "\n Please enter a new command");
            }
            System.out.println("Logging out!");
            return false;
        }
        return true;
    }

    //End of Admin Menu/Functionality



    //Start of User Menu/Functionality

    private static boolean employee_menu(Statement stmt){ //employee menu is for employee. Describes their privilages
        System.out.println("Employee Privilages inlude:");
        System.out.println("1. Pull employee information");
        System.out.println("2. Update Employee information");
        System.out.println("3. Quit");

        Scanner scan = new Scanner(System.in);
        String command = scan.nextLine();
        if (command.equals("1")){ //command 1 is pulling an employee's own information
            try{
                System.out.println("ID:");
                String id = scan.nextLine();
                String findUser = "select employee_ssn, name from employee where employee_id = '"+Integer.parseInt(id)+"';";
                stmt.executeUpdate(findUser);
            }catch (Exception ex){
                System.out.println("Error -- "+ex.getMessage() + "\n Please enter a new command");
            }
        }
        else if (command.equals("2")){ //command 2 is  upating an employee informtion
            try{
                System.out.println("ID:");
                String id = scan.nextLine();

                System.out.println("What would you like to change?");
                System.out.println("1. Name");
                System.out.println("2. State Name");
                System.out.println("3. Address");
                String option = scan.nextLine();
                String change = ""; //made them local variables to method since we use them multiple times
                String update = "";
                if (option.equals("1")){
                    System.out.println("New Name:");
                    change = scan.nextLine();
                    update = "update employee set name = '"+change+"' where employee_id = '"+Integer.parseInt(id)+"';";
                }
                else if (option.equals("2")){
                    System.out.println("New State Name:");
                    change = scan.nextLine();
                    update = "update employee set state_name = '"+change+"' where employee_id = '"+Integer.parseInt(id)+"';";

                }
                else if (option.equals("3")){
                    option = "address";
                    System.out.println("New Address:");
                    change = scan.nextLine();
                    update = "update employee set address = '"+change+"' where employee_id = '"+Integer.parseInt(id)+"';";
                }
                else {
                    System.out.println("Please Choose a valid option. Try Again");
                }
                stmt.executeUpdate(update);
            }catch (Exception ex){
            }
        }
        else{ //else statement is when employee wants to sign out or quit
            return false;
        }
        return true; //returns true if employee decides to do anything other than quit
    }

    //End of User Functionality/Menu

}
