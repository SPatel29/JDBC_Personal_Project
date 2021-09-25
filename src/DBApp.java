import java.sql.*;
import java.util.*;

public class DBApp {

    public static final String employeeCommands  = "\n\t COMMANDS: \n\n" +  " signin -- allows an employee to sign in\n"
            + " signup -- allows creation of new employee\n" + " signout -- signs out of app and returns to main menu\n"
            + " update -- allows employee to update info\n" + " view paycheck -- allows employee to see their paycheck\n"
            + " --help -- prints out command list \n quit -- terminates program and signs out \n\n\n";
    public static final String managerCommands = employeeCommands + "\n\n\t MANAGER COMMANDS: \n\n"
            + " update performance -- update employee performance\n" + " update bonus -- update any employee's bonus\n\n\n";
    public static final String adminCommands = managerCommands + " \t ADMIN COMMANDS: \n\n" + " create -- creates database\n"
            + " admin update -- update any database information\n" + " add employee -- allows admin to add new employee to database\n"
            + " remove employee -- allows admin to remove employee based on employee id\n"
            + " run report -- allows admin to run report \n";
    private static boolean signedIn = false;
    private static boolean manager = false;
    private static boolean admin = false;
    private static int userCount = 1;
    private static String adminUser = "postgres";
    private static String adminPassword = "0000";
    private static ArrayList<String> w2 = new ArrayList<String>(10);
    private static ArrayList<String> paychecks = new ArrayList<String>(10);
    private static String expenseReport;
    private static String currentUser = "", currentPassword="", port= "5432", host="localhost", database="postgres";
    private static int currentId = -1; // -1 is not signed in, 0 is admin, positive int is employee


