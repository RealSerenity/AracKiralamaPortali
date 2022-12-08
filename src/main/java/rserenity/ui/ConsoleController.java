package rserenity.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.spel.spi.Function;
import org.springframework.stereotype.Controller;
import rserenity.Exception.ResourceNotFoundException;
import rserenity.business.dto.CompanyDto;
import rserenity.business.dto.ReservationDto;
import rserenity.business.dto.UserDto;
import rserenity.business.dto.VehicleDto;
import rserenity.business.services.CompanyServices;
import rserenity.business.services.ReservationServices;
import rserenity.business.services.UserServices;
import rserenity.business.services.VehicleServices;
import rserenity.enums.City;
import rserenity.enums.VehicleType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@Controller
public class ConsoleController {

    UserServices userServices;

    VehicleServices vehicleServices;

    CompanyServices companyServices;

    ReservationServices reservationServices;

    @Autowired
    public ConsoleController(UserServices userServices, VehicleServices vehicleServices, CompanyServices companyServices, ReservationServices reservationServices) {
        this.userServices = userServices;
        this.vehicleServices = vehicleServices;
        this.companyServices = companyServices;
        this.reservationServices = reservationServices;
        this.scanner = new Scanner(System.in);
    }

    private final Scanner scanner;

    private UserDto user;

    private char operation;

    public Function userNullMenu(){
        delay();
        System.out.print("""
                Select an option :\s
                0 : Log in\s
                1 : Sign in\s
                2 : Back
                """);

        operation = scanner.next().charAt(0);

        switch (operation) {
            case '0' -> {return userLogin();}
            case '1' -> {return userSignIn();}
            case '2' -> {return goToMain();}
            default -> System.out.println("Invalid operation !");
        }
        return userNullMenu();
    }

    private Function userLogin(){
        System.out.println("Log in -> ");
        System.out.print("username : ");
        String userName = scanner.next();
        user = userServices.getUserDtoByName(userName);
        if(user == null){
            System.out.println("username not exists");
            return userNullMenu();
        }
        System.out.print("password : ");
        String password = scanner.next();
        user.setPassword(password);
        if(userServices.userLogin(user)){
            System.out.println("Logged in");
            return userOptions();
        }else {
            System.out.println("0 : Try again \n"+
                    "1 : Back");
            operation = scanner.next().charAt(0);
            switch (operation) {
                case '0' -> {return userLogin();}
                case '1' -> {return userNullMenu();}
                default -> System.out.println("Invalid operation !");
            }
        }
            return userLogin();
    }
    private Function userSignIn(){
        System.out.print("username : ");
        String userName = scanner.next();
        if(!(userServices.getUserDtoByName(userName) == null)){
            System.out.println("Username must be unique");
            delay();
            userSignIn();
        }
        System.out.print("password : ");
        String password = scanner.next();
        userServices.createUser(UserDto.builder()
                .username(userName)
                .password(password)
                .build());
        System.out.println("Signed in");
        return userLogin();
    }
    private Function goToMain(){
        delay();
        return null;
        // blank
    }


    private Function userOptions(){
        delay();
        System.out.print("""
                Select an option :\s
                0 : Make a reservation\s
                1 : Cancel Reservation\s
                2 : User profile\s
                3 : Back
                """);

        operation = scanner.next().charAt(0);

        switch (operation) {
            case '0' -> {return createReservation();}
            case '1' -> {return cancelReservation();}
            case '2' -> {return userProfile();}
            case '3' -> {
                user = null;
                return userNullMenu();
            }
            default -> System.out.println("Invalid operation !");
        }
        return userOptions();
    }

