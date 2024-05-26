package com.aninfo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Double sum;

    private Long accountCbu;

    public Transaction() {
    }

    public Transaction(Double sum, Long AccountCbu) {
        this.sum = sum;
        this.accountCbu = AccountCbu;
    }

    public Long getId() {
        return id;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public Long getAccountCbu() {
        return accountCbu;
    }

}