    public static void main(String[] args) throws SQLException {
        //createDb("postgres", "0000", port, host, database);
        boolean running = true;
        Scanner scan = new Scanner(System.in);
        System.out.println("\n Enter a command: \n\n signin -- allows an employee to sign in \n signup -- allows creation of new employee\n "
                + "quit -- terminates program and signs out\n");

        while (running) {  // menu
            if (signedIn) {System.out.println("Enter a command or type --help for commands list: ");}

            scan = new Scanner(System.in);
            try {
                String command = scan.nextLine();
                switch (command) {
                    case "create" : if (admin) {
                        System.out.println(" Enter database name:");
                        String newDatabase = scan.nextLine();
                        if (database != newDatabase) {
                            System.out.println(" Enter the user, password, port, and host.");
                            currentUser = scan.nextLine();
                            currentPassword = scan.nextLine();
                            port = scan.nextLine();
                            host = scan.nextLine();
                            database = newDatabase;
                            createDb(currentUser, currentPassword, port, host, database);
                        }
                        else {
                            System.out.println(" Database already created. Please select another command.");
                        }
                    }
                    else {
                        System.out.println("You do not have persmission for this command. Please sign in or try another command.");
                    }
                        break;
                    case "signin":System.out.println(" Enter username and password.");
                        currentUser = scan.nextLine();
                        currentPassword = scan.nextLine();
                        signin(currentUser, currentPassword);
                        break;

                    case "signup": System.out.println(" Enter user info: (employee name, password, ssn, benefit");
                        String name = scan.nextLine();
                        String password = scan.nextLine();
                        int ssn = scan.nextInt();
                        String benefit = scan.nextLine();
                        signup(name, password, ssn, benefit);
                        break;

                    case "signout": System.out.println(" Signout successfull. \n\n\n");
                        signedIn = false;
                        currentUser = "";
                        manager = false;
                        admin = false;
                        currentId = -1;
                        currentPassword = "";
                        System.out.println("\n Enter a command: \n\n signin -- allows an employee to sign in "
                                + "\n signup -- allows creation of new employee\n "
                                + "quit -- terminates program and signs out\n");
                        break;

                    case "update": if (signedIn) {
                        System.out.println(" Enter 'dependent', 'insurance', or 'address' to update dependent, insurance, or address.");
                        String update = scan.nextLine();
                        updateAccount(update);
                    }

                    else {
                        System.out.println("You do not have persmission for this command. Please sign in or try another command.");
                    }
                        break;

                    case "view paycheck": if(signedIn) {
                        viewPaycheck();
                    }
                        break;

                    case "view report": if (signedIn && (manager || admin)) {
                        while (true) {
                            try {
                                System.out.println("\n Enter the report number that you want to see or type 'quit' to return to menu: ");
                                String type = scan.nextLine();
                                if (type.equals("quit")) {break;}

                                viewReport(type);
                                break;
                            }
                            catch (Exception x) {
                                System.out.println(" Error -- no report matching that number. ");
                            }
                        }
                    }
                    else {
                        System.out.println(" You do not have persmission for this command or report has not been printed."
                                + " Please sign in or try another command.");
                    }

                        break;

                    case "--help": if (admin) {
                        System.out.println(adminCommands);
                    }
                    else if (manager) {
                        System.out.println(managerCommands);
                    }
                    else {
                        System.out.println(employeeCommands);
                    }
                        break;

                    case "update bonus": if (signedIn && (admin  || manager )) {
                        System.out.println("Enter employeeid: ");
                        int employee = scan.nextInt();
                        updateBonus(employee);
                    }
                    else {
                        System.out.println("You do not have persmission for this command. "
                                + "Please sign in or try another command.");
                    }
                        break;
                    case "update performance": if (signedIn &&(admin||manager)) {
                        System.out.println(" Enter employee id: ");
                        int id = scan.nextInt();
                        updatePerformance(id);
                    }
                    case "admin update": if (signedIn && admin ) {
                        System.out.println("Enter employee number: ");
                        int employee = scan.nextInt();
                        adminUpdate(employee);
                    }
                    else {
                        System.out.println("You do not have persmission for this command. "
                                + "Please sign in or try another command.");
                    }
                        break;

                    case "add employee": if (signedIn  && admin ) {
                        System.out.println("Enter employee information: (ssn, name, salary type, job title, bonus, federal tax, "
                                + "yearly income, payment date, payment amount, state name, insurance plan)");
                        ssn = scan.nextInt();
                        name = scan.nextLine();
                        String salaryType = scan.nextLine();
                        String jobTitle = scan.nextLine();
                        String bonus = scan.nextLine();
                        String federalTax = scan.nextLine();
                        String yearlyIncome = scan.nextLine();
                        String paymentDate = scan.nextLine();
                        String paymentAmount = scan.nextLine();
                        String state = scan.nextLine();
                        String insurance = scan.nextLine();
                        addEmployee(ssn, name, salaryType, jobTitle, bonus, federalTax, yearlyIncome, paymentDate, paymentAmount, state, insurance);
                    }
                    else {
                        System.out.println("You do not have persmission for this command. Please sign in or try another command.");
                    }
                        break;
                    case "remove employee": if (signedIn && admin ) {
                        System.out.println(" Enter employee id you want to remove: ");
                        int id = scan.nextInt();
                        removeEmployee(id);
                    }
                    else {
                        System.out.println("You do not have persmission for this command. Please sign in or try another command.");

                    }
                        break;

                    case "run report": if (signedIn && admin) {
                        System.out.println("\n Are you running yearly expense reports? Type: 'y' or 'n': ");
                        String expense = scan.nextLine();
                        boolean yearlyExpenses;
                        if (expense.equals("y")) {yearlyExpenses = true;}
                        else if (expense.equals("n")) {yearlyExpenses = false;}
                        else {
                            System.out.println(" Error -- incorrect argument for yearly expsense report. ");
                            yearlyExpenses = false;
                            break;
                        }
                        runReport(yearlyExpenses);
                    }
                    else {
                        System.out.println(" You do not have persmission for this command. Please sign in or try another command.");
                    }
                        break;
                    case "quit": running = false;
                        System.out.println(" Program terminated. Goodbye. ");
                        break;

                    default: System.out.println(" Error -- you might not have permission or you might have entered an incorrect command.\n Please enter "
                            + "another command, or type '--help' for command list.");


                }

            }
            catch (Exception E) {
                System.out.println(" Please enter a valid command. Type '--help' for command list.\n"
                        + "Enter a command: ");
            }
        }

        scan.close();


    }

