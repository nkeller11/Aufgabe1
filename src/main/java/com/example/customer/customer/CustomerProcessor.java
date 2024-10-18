package com.example.customer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CustomerProcessor {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${dataset.service.url}")
    private String datasetServiceUrl;

    public CustomerProcessor(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/getCustomers")
    public ResponseEntity<List<Customer>> getCustomers() {
        try {
            // Die Daten als String abrufen
            String response = restTemplate.getForObject(datasetServiceUrl, String.class);

            // JSON-String in JsonNode umwandeln
            JsonNode jsonNode = objectMapper.readTree(response);

            // Liste von Customer-Objekten erstellen
            List<Customer> customers = new ArrayList<>();

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
            return null;
        }
    }
}




