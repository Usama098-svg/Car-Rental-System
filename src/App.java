
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;

class Car{
    private String brand;
    private String model;
    private double rentPerDay;
    private boolean isAviable;

    public Car( String brand, String model, double rentPerDay){
        this.brand = brand;
        this.model = model;
        this.rentPerDay = rentPerDay;
        this.isAviable = true;
    }
    public String get_brand(){
        return brand;
    }
    public String get_model(){
        return model;
    }
    public double get_carRentPerDay(){
        return rentPerDay;
    }
    public boolean get_isAviable(){
        return isAviable;
    }
    public double calculateRent(int days){
        return rentPerDay * days;
    }
     

}

class Customer{
    private String customerID;
    private String customerName;
    private String phone;

    public Customer(String customerID, String customerName, String phone){
        this.customerID = customerID;
        this.customerName = customerName;
        this.phone = phone;
    }
    public String get_customerID(){
        return customerID;
    }
    public String get_customerName(){
        return customerName;
    }
    public String get_customerPhone(){
        return phone;
    }
}

class Rental{
    private Car car;
    private Customer customer;
    private int days;

    public Rental(Car car, Customer customer, int days){
        this.car = car;
        this.customer = customer;
        this.days = days;
    }
    public Car get_car(){
        return car;
    }
    public Customer get_customer(){
        return customer;
    }
    public int get_days(){
        return days;
    }
}


class CarRentalSystem{
    Connection connection;