    public static void createDb(String user, String password, String port, String host, String database) {

        try {
            String tables = "create table insurance_plan("
                    + "	name varchar(30) primary key,"
                    + "	premium_cost bigint"
                    + ");"

                    + "create table dependent("
                    + "	dependent_ssn bigint primary key,"
                    + "	relation_to_employee varchar(30),"
                    + "	name varchar(30)"
                    + ");"

                    + "create table benefits("
                    + "	health_plan varchar(30) primary key,"
                    + "	contribution_401k bigint,"
                    + "	attorney_plan varchar(30),"
                    + "	life_insurance bigint,"
                    + "	dental bigint,"
                    + "	vision bigint"
                    + ");"


                    + "create table state("
                    + "	name varchar(30) primary key,"
                    + "	state_tax int"
                    + ");"


                    + " create table employee ("
                    + "	employee_ssn bigint primary key,"
                    + " employee_id bigint,"
                    + "	name varchar(30),"
                    + "	salary_type varchar(30),"
                    + "	job_title varchar(30),"
                    + "	bonus bigint,"
                    + "	federal_tax bigint,"
                    + "	yearly_income bigint,"
                    + "	payment_date varchar(30),"
                    + "	payment_amount bigint,"
                    + "	state_name varchar(30),"
                    + "	insurance_plan_name varchar(30), "
                    + "	foreign key (state_name) references state(name),"
                    + "	foreign key (insurance_plan_name) references insurance_plan(name),"
                    + " address varchar(30),"
                    + " performance varchar(30)"
                    + ");"



                    + "create table employee_benefits("
                    + "	employee_ssn bigint,"
                    + "	health_plan varchar(30), "
                    + "	foreign key (health_plan) references benefits(health_plan),"
                    + "	foreign key (employee_ssn) references employee(employee_ssn)"
                    + ");"

                    + "create table employee_dependent("
                    + "	dependent_ssn bigint, "
                    + "	employee_ssn bigint,"
                    + "	foreign key (employee_ssn) references employee(employee_ssn),"
                    + "	foreign key (dependent_ssn) references dependent(dependent_ssn)"
                    + ");"

                    + "create table dependent_benefits("
                    + "	dependent_ssn bigint,"
                    + "	health_plan varchar(30),"
                    + "	foreign key (dependent_ssn) references dependent(dependent_ssn),"
                    + "	foreign key (health_plan) references benefits(health_plan)"
                    + ");"

                    + "create table employee_bonus_multi("
                    + "	employee_ssn bigint,"
                    + "	bonus_amount bigint,"
                    + "	bonus_year int,"
                    + "	foreign key (employee_ssn) references employee(employee_ssn)"
                    + ");"

                    ;


            String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();

            stmt.executeUpdate(tables);
            stmt.executeUpdate("create index payment_details on employee(payment_date, payment_amount);");
            stmt.executeUpdate("create index bonus_details on employee_bonus_multi(bonus_amount, bonus_year);");
            stmt.executeUpdate("create role employee; create role manager; create role admin;");
            stmt.executeUpdate("grant select, update on employee, dependent, employee_dependent to employee, manager;"
                    +" grant select, update, delete, insert on benefits, dependent, dependent_benefits, employee, "
                    + "employee_benefits, employee_bonus_multi, employee_dependent, insurance_plan, state to admin with grant option;");
            stmt.executeUpdate("grant insert on dependent to employee, manager;");
            System.out.println(" DB CREATED.");

            adminUser = user;
            adminPassword = password;

            stmt.close();
            conn.close();
        }
        catch (Exception ex) {
            System.out.println("Error -- " + ex.getMessage() + "\n Please enter another command.");
            database = "";
        }
    }

