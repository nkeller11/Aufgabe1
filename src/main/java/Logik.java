import java.time.LocalTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Logik {

    public static String hexTimeDifference(String time1, String time2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime t1 = LocalTime.parse(time1, formatter);
        LocalTime t2 = LocalTime.parse(time2, formatter);
        Duration diff = Duration.between(t1, t2);
        long hours = diff.toHours();
        long minutes = diff.toMinutes() % 60;
        long seconds = diff.getSeconds() % 60;
        LocalTime timeDifference = LocalTime.of((int) hours, (int) minutes, (int) seconds);
        String timeDiffString = timeDifference.format(formatter);
        return stringToHex(timeDiffString);
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static List<CustomerPost> giveCustomer(Customer[] customers) {
        List<CustomerPost> customerPosts = new ArrayList<>();
        for (int i = 0; i < customers.length; i++) {
            for (int j = 0; j < customers.length; j++) {
                if (customers[i].getCustomerId().equals(customers[j].getCustomerId()) && i != j) {
                    String hexDifference = hexTimeDifference(customers[i].getTimestamp().toString(), customers[j].getTimestamp().toString());
                    customerPosts.add(new CustomerPost(customers[i].getCustomerId(), hexDifference));
                }
            }
        }
        return customerPosts;
    }

    private static String stringToHex(String str) {
        StringBuilder hexString = new StringBuilder();
        for (char c : str.toCharArray()) {
            hexString.append(String.format("%02x", (int) c));
        }
        return hexString.toString();
    }
}
