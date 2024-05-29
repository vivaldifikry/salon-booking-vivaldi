package com.booking.service;

import java.util.List;

import com.booking.models.*;

public class PrintService {
    public static void printMenu(String title, String[] menuArr){
        int num = 1;
        System.out.println(title);
        for (int i = 0; i < menuArr.length; i++) {
            if (i == (menuArr.length - 1)) {   
                num = 0;
            }
            System.out.println(num + ". " + menuArr[i]);   
            num++;
        }
    }

    public static String printServices(List<Service> serviceList){
        String result = "";
        // Bisa disesuaikan kembali
        for (Service service : serviceList) {
            result += service.getServiceName() + ", ";
        }
        return result;
    }

    // Function yang dibuat hanya sebgai contoh bisa disesuaikan kembali
    public static void showRecentReservation(List<Reservation> reservationList){
        int num = 1;
        System.out.printf("| %-4s | %-4s | %-11s | %-15s | %-15s | %-15s | %-10s |\n",
                "No.", "ID", "Nama Customer", "Service", "Biaya Service", "Pegawai", "Workstage");
        System.out.println("+========================================================================================+");
        for (Reservation reservation : reservationList) {
            if (reservation.getWorkstage().equalsIgnoreCase("Waiting") || reservation.getWorkstage().equalsIgnoreCase("In Process")) {
                System.out.printf("| %-4s | %-4s | %-11s | %-15s | %-15s | %-15s | %-10s |\n",
                num, reservation.getReservationId(), reservation.getCustomer().getName(), printServices(reservation.getServices()), reservation.getReservationPrice(), reservation.getEmployee().getName(), reservation.getWorkstage());
                num++;
            }
        }
    }

    public static void showAllCustomer(List<Person> customerList){
        int num = 1;
        System.out.printf("| %-4s | %-4s | %-11s | %-15s | %-15s | %-15s |\n",
                "No.", "ID", "Nama", "Alamat", "Membership", "Uang");
        System.out.println("+========================================================================================+");

        for (Person person : customerList) {
            if (person instanceof Customer) {
                Customer customer = (Customer) person;
                System.out.printf("| %-4s | %-4s | %-11s | %-15s | %-15s | %-10.2f |\n",
                        num, customer.getId(), customer.getName(), customer.getAddress(),
                        customer.getMember().getMembershipName(), customer.getWallet());
                num++;
            }
        }
    }

    public static void showAllEmployee(List<Person> employeeList){
        int num = 1;
        System.out.printf("| %-4s | %-4s | %-11s | %-15s | %-15s |\n",
                "No.", "ID", "Nama", "Alamat", "Pengalaman");
        System.out.println("+========================================================================================+");

        for (Person person : employeeList) {
            if (person instanceof Employee) {
                Employee employee = (Employee) person;
                System.out.printf("| %-4s | %-4s | %-11s | %-15s | %-15s |\n",
                        num, employee.getId(), employee.getName(), employee.getAddress(),
                        employee.getExperience());
                num++;
            }
        }
    }

    public static void showAllService(List<Service> serviceList){
        int num = 1;
        System.out.printf("| %-4s | %-4s | %-11s | %-15s |\n",
                "No.", "ID", "Nama", "Harga");
        System.out.println("+========================================================================================+");

        for (Service service : serviceList) {

                System.out.printf("| %-4s | %-4s | %-11s | %-15s |\n",
                        num, service.getServiceId(), service.getServiceName(), service.getPrice());
                num++;

        }
    }

    public static void showHistoryReservation(List<Reservation> reservationList){
        int num = 1;
        double total = 0;
        System.out.printf("| %-4s | %-4s | %-11s | %-15s | %-15s | %-10s |\n",
                "No.", "ID", "Nama Customer", "Service", "Biaya Service", "Workstage");
        System.out.println("+========================================================================================+");
        for (Reservation reservation : reservationList) {
            System.out.printf("| %-4s | %-4s | %-11s | %-15s | %-15s | %-10s |\n",
                    num, reservation.getReservationId(), reservation.getCustomer().getName(), printServices(reservation.getServices()), reservation.getReservationPrice(), reservation.getWorkstage());
            num++;
            if (reservation.getWorkstage().equalsIgnoreCase("Finish")) {
                total = total + reservation.getReservationPrice();
            }
        }
        System.out.printf("| %-84s | %-15.2f |\n", "Total Keuntungan", total);
    }
}