    public CarRentalSystem(Connection connection){
        this.connection = connection;
    }
    public String generateCarID() {
        Random rand = new Random();
        int number = rand.nextInt(9000) + 1000; // ensures a 4-digit number between 1000 and 9999
        return String.valueOf(number);
    }
    public void addCar(Car car){
        try {
            String carID =   generateCarID();
            String sql = "INSERT INTO cars (carID, brand, model, rentPerDay, isAvailable) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, carID);
            preparedStatement.setString(2, car.get_brand());
            preparedStatement.setString(3, car.get_model());
            preparedStatement.setDouble(4, car.get_carRentPerDay());
            preparedStatement.setBoolean(5, car.get_isAviable());
            int rowEffected = preparedStatement.executeUpdate();
            if (rowEffected > 0) {
                System.out.println("Car added successfully.");
            } else {
                System.out.println("Car added failed.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
       
    }
    public void addCustomer(Customer customer){
        try {
            String sql = "INSERT INTO customers (customerID, customerName, phoneNumber) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, customer.get_customerID());
            preparedStatement.setString(2, customer.get_customerName());
            preparedStatement.setString(3, customer.get_customerPhone());
            int rowEffected = preparedStatement.executeUpdate();
            if (rowEffected > 0) {
                System.out.println("Customer added successfully.");
            } else {
                System.out.println("Customer added failed.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
    }
    public void return_a_Car(String carID){
        try {
            String updateCar = "UPDATE cars SET isAvailable = 1 WHERE carID = ?";
            PreparedStatement updateStmt = connection.prepareStatement(updateCar);
            updateStmt.setString(1, carID);
            int rowEffected = updateStmt.executeUpdate();
            String deleteRental = "DELETE FROM rentals WHERE carID = ?";
            PreparedStatement deleteStmt = connection.prepareStatement(deleteRental);
            deleteStmt.setString(1, carID);
            int rowEffected1 = deleteStmt.executeUpdate();
            if (rowEffected > 0 && rowEffected1 > 0) {
                System.out.println("Car return successfully.");
            } else {
                System.out.println("Wrong carID to return.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public String generateCustomerID() {
        Random rand = new Random();
        int number = rand.nextInt(9000) + 1000; // ensures a 4-digit number between 1000 and 9999
        return String.valueOf(number);
    }

    public void menu(){
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("");
            System.out.println("---------------------------------");
            System.out.println("------- Car Rental System---------");
            System.out.println("----------------------------------");
            System.out.println("");
            System.out.println("1] Rent a car");
            System.out.println("2] Show All Rentals");
            System.out.println("3] Add new Car");
            System.out.println("4] Return a car");
            System.out.println("0] Exit");
            System.out.println("");
            System.out.print("Enter a choice: ");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    {
                        System.out.println("");
                        System.out.println("Avaiable Cars Here");
                        System.out.println("");
                        try {
                            String query = "SELECT * FROM cars WHERE isAvailable = 1";
                            PreparedStatement preparedStatement = connection.prepareStatement(query);
                            ResultSet rs = preparedStatement.executeQuery();
                            while (rs.next()) {
                                String carID = rs.getString("carID");
                                String brand = rs.getString("brand");
                                String model = rs.getString("model");
                                double rentPerDay = rs.getDouble("rentPerDay");
                                
                                System.out.println("Car ID: "+carID);
                                System.out.println("Brand: "+brand);
                                System.out.println("Model: "+model);
                                System.out.println("Rent Per Day: "+rentPerDay);
                                System.out.println("Available: Yes");
                                System.out.println("---------------------------------------");
                            }
                        } catch (SQLException e) {
                            System.out.println(e.getMessage());
                        }       System.out.println("");
                        sc.nextLine();
                        System.out.print("Enter your name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter your phone: ");
                        String phone = sc.nextLine();
                        System.out.print("Enter car ID to rent: ");
                        String carIId = sc.nextLine();
                        System.out.print("Enter how many days to rent: ");
                        int day = sc.nextInt();
                        String customerID = generateCustomerID();
                        Customer newCustomer = new Customer("CUS-"+(customerID), name, phone);
                        addCustomer(newCustomer);
                        String carID;
                        String brand;
                        String model;
                        double rentPerDay;
                        try {
                            String query = "SELECT * FROM cars WHERE carID = ? AND isAvailable = 1";
                            PreparedStatement preparedStatement = connection.prepareStatement(query);
                            preparedStatement.setString(1, carIId);
                            ResultSet rs = preparedStatement.executeQuery();
                            
                            if (rs.next()) {
                                carID = rs.getString("carID");
                                brand = rs.getString("brand");
                                model = rs.getString("model");
                                rentPerDay = rs.getDouble("rentPerDay");
                                
                                // Create Car object
                                Car car = new Car(brand, model, rentPerDay);
                                
                                double totalRent = car.calculateRent(day);
                                System.out.println("");
                                System.out.println("+--------------------+");
                                System.out.println("| Rental Information |");
                                System.out.println("+--------------------+");
                                System.out.println("");
                                System.out.println("CustomerID: " + newCustomer.get_customerID());
                                System.out.println("Customer Name: " + newCustomer.get_customerName());
                                System.out.println("Customer Phone: " + newCustomer.get_customerPhone());
                                System.out.println("-------------------------------------");
                                System.out.println("Car You Selected");
                                System.out.println("-------------------------------------");
                                System.out.println("Car ID: " + carID);
                                System.out.println("Brand: " + brand);
                                System.out.println("Model: " + model);
                                System.out.println("Rent Per Day: " + rentPerDay);
                                System.out.println("Rental Days: " + day);
                                System.out.println("Total Rent: " + totalRent);
                                System.out.print("Confirm Rent? (Y/N): ");
                                sc.nextLine();
                                String confirm = sc.nextLine();
                                
                                if (confirm.equalsIgnoreCase("Y")) {
                                    try {
                                        String insert_query = "INSERT INTO rentals(carID, customerID, days, totalRent) VALUES (?,?,?,?)";
                                        PreparedStatement preparedStatement1 = connection.prepareStatement(insert_query);
                                        preparedStatement1.setString(1, carID);
                                        preparedStatement1.setString(2, newCustomer.get_customerID());
                                        preparedStatement1.setInt(3, day);
                                        preparedStatement1.setDouble(4, totalRent);
                                        int rowEffected = preparedStatement1.executeUpdate();
                                        
                                        if (rowEffected > 0) {
                                            System.out.println("Rented Successfully...");
                                            
                                            // Mark car as unavailable
                                            String updateCar = "UPDATE cars SET isAvailable = 0 WHERE carID = ?";
                                            PreparedStatement updateStmt = connection.prepareStatement(updateCar);
                                            updateStmt.setString(1, carID);
                                            updateStmt.executeUpdate();
                                        } else {
                                            System.out.println("Rent cancelled...");
                                        }
                                    } catch (SQLException e) {
                                        System.out.println("Error inserting rental: " + e.getMessage());
                                    }
                                } else {
                                    System.out.println("Rent cancelled...");
                                }
                            } else {
                                System.out.println("Invalid car id.");
                            }
                        } catch (SQLException e) {
                            System.out.println("Error while fetching car: " + e.getMessage());
                        }       break;
                    }
                case 2:
                    {
                        System.out.println("");
                        try {
                            String query = "SELECT * FROM rentals";
                            PreparedStatement preparedStatement = connection.prepareStatement(query);
                            ResultSet rs = preparedStatement.executeQuery();
                            boolean hasRentals = false;
                            while (rs.next()) {
                                hasRentals = true;
                                int rentalID = rs.getInt("rentalID");
                                String carID = rs.getString("carID");
                                String customerID = rs.getString("customerID");
                                int days = rs.getInt("days");
                                double totalRent = rs.getDouble("totalRent");

                                System.out.println("Rental ID: "+rentalID);
                                System.out.println("Car ID: "+carID);
                                System.out.println("Customer ID: "+customerID);
                                System.out.println("Days To Rent: "+days);
                                System.out.println("Total Rent: "+totalRent);
                                System.out.println("------------------------------------------");
                            }
                            if (!hasRentals) {
                                System.out.println("No rentals yet.");
                            }
                        } catch (SQLException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    }        
                case 3:
                    {
                        System.out.println("");
                        sc.nextLine();
                        System.out.print("Enter Brand: ");
                        String brand = sc.nextLine();
                        System.out.print("Enter Model: ");
                        String model = sc.nextLine();
                        System.out.print("Rent Per Day: ");
                        double rent = sc.nextDouble();
                        Car car = new Car(brand, model, rent);
                        addCar(car);
                        break;
                    }    
                case 4:
                    {
                        System.out.println("");
                        System.out.print("Enter carID to return: ");
                        sc.nextLine();
                        String carID = sc.nextLine();
                        return_a_Car(carID);
                        break;
                    }
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice...");
                    break;
            }
           
        }

      
        
    }
    

}




public class App {
    private static final String url = "jdbc:mysql://localhost:3306/car_rental_system";
    private static final String user = "root"; 
    private static final String password = "Usama@123456";
    public static void main(String[] args) throws Exception {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded succesfully.");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connection established successfully.");

            CarRentalSystem carRentalSystem = new CarRentalSystem(connection);
            // Car car1 = new Car( "Toyota", "Carry", 5000);
            // Car car2 = new Car( "Honda", "Road", 2500);
            // Car car3 = new Car( "KIA", "Sportage", 7000);
            // carRentalSystem.addCar(car1);
            // carRentalSystem.addCar(car2);
            // carRentalSystem.addCar(car3);
            System.out.println("");
            carRentalSystem.menu();
            System.out.println("");
            System.out.println("Thanks for using Car Rental System...");
            System.out.println("This system designed by UA Tech...");
            System.out.println("");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
       

    }
}
