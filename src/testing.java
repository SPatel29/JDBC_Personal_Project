import java.sql.*;
import java.util.*;


            /*
            String table = "create table insurance_plan(" +
                    "           name varchar(30) primary key," +
                    "           premium_cost bigint" +
                    "       );";
            String add;
            stmt.executeUpdate(table);
            System.out.println("Database created!");
            */

public class testing {
    public static void main (String[] args) throws SQLException {

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

        String url = "jdbc:postgresql://localhost:5432/postgres";
        Connection conn = DriverManager.getConnection(url, "postgres", "98568216");
        Statement stmt = conn.createStatement();

        try{
            create_insurance_plan(stmt); //creates the insurance plan table
            create_state(stmt);
            create_dependent(stmt);
            create_employee(stmt); //creates the employee table
            create_benefits(stmt);
            create_employee_benefits(stmt);
            create_employee_dependent(stmt);
            create_dependent_benefits(stmt);

            String add;
            add = "insert into insurance_plan (name, premium_cost) values('Sunny ', '10 ');  "   ;
            System.out.println("After add");
            stmt.executeUpdate(add);
            System.out.println("After execute add");
            stmt.close();
            conn.close();
        }catch (Exception ex){
            System.out.println("Error -- "+ex.getMessage() + "\n Please enter a new command");
        }



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



}
