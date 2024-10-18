package com.example.customer;
import java.time.LocalTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Logik {

    public static long hexTimeDifference(long time1, long time2) {
        long diffInMillis = Math.abs(time1 - time2);
        long hours = (diffInMillis / (1000 * 60 * 60)) % 24;
        long minutes = (diffInMillis / (1000 * 60)) % 60;
        long seconds = (diffInMillis / 1000) % 60;
        return diffInMillis;
    }

    public static List<CustomerPost> giveCustomer(Customer[] customers) {
        List<CustomerPost> customerPosts = new ArrayList<>();
        for (int i = 0; i < customers.length; i++) {
            for (int j = 0; j < customers.length; j++) {
                if (customers[i].getCustomerId().equals(customers[j].getCustomerId()) && i != j) {
                    long differenceAsLong = hexTimeDifference(customers[i].getTimestamp(),customers[j].getTimestamp());
                    customerPosts.add(new CustomerPost(customers[i].getCustomerId(), differenceAsLong));
                }
            }
        }
        return customerPosts;
    }
}
