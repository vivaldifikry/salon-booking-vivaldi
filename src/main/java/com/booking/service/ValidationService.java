package com.booking.service;

import com.booking.models.*;

import java.util.List;
import java.util.Objects;

public class ValidationService {
    // Buatlah function sesuai dengan kebutuhan
    public static void validateInput(){

    }

    public static boolean validateCustomerId(List<Person> customerList, String id){
        for (Person person : customerList) {
            if (person instanceof Customer) {
                Customer customer = (Customer) person;
                if(Objects.equals(customer.getId(), id)){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean validateEmployeeId(List<Person> employeeList, String id){
        for (Person person : employeeList) {
            if (person instanceof Employee) {
                Employee employee = (Employee) person;
                if(Objects.equals(employee.getId(), id)){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean validateServiceId(List<Service> serviceList, String id){
        for (Service service : serviceList) {
            if(Objects.equals(service.getServiceId(), id)){
                return true;
            }
        }
        return false;
    }

    public static boolean validateReservationId(List<Reservation> reservationList, String id){
        for (Reservation reservation : reservationList) {
            if(Objects.equals(reservation.getReservationId(), id)){
                return true;
            }
        }
        return false;
    }

    public static boolean validateInputService(String[] array, String input) {
        for (String element : array) {
            if (element != null && element.equals(input)) {
                return true;
            }
        }
        return false;
    }
}