    private Function createReservation(){
        // catch the reservation available date in the reservationserviceimpl class
        List<CompanyDto> companies= companyServices.getAll();
        if(companies.size() ==0){
            System.out.println("0 companies");
            return userOptions();
        }
        int i =1;
        for (CompanyDto company:  companies) {
            System.out.println("id : " + i+ "  " + company.getName() + " city : " + company.getCity());
            System.out.println("-");
            i++;
        }
        System.out.print("Enter id information = ");
        int companyIndex = getIntFromScanner(1,companies.size());

        List<VehicleDto> vehicles;
        try {
            // checking whether company exists
            companyServices.getCompanyById(companies.get(companyIndex-1).getId());

            vehicles = vehicleServices.getVehiclesByCompanyId(companies.get(companyIndex).getId());
            i =1;
            for (VehicleDto vehicle:  vehicles) {
                System.out.println("id : " + i + " daily price : " + vehicle.getDailyPrice() + " vehicle type : "+  vehicle.getType());
                i++;
            }
        }  catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
            return userOptions();
        }
        System.out.print("Enter id information = ");
        int vehicleIndex = getIntFromScanner(1,vehicles.size());

        System.out.print("Date ? ");
        LocalDate date = getLocalDateFromScanner();
        try {
            Map<String, Object> response = reservationServices.createReservation(ReservationDto.builder()
                    .userId(user.getId())
                    .vehicleId(vehicles.get(vehicleIndex-1).getId())
                    .reservationStartDate(date)
                    .build());
            if((boolean)response.get("ok")){
                System.out.println("Reservation created successfully");
            }else {
                if((boolean)response.get("gecersizDate")){
                    System.out.println("Cannot be booked for the past");
                }else {
                    System.out.println("Can't make reservation because there is a reservation on the given date");
                }
            }
        }  catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return userOptions();
    }
    private Function cancelReservation(){
        try {
            List<ReservationDto> reservations = reservationServices.getUserReservations(user.getId());
            if(reservations.size() ==0){
                System.out.println("0 reservations");
                return userOptions();
            }else{
                int i =1;
                for (ReservationDto reservation: reservations) {
                    System.out.println("Reservation id = " + i + "  reservation date = " + reservation.getReservationStartDate()+ "\n"
                            + vehicleServices.getVehicleById(reservation.getVehicleId()).getBody());
                    i++;
                    System.out.println("-");
                }
                System.out.print("Enter id information = ");
                int reservationId = getIntFromScanner(1,reservations.size());
                //checking whether reservation exist
                reservationServices.getReservationById(reservations.get(reservationId).getId());
                Map<String,Boolean> response = reservationServices.deleteReservation(reservations.get(reservationId).getId()).getBody();
                Boolean canceled = response != null ? response.get("canceled") : null;
                System.out.println("Canceled = "  + canceled);
                if(Boolean.FALSE.equals(canceled)){
                    // something wrong
                    System.out.println("Too late to cancel reservation");
                }
            }
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return userOptions();
    }
    private Function userProfile(){
        // user update
        delay();
        System.out.print("""
                Select an option :\s
                0 : Update user infos\s
                1 : See all reservations\s
                2 : Back\s
                """);
        
        operation = scanner.next().charAt(0);

        switch (operation) {
            case '0' -> {return userUpdate();}
            case '1' -> {return userReservations();}
            case '2' -> {return userOptions();}
            default -> System.out.println("Invalid operation !");
        }

        return userProfile();
    }

    private Function userUpdate() {
        delay();
        System.out.print("""
                Select an option :\s
                0 : Update username\s
                1 : Update password\s
                2 : Back\s
                """);
         operation = scanner.next().charAt(0);

        switch (operation) {
            case '0' -> {
                //update username
                System.out.print("username = ");
                String username = scanner.next();
                try {
                    userServices.updateUserById(user.getId(), UserDto.builder()
                            .username(username)
                            .build());
                    if(user.getUsername().matches(username)) {
                        System.out.println("Password updated");
                    }
                } catch (ResourceNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
            case '1' -> {
                System.out.print("password = ");
                String password = scanner.next();
                try {
                    user = userServices.updateUserById(user.getId(), UserDto.builder()
                            .password(password)
                            .build()).getBody();
                    if(user==null){
                        return userUpdate();
                    }
                    if(user.getPassword().matches(password)){
                        System.out.println("Password updated");
                    }else System.out.println("Something is wrong");
                }  catch (ResourceNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
            case '2' -> {return userProfile();}
            default -> System.out.println("Invalid operation !");
        }
        return userUpdate();
    }

    private Function userReservations() {
        try {
            List<ReservationDto>  reservations = reservationServices.getUserReservations(user.getId());
            VehicleDto vehicle;
            CompanyDto company;
            if(reservations.size() == 0 ){
                System.out.println("0 reservations");
            }
            for (ReservationDto reservation: reservations) {
                 vehicle = vehicleServices.getVehicleById(reservation.getVehicleId()).getBody();
                 if(vehicle != null){
                     company = companyServices.getCompanyById(vehicle.getCompanyId()).getBody();
                     if (company != null) {
                         System.out.println("Reservation date : " + reservation.getReservationStartDate() +"\n" +
                                 "Vehicle's company '"+ company.getName() + "' vehicle type " + vehicle.getType());
                     }
                 }
            }
        }  catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return userProfile();
    }


    public Function adminMenu(){
        System.out.print("""
                Select an option :\s
                0 : Create Company\s
                1 : Create Vehicle\s
                2 : Back
                """);

        operation = scanner.next().charAt(0);

        switch (operation) {
            case '0' -> {return createCompany();}
            case '1' -> {return createVehicle();}
            case '2' -> {return goToMain();}
            default -> {
                System.out.println("Invalid operation !");
                return adminMenu();
            }
        }
    }
    private Function createCompany() {
        delay();
        System.out.print("Company name : ");
        String companyName = scanner.next();
        System.out.print("City : ");
        int cityIndex = getIntFromScanner(1,81);
        companyServices.createCompany(CompanyDto.builder()
                        .name(companyName)
                        .city(City.getCityByPlaka(cityIndex))
                .build());
        return adminMenu();
    }

    private Function createVehicle() {
        delay();
        System.out.print("Daily price : ");
        double dailyPrice = getDoubleFromScanner();

        System.out.print("Available until ? ");
        LocalDate date = getLocalDateFromScanner();
        int i =0;
        for (VehicleType type: VehicleType.values()) {
            System.out.println(i + " : " + type);
            i++;
        }
        System.out.print("Enter index : ");
        int vehicleTypeIndex = getIntFromScanner(0,i-1);

        for (CompanyDto company: companyServices.getAll()) {
            System.out.println(company.toString());
        }
        System.out.print("Enter company id : ");
        Long companyId = getLongFromScanner();
        try {
            companyServices.getCompanyById(companyId);
            Map<String ,Object> response = vehicleServices.createVehicle(VehicleDto.builder()
                    .dailyPrice(dailyPrice)
                    .availableUntil(date)
                    .type(VehicleType.values()[vehicleTypeIndex])
                    .companyId(companyId)
                    .build()).getBody();
            assert response != null;
            if((boolean)response.get("ok")){
                System.out.println(response.get("object"));
            }else {
                System.out.println("Date must be in the future");
                return adminMenu();
            }
        }  catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return adminMenu();
    }

    private void delay(){
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private long getLongFromScanner(){
        long value;

        try {
            value = scanner.nextLong();
        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("\tInvalid input type (must be a long)");
            System.out.print("Try again : ");
            return getLongFromScanner();
        }
        return value;
    }

    private int getIntFromScanner(int lowerBound, int upperBound){
        int value;

        try {
            value = scanner.nextInt();
            if(value>upperBound || value<lowerBound){
                System.out.println("Input must be between " + lowerBound + " - " + upperBound + "(bounds are included)");
                System.out.print("Try again : ");
                return getIntFromScanner(lowerBound,upperBound);
            }
        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("\tInvalid input type (must be an integer)");
            System.out.print("Try again : ");
            return getIntFromScanner(lowerBound,upperBound);
        }
        return value;
    }

    private double getDoubleFromScanner(){
        double value;

        try {
            value = scanner.nextDouble();
        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("\tInvalid input type (must be an double)");
            System.out.print("Try again : ");
            return getDoubleFromScanner();
        }
        return value;
    }

    private LocalDate getLocalDateFromScanner(){
        LocalDate value;

        try {
            String dateString = scanner.next();
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("d/M/yyyy");
            value = LocalDate.parse(dateString, dateFormat);

        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("\tInvalid input type (must be like 'd/M/yyyy')");
            System.out.print("Try again : ");
            return getLocalDateFromScanner();
        }
        return value;
    }

}
