package com.aninfo.model;

import javax.persistence.*;

@Entity
@Table(name = "Transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Double sum;
    private String type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cbu", nullable = false)
    private Account account;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
