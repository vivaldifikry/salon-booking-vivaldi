package com.booking.service;


import com.booking.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ReservationService {
    static Scanner input = new Scanner(System.in);
    private static List<Reservation> reservations = new ArrayList<>();
    private static int reservationCounter = 1;

    public static void createReservation(List<Person> personList, List<Service> serviceList, List<Reservation> reservationList){

        List<Service> selectedServices = new ArrayList<>();

        boolean tandaCostumer = false;      // tanda ditemukannya id yang dipilih user
        boolean tandaEmployee = false;
        int tandaService = 0;
        Customer selectedCustomer = null;
        Employee selectedEmployee = null;

        do {
            PrintService.showAllCustomer(personList);
            System.out.println("Silahkan Masukkan Customer Id:");
            String pilihCostumer = input.nextLine();

            if(ValidationService.validateCustomerId(personList, pilihCostumer)){
                selectedCustomer = (Customer) personList.stream()
                        .filter(person -> person.getId().equals(pilihCostumer) && person instanceof Customer)
                        .findFirst().orElse(null);

                tandaCostumer = true;
            }else
                System.out.println("Customer yang dicari tidak tersedia");
        }while (!tandaCostumer);

        do {
            PrintService.showAllEmployee(personList);
            System.out.println("Silahkan Masukkan Employee Id:");
            String pilihEmployee = input.nextLine();

            if(ValidationService.validateEmployeeId(personList, pilihEmployee)){
                selectedEmployee = (Employee) personList.stream()
                        .filter(person -> person.getId().equals(pilihEmployee) && person instanceof Employee)
                        .findFirst().orElse(null);

                tandaEmployee = true;
            }else
                System.out.println("Employee yang dicari tidak tersedia");
        }while (!tandaEmployee);


        String[] pilihService = new String[serviceList.size()];         // membuat array tampungan sepanjang list untuk cek redundan input
        int indexService = 0;       // index input array service
        boolean pilihServiceLagi = false;       // tanda jika user ingin memilih service banyak
        String tampunganCekService;
        double totalBiaya = 0;

        do {
            PrintService.showAllService(serviceList);
            System.out.println("Silahkan Masukkan Service Id:");
            tampunganCekService = input.nextLine();

            // cek input redundan
            if(indexService > 0 && ValidationService.validateInputService(pilihService, tampunganCekService)){
                System.out.println("Service sudah dipilih");
            } else{
                pilihService[indexService] = tampunganCekService;
                if(ValidationService.validateServiceId(serviceList, pilihService[indexService])){
                    tandaService++;
                    indexService++;

                    final String selectedServiceId = pilihService[indexService - 1];
                    Service selectedService = serviceList.stream()
                            .filter(service -> service.getServiceId().equals(selectedServiceId))
                            .findFirst()
                            .orElse(null);

                    if (selectedService != null) {
                        selectedServices.add(selectedService);
                    }

                    totalBiaya += selectedService.getPrice();

                    boolean tandaPilihServiceLagi = false;      // tanda perulangan user pilih service lagi

                    if (tandaService < serviceList.size()){     // kondisi jika user sudah max pilih service tidak diberi menu tambah service
                        do {
                            System.out.println("Ingin pilih service yang lain (Y/T)?");
                            char nextService = input.next().charAt(0);
                            input.nextLine();
                            if (nextService == 'Y' || nextService == 'y') {
                                pilihServiceLagi = true;
                            } else if (nextService == 'T' || nextService == 't') {
                                pilihServiceLagi = false;
                            } else {
                                System.out.println("Pilihan service yang dicari tidak tersedia");
                                tandaPilihServiceLagi = true;
                            }
                        }while (tandaPilihServiceLagi);
                    }
                }else {
                    System.out.println("Service yang dicari tidak tersedia");
                    pilihServiceLagi = true;
                }

            }

        }while (tandaService < serviceList.size() && pilihServiceLagi);

        assert selectedCustomer != null;
        double totalBiayaSetelahDiskon = ReservationService.hitungTotalBiayaSetelahDiskon(totalBiaya, selectedCustomer.getMember());

        if(selectedCustomer.getWallet() >= totalBiayaSetelahDiskon){
            System.out.println("Booking Berhasil!");
            System.out.println("Total Biaya Booking : Rp. " + totalBiayaSetelahDiskon);

            selectedCustomer.setWallet(selectedCustomer.getWallet()-totalBiayaSetelahDiskon);

            // lakukan proses add ke list Reservation, dimana ada ID, Nama costumer, service, total biaya, dan workstage
            String reservationId = "Rsv-" + String.format("%02d", reservationCounter++);

            Reservation newReservation = new Reservation(reservationId, selectedCustomer, selectedEmployee, selectedServices, "In Process");
            newReservation.setReservationPrice(totalBiayaSetelahDiskon); // Set harga dengan diskon
            reservationList.add(newReservation);
        }else
            System.out.println("Maaf, saldo costumer dengan id: "+selectedCustomer.getId()+", tidak mencukupi untuk reservasi");

    }

    public static void getCustomerByCustomerId(){
        
    }

    public static void editReservationWorkstage(List<Reservation> reservationList){
        boolean tandaReservation = false;   // tanda ditemukan id reservation

        do{
            PrintService.showRecentReservation(reservationList);
            System.out.println("Silahkan Masukkan Reservation Id:");
            String pilihReservation = input.nextLine();

            if(ValidationService.validateReservationId(reservationList, pilihReservation)){
                tandaReservation = true;
                System.out.println("Selesaikan reservasi (Finish/Cancel):");
                String editReservasi = input.nextLine();
                if(Objects.equals(editReservasi, "Finish") || Objects.equals(editReservasi, "Cancel")){
                    // ubah status workstage menjadi string pilihReservation
                    // output Reservasi dengan id "Rsv-01" sudah Finish
                    for (Reservation reservation : reservationList) {
                        if (reservation.getReservationId().equals(pilihReservation)) {
                            reservation.setWorkstage(editReservasi);
                            System.out.println("Reservasi dengan id \"" + pilihReservation + "\" sudah " + editReservasi);
                            break;
                        }
                    }
                } else {
                    System.out.println("Pilihan tidak valid. Silahkan pilih Finish atau Cancel.");
                }
            }else
                System.out.println("Reservasi yang dicari tidak tersedia");
        }while (!tandaReservation);
    }

    public static double hitungBiayaService(List<Service> serviceList, String id){
        for (Service service : serviceList) {
            if (Objects.equals(service.getServiceId(), id)) {
                return service.getPrice();
            }
        }
        return 0;
    }

    public static double hitungTotalBiayaSetelahDiskon(double totalBiaya, Membership membership) {
        double discount = 0.0;
        if (membership != null) {
            String membershipName = membership.getMembershipName();
            if ("Silver".equalsIgnoreCase(membershipName)) {
                discount = 0.05;
            } else if ("Gold".equalsIgnoreCase(membershipName)) {
                discount = 0.10;
            }
        }
        return totalBiaya - (totalBiaya * discount);
    }

    // Silahkan tambahkan function lain, dan ubah function diatas sesuai kebutuhan
}
