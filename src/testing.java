import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.*;


//ADMIN CREDENTIALS:
//host: localhost
//database: postgres
//port: 5432
//username: postgres
//password: 98568216 (Same password as the sql terminal)
//local client: PostgreSQL 13

//Note: there are no credentials for employee.
//we know if someone is an employee is they do not sign in
// as an admin. No need for employee credentials, in other words

//only thing an employee can do is request THEIR information.
//Nothing else

            /*
            String table = "create table insurance_plan(" +
                    "           name varchar(30) primary key," +
                    "           premium_cost bigint" +
                    "       );";
            String add;
            stmt.executeUpdate(table);
            System.out.println("Database created!");
            */

            /*
            String add;
            add = "insert into insurance_plan (name, premium_cost) values('Sunny ', '10 ');  "   ;
            System.out.println("After add");
            stmt.executeUpdate(add);
            System.out.println("After execute add");
            */

public class testing {
    public static void main (String[] args) throws SQLException {
        boolean employee_signed_in = false;
        boolean admin_signed_in = false;
        boolean logged_in = false;

        String url = "jdbc:postgresql://localhost:5432/postgres";
        Connection conn = DriverManager.getConnection(url, "postgres", "98568216");
        Statement stmt = conn.createStatement();

        do{
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

        }while (logged_in);

    }

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
    private static boolean admin_menu(Statement stmt, Connection conn){
        System.out.println("Admin Privilages include:");
        System.out.println("---------------------------------------");
        System.out.println("1. Create a database");
        System.out.println("2. Add an employee to the database");
        System.out.println("3. Remove an employee to the database");
        System.out.println("4. Log out!");
        Scanner scan = new Scanner(System.in);
        String command = scan.nextLine();
        if (command.equals("1")){
            try{
                create_insurance_plan(stmt); //creates the insurance plan table
                create_state(stmt);
                create_dependent(stmt);
                create_employee(stmt); //creates the employee table
                create_benefits(stmt);
                create_employee_benefits(stmt);
                create_employee_dependent(stmt);
                create_dependent_benefits(stmt);
                System.out.println("--------------");
            }catch (Exception ex){
                System.out.println("Error -- "+ex.getMessage() + "\n Please enter a new command");
            }
        }
        else if (command.equals("2")){ //adding an employee
            try {
                String add;
                /*
                System.out.println("SSN:");
                int ssn = scan.nextInt();

                System.out.println("ID:");
                int id = scan.nextInt();

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

                System.out.println("State Name:");
                String state_name = scan.nextLine();

                System.out.println("Address:");
                String address = scan.nextLine();

                System.out.println("Insurance Plan Name:");
                String insurance_plan_name = scan.nextLine();
                */
                // String add = "insert into employee (employee_ssn, name, salary_type, job_title, bonus, federal_tax, yearly_income, "
                //                    + "payment_date, payment_amount, state_name, insurance_plan) values('" + ssn + "', '" + name + "','"
                //                    + salaryType + "','" + jobTitle + "','" + bonus + "','" + federalTax + "','" + yearlyIncome + "','" + paymentDate + "','" + paymentAmount + "','" + state + "','" + insurance + "');";
                add = "insert into state (name, state_tax) values('IL', 120);" ;
                stmt.executeUpdate(add);
                System.out.println("Line after state add execute");

                add = "insert into insurance_plan (name, premium_cost) values('HPO', 120);" ;
                stmt.executeUpdate(add);
                System.out.println("Line after state insurance plan execute");

                add = "insert into employee (employee_ssn, employee_id, name, salary_type, job_title, bonus, yearly_income,state_name, address, " +
                        "insurance_plan_name) values(12 , 21 , 'Sunny','Salary', 'Intern', 2999, 10000, 'IL', '123 Main', 'HPO');  ";
                System.out.println("Line after employee");
                stmt.executeUpdate(add);
                System.out.println("Employee added");
            }catch (Exception ex){
                System.out.println("Error -- "+ex.getMessage() + "\n Please enter a new command");
            }

        }
        else if (command.equals("3")){
            try{
                String remove = "delete from employee where employee_id = '"+21+"';";
                //String remove = " delete from employee where employee_id = '"id "';";
                stmt.executeUpdate(remove);
                System.out.println("Employee removed!");
            }catch (Exception ex){
                System.out.println("Error -- "+ex.getMessage() + "\n Please enter a new command");
            }
        }
        else{
            try{
                stmt.close();
                conn.close();
            }catch (Exception ex){
                System.out.println("Error -- "+ex.getMessage() + "\n Please enter a new command");
            }
            System.out.println("Logging out!");
            return false;
        }
        return true;
    }

    private static boolean employee_menu(Statement stmt){
        System.out.println("Employee Privilages inlude:");
        System.out.println("");
        System.out.println("1. Pull employee information");
        System.out.println("2. Update Employee information");

        Scanner scan = new Scanner(System.in);
        String command = scan.nextLine();
        if (command.equals("1")){
            try{
                String findUser = "select employee_ssn, namefrom employee where employee_id = '"+21+"';";
                stmt.executeUpdate(findUser);
            }catch (Exception ex){
                System.out.println("Error -- "+ex.getMessage() + "\n Please enter a new command");
            }
        }
        else if (command.equals("2")){
            try{
                String update = "update employee set name = 'Tom' where employee_id = 21;";
                stmt.executeUpdate(update);
            }catch (Exception ex){
            }
        }
        else{
            return false;
        }
        return true;
    }
}