    public static void signin(String user, String password) {
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();
            System.out.println("\n\nWelcome, " + user);
            if (user.contains("user")) {
                String stringid = user.replace("user", "");
                currentId = Integer.parseInt(stringid);
            }
            else if (user.contains("manager")) {
                String stringid = user.replace("manager", "");
                currentId = Integer.parseInt(stringid);
                manager = true;
            }
            else {
                currentId = 0;
                admin = true;
            }
            signedIn = true;
            stmt.close();
            conn.close();
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("Error -- Signin not completed. Please enter 'signin' again, or try another command.");
            currentUser = "";
            currentPassword = "";
            currentId = -1;
        }
    }

    public static void signup(String name, String password, int ssn, String benefit) {
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
        try {
            Connection conn = DriverManager.getConnection(url, adminUser, adminPassword);
            Statement stmt = conn.createStatement();
            String findUser = "select name from employee where name = '" + name + "';";
            ResultSet ret = stmt.executeQuery(findUser);
            if (ret.next()) {
                String addId = " update employee set employee_id = '" + userCount + "';";
                stmt.executeUpdate(addId);
            }
            String createUser = "create login user" + userCount + " with password = '" + password + "';";
            stmt.executeUpdate(createUser);
            String grant  = " grant employee to user" + userCount;
            stmt.executeUpdate(grant);
            stmt.close();
            conn.close();
            System.out.println(" User created. Username is 'user" + userCount + "' ");
            userCount++;

        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("Error -- Signup not completed. Please enter 'signup' again, or try another command.");
            currentUser = "";
            currentPassword = "";
            currentId = -1;
        }
    }

    public static void updateAccount(String update) {
        Scanner scan = new Scanner(System.in);
        if (update.equals("insurance")) {
            System.out.println(" Enter the new insurance name. ");
            String insurance = scan.nextLine();
            String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
            try {
                Connection conn = DriverManager.getConnection(url, currentUser, currentPassword);
                Statement stmt = conn.createStatement();
                String insuranceUpdate = "update employee set insurance_plan_name = '" + insurance +"' where employee_id = " + currentId;
                stmt.executeUpdate(insuranceUpdate);
                stmt.close();
                conn.close();
            }
            catch (Exception ex) {
                System.out.println(ex.getMessage());
                System.out.println("Error -- Update not completed. Please enter 'update' again, or try another command.");
            }

        }
        else if (update.equals("dependent")) {
            System.out.println(" Enter the new dependent information (name, ssn, relation to employee)");
            String name = scan.nextLine();
            int ssn = scan.nextInt();
            String relation = scan.nextLine();
            String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
            try {
                Connection conn = DriverManager.getConnection(url, currentUser, currentPassword);
                Statement stmt = conn.createStatement();
                String dependentUpdate = "insert into depependent(dependent_ssn, relation_to_employee, name) values('" + ssn + "','" + relation + "', '" + name + "');";
                stmt.executeUpdate(dependentUpdate);
                stmt.close();
                conn.close();
            }
            catch (Exception ex) {
                System.out.println(ex.getMessage());
                System.out.println("Error -- Update not completed. Please enter 'update' again, or try another command.");
            }

        }
        else if (update.equals("address")) {
            System.out.println(" Enter the new address: ");
            String address = scan.nextLine();
            String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
            try {
                Connection conn = DriverManager.getConnection(url, currentUser, currentPassword);
                Statement stmt = conn.createStatement();
                String addressUpdate = "update employee set address = '" + address + "' where employee_id = '" + currentId + "';";
                stmt.executeUpdate(addressUpdate);
                stmt.close();
                conn.close();
            }
            catch (Exception ex) {
                System.out.println(ex.getMessage());
                System.out.println("Error -- Update not completed. Please enter 'update' again, or try another command.");
            }

        }

        else {
            System.out.println("Error -- incorrect argument for 'update' command. \n Please enter another command, or type '--help' for command list.");
        }
        scan.close();
    }

    public static void viewPaycheck() {
        System.out.println();
    }

    public static void viewReport(String type) {
        Scanner scan = new Scanner(System.in);
        int id = 0;
        switch(type) {
            case "paycheck": System.out.println(" Enter employee id to view their paycheck or enter '-1' to view all paychecks: ");
                id = scan.nextInt();
                if (id == -1) {
                    for (int i = 0; i<paychecks.size(); i++) {
                        System.out.println(paychecks.get(i) + "\n");
                    }
                }
                else {
                    System.out.println(paychecks.get(id) + "\n\n");
                }
                break;
            case "expense": System.out.println(expenseReport);
            case "w2": System.out.println(" Enter employee id to view their W2 or enter '-1' to view all W2's: ");
                id = scan.nextInt();
                if (id == -1) {
                    for (int i = 0; i<w2.size(); i++) {
                        System.out.println(w2.get(i) + "\n");
                    }
                }
                else {
                    System.out.println(w2.get(id) + "\n\n");
                }
                break;
            default: System.out.println(" Error -- Report return not completed. Please enter 'view report' again, or try another command.");
                break;
        }
        scan.close();
    }

    public static void updateBonus(int employee) {
        Scanner scan = new Scanner(System.in);
        System.out.println("\n Enter the new empoloyee bonus: ");

        int bonus = scan.nextInt();
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
        try {
            Connection conn = DriverManager.getConnection(url, currentUser, currentPassword);
            Statement stmt = conn.createStatement();
            String bonusUpdate = " update employee set bonus = '" + bonus +"' where employee_id = " + employee;
            stmt.executeUpdate(bonusUpdate);
            stmt.close();
            conn.close();
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println(" Error -- Update not completed. Please enter 'update bonus' again, or try another command.");
        }
        scan.close();
    }

    public static void updatePerformance(int id) {
        Scanner scan = new Scanner(System.in);
        System.out.println(" Enter the new performance report: ");
        String performance = scan.nextLine();
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
        try {
            Connection conn = DriverManager.getConnection(url, currentUser, currentPassword);
            Statement stmt = conn.createStatement();
            String bonusUpdate = " update employee set performance = '" + performance +"' where employee_id = " + id;
            stmt.executeUpdate(bonusUpdate);
            stmt.close();
            conn.close();
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println(" Error -- Update not completed. Please enter 'update bonus' again, or try another command.");
        }
        scan.close();
    }

    public static void adminUpdate(int employee) {
        boolean error = false;
        Scanner scan = new Scanner(System.in);
        System.out.println("\n Enter the table you want to update \n [ benefits, dependent, dependent_benefits, employee, employee_benefits, "
                + "\n employee_bonus_multi, employee_dependent, insurance_plan, state]: ");

        String table = scan.nextLine();
        switch(table) {
            case "benefits": System.out.println("\n Enter the attribute, followed by the new value, followed by the primary key attribute and name for the table: ");
                break;
            case "dependent":System.out.println("\n Enter the attribute, followed by the new value, followed by the primary key attribute and name for the table: ");
                break;
            case "dependent_benefits":System.out.println("\n Enter the attribute, followed by the new value, followed by the primary key attribute and name for the table: ");
                break;
            case "employee":System.out.println("\n Enter the attribute, followed by the new value, followed by the primary key attribute and name for the table: ");
                break;
            case "employee_benefits":System.out.println("\n Enter the attribute, followed by the new value, followed by the primary key attribute and name for the table: ");
                break;
            case "employee_bonus_multi":System.out.println("\n Enter the attribute, followed by the new value, followed by the primary key attribute and name for the table: ");
                break;
            case "employee_dependent":System.out.println("\n Enter the attribute, followed by the new value, followed by the primary key attribute and name for the table: ");
                break;
            case "insurance_plan":System.out.println("\n Enter the attribute, followed by the new value, followed by the primary key attribute and name for the table: ");
                break;
            case "state":System.out.println("\n Enter the attribute, followed by the new value, followed by the primary key attribute and name for the table: ");
                break;
            default: System.out.println(" Error -- You may have entered an incorrect table.\\n Please enter 'admin update'again, or try another command.");
                error = true;
                break;
        }
        if (!error) {
            String attribute = scan.nextLine();
            String value = scan.nextLine();
            String primaryAttribute = scan.nextLine();
            String primaryName = scan.nextLine();
            String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
            try {
                Connection conn = DriverManager.getConnection(url, currentUser, currentPassword);
                Statement stmt = conn.createStatement();
                String update = "update " + table + " set " + attribute + " = '" + value +"' where " + primaryAttribute + " = " + primaryName;
                stmt.executeUpdate(update);
                stmt.close();
                conn.close();
            }
            catch (Exception ex) {
                System.out.println(ex.getMessage());
                System.out.println("Error -- Update not completed. Please enter 'admin update' again, or try another command.");
            }
        }
        scan.close();
    }

    public static void addEmployee(int ssn, String name, String salaryType, String jobTitle, String bonus, String federalTax, String yearlyIncome, String paymentDate, String paymentAmount, String state, String insurance) {
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
        try {
            Connection conn = DriverManager.getConnection(url, currentUser, currentPassword);
            Statement stmt = conn.createStatement();
            String add = "insert into employee (employee_ssn, name, salary_type, job_title, bonus, federal_tax, yearly_income, "
                    + "payment_date, payment_amount, state_name, insurance_plan) values('" + ssn + "', '" + name + "','"
                    + salaryType + "','" + jobTitle + "','" + bonus + "','" + federalTax + "','" + yearlyIncome + "','" + paymentDate + "','" + paymentAmount + "','" + state + "','" + insurance + "');";
            stmt.executeUpdate(add);
            stmt.close();
            conn.close();
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("Error -- Update not completed. Please enter 'admin update' again, or try another command.");
        }
    }

    public static void runReport(boolean yearlyExpenses) {
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
        try {
            Connection conn = DriverManager.getConnection(url, currentUser, currentPassword);
            Statement stmt = conn.createStatement();
            String employees = " select * from employee";
            ResultSet eret =  stmt.executeQuery(employees);
            double wages = 0, allbonus = 0, allfour01k = 0, ssncontrib = 0, insurancecontrib = 0;


            while(eret.next()) {    //generate paychecks, w2, and expense report
                String insurance = " select * from insurance where name = '" + eret.getString(12) + "';";
                ResultSet iret = stmt.executeQuery(insurance);
                String states = "select * from state where name = '" + eret.getString(11) + "';";
                ResultSet sret = stmt.executeQuery(states);
                String employee_benefits = "select * from employee_benefits where employee_ssn = '" + eret.getString(1) + "';";
                ResultSet ebret = stmt.executeQuery(employee_benefits);
                String benefits = "select * from benefits where health_plan = '" + ebret.getString(2) + "';";
                ResultSet bret = stmt.executeQuery(benefits);
                int id = eret.getInt(2);
                String paycheck;
                String curw2;

                if (id < paychecks.size()) {
                    for (int i = paychecks.size(); i < id; i++) {
                        paychecks.add("");
                    }
                }
                double income = eret.getInt(8)/(52.1429/2);
                double taxdeductions = income*(eret.getInt(7)/100) + income*sret.getInt(2)/100;
                double four01k = income*(bret.getInt(2)/100);
                double socialsecurity = 0;
                if (eret.getString(4).equals("hourly")) {socialsecurity = .15*income;}
                else {socialsecurity = .075*income;}
                double medicare = income*.025;
                double incomeAfterTax = income - taxdeductions-four01k-socialsecurity-medicare;
                paycheck = "Name: " + eret.getInt(3) + "\tSSN: " + eret.getInt(1) + "\t income before tax: $" + income +
                        "\ttax deductions: -$" + taxdeductions + "\t401k deduction: -$" + four01k + "\tinsurance premium: -$" + iret.getString(2)
                        +"\tsocial security: -$"+ socialsecurity + "\tmedicare: -$" + medicare + "\n income after tax: $" + incomeAfterTax + "\n\n";
                paychecks.add(id, paycheck);

                if(yearlyExpenses) {


                    double bonus = (eret.getInt(id)/(double)100)*income;
                    wages += income;
                    allbonus+= bonus;
                    if (bret.getInt(2)/(double)100 > .07 ) {allfour01k+=income*.07;}
                    else {allfour01k += four01k;}
                    if (!(eret.getString(4).equals("hourly"))) {ssncontrib += socialsecurity;}
                    insurancecontrib += iret.getInt(2);


                    if (id<w2.size()) {
                        for (int i = w2.size(); i < id; i++) {
                            w2.add("");
                        }
                    }
                    income = eret.getInt(8);
                    taxdeductions = income*(eret.getInt(7)/(double)100) + income*sret.getInt(2)/100;
                    four01k = income*(bret.getInt(2)/(double)100);
                    socialsecurity = 0;
                    if (eret.getString(4).equals("hourly")) {socialsecurity = .15*income;}
                    else {socialsecurity = .075*income;}
                    medicare = income*.025;
                    incomeAfterTax = income - taxdeductions-four01k-socialsecurity-medicare+bonus;
                    curw2 = "Name: " + eret.getInt(3) + "\tSSN: " + eret.getInt(1) + "\t yearly income before tax: $" + income +
                            "\ttax deductions: -$" + taxdeductions + "\t401k deduction: -$" + four01k + "\tinsurance premium: -$"
                            + iret.getString(2) +"\tsocial security: -$"+ socialsecurity + "\tmedicare: -$" + medicare + "\tbonus: +$"
                            + bonus+ "\n income after tax and bonus: $" + incomeAfterTax + "\n\n";
                    w2.add(id, curw2);
                }
            }

            double total = wages+allbonus+allfour01k + ssncontrib+insurancecontrib;

            expenseReport = "\n\n Wages: +" + wages + "\tBonuses: +$" + allbonus + "\t401k: +$" + allfour01k + "\tSSN Contribution: +$"
                    + ssncontrib + "\tInsurance Contribution: +$" + insurancecontrib + "\n Total Expenses: $" + total;

            stmt.close();
            conn.close();
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("Error -- Remove not completed. Please enter 'remove employee' again, or try another command.");
        }

        System.out.print(" Report run. \n Type 'view report' to view report.");
    }

    public static void removeEmployee(int id) {
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
        try {
            Connection conn = DriverManager.getConnection(url, currentUser, currentPassword);
            Statement stmt = conn.createStatement();
            String remove = " delete from employee where employee_id = '" + id +"';";
            stmt.executeUpdate(remove);
            stmt.close();
            conn.close();
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("Error -- Remove not completed. Please enter 'remove employee' again, or try another command.");
        }
    }

    public static void test() {
        String url = "jdbc:postgresql://" + "localhost" + ":" + "5432" + "/" + "postgres";
        try {
            Connection conn = DriverManager.getConnection(url, "postgres", "0000");
            Statement stmt = conn.createStatement();
            //	stmt.executeUpdate("grant select, update on employee to employee; grant select, update on employee to manager;"
            //	+"grant select, update, delete, insert on benefits, dependent, dependent_benefits, employee, employee_benefits, employee_bonus_multi, employee_dependent, insurance_plan, state to admin;");
            //create();

            stmt.close();
            conn.close();
            System.out.print("db created");
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
