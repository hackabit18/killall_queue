package com.killall_queuehackabit.smartshop;
public class Order {

    public String dates;
    public String amount;

    public Order(){

    }

    public Order(String dates, String amount){
        this.dates = dates;
        this.amount = amount;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
