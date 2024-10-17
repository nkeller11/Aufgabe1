public class CustomerPost {
    private String customerId;
    private long consumption;
    public CustomerPost(String customerId, long consumption) {
        this.customerId = customerId;
        this.consumption = consumption;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public int getConsumption() {
        return consumption;
    }

    public void setConsumption(long consumption) {
        this.consumption = consumption;
    }
}
