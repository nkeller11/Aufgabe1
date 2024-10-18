package com.example.customer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus; // Importiere HttpStatus
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;

@Controller
public class CustomerProcessor {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    List<Customer> customers = new ArrayList<>();

    @Value("${dataset.service.url}")
    private String datasetServiceUrl;

    @Value("${result.service.url}")
    private String resultServiceUrl;

    public CustomerProcessor(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/getCustomers")
    public ResponseEntity<List<Customer>> getCustomers() {
        try {
            customers.clear();
            // Die Daten als String abrufen
            String response = restTemplate.getForObject(datasetServiceUrl, String.class);

            // JSON-String in JsonNode umwandeln
            JsonNode jsonNode = objectMapper.readTree(response);

            // Durch das 'events' Array iterieren und Customer-Objekte hinzufügen
            for (JsonNode eventNode : jsonNode.get("events")) {
                Customer customer = new Customer();
                customer.setCustomerId(eventNode.get("customerId").asText());
                customer.setWorkloadId(eventNode.get("workloadId").asText());
                customer.setTimestamp(eventNode.get("timestamp").asLong());
                customer.setEventType(eventNode.get("eventType").asText());
                customers.add(customer);
            }

            // Die Liste von Customer-Objekten zurückgeben
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            // Im Fehlerfall eine 500-Fehlermeldung zurückgeben
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/postCustomerData")
    public ResponseEntity<Map<String, List<CustomerPost>>> postCustomerData(@RequestBody List<CustomerPost> customerPosts) {
        // Überprüfe, ob die übergebenen Daten gültig sind
        if (customerPosts == null || customerPosts.isEmpty()) {
            return ResponseEntity.badRequest().body(null);  // Überarbeiten, um null oder eine leere Liste zu vermeiden
        }

        // Map zur Aggregation der Verbrauchswerte pro CustomerId
        Map<String, Long> aggregatedConsumption = new HashMap<>();

        // Aggregation der Consumptions
        for (CustomerPost post : customerPosts) {
            aggregatedConsumption.merge(post.getCustomerId(), post.getConsumption(), Long::sum);
        }

        // Erstelle die Ergebnisliste mit aggregierten Daten
        List<CustomerPost> results = new ArrayList<>();
        for (Map.Entry<String, Long> entry : aggregatedConsumption.entrySet()) {
            results.add(new CustomerPost(entry.getKey(), entry.getValue()));
        }

        // Erstelle das endgültige Rückgabeformat
        Map<String, List<CustomerPost>> response = new HashMap<>();
        response.put("result", results);

        // Rückgabe der berechneten Ergebnisse im richtigen Format
        return ResponseEntity.ok(response);
    }

}









