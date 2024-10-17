import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CustomerProcessor {

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/processCustomers")
    public void processCustomers() {
        // 1. Kunden-Daten von der API abrufen
        String url = "http://localhost:8080/v1/dataset"; // URL der API
        Customer[] customers = restTemplate.getForObject(url, Customer[].class);

        // 2. Logik anwenden, um die CustomerPost-Liste zu erhalten
        List<CustomerPost> customerPosts = Logik.giveCustomer(customers);

        // 3. Ergebnis zusammenstellen
        List<Map<String, Object>> results = new ArrayList<>();
        for (CustomerPost customerPost : customerPosts) {
            Map<String, Object> result = new HashMap<>();
            result.put("customerId", customerPost.getCustomerId());
            result.put("consumption", customerPost.getConsumption());
            results.add(result);
        }

        // 4. Erstellen des Request-Objekts für die Ergebnis-API
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("result", results);

        // 5. Senden der CustomerPost-Daten an das Ergebnis-System
        String resultUrl = "http://localhost:8080/v1/result"; // URL des Referenzsystems
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity(resultUrl, requestEntity, Void.class);
    }
}

