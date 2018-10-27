package com.killall_queuehackabit.smartshop;

public class Profile {
    public UserOrders userOrders;
    public UserProfile userProfile;

    public Profile(){

    }
    public Profile(UserOrders userOrders,UserProfile userProfile){
        this.userOrders = userOrders;
        this.userProfile = userProfile;
    }

    public UserOrders getUserOrders() {
        return userOrders;
    }

    public void setUserOrders(UserOrders userOrders) {
        this.userOrders = userOrders;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
