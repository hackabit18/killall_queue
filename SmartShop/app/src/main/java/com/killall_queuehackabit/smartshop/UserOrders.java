package com.killall_queuehackabit.smartshop;


public class UserOrders {
    public Order order;
    public int index;

    public UserOrders(){

    }
    public UserOrders(Order order, int index){
        this.order = order;
        this.index = index;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
