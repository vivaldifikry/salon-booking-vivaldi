package com.booking.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Customer extends Person {
    private double wallet;
    private Membership member;

    public Customer(String id, String name, String address, double wallet, Membership member) {
        super(id, name, address);
        this.wallet = wallet;
        this.member = member;
    }
}
