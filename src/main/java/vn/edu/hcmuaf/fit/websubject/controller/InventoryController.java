package vn.edu.hcmuaf.fit.websubject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.websubject.entity.Inventory;
import vn.edu.hcmuaf.fit.websubject.payload.request.InventoryRequest;
import vn.edu.hcmuaf.fit.websubject.service.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/api/inventories")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<?> getAllInventories(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int perPage,
                                               @RequestParam(defaultValue = "id") String sort,
                                               @RequestParam(defaultValue = "{}") String filter,
                                               @RequestParam(defaultValue = "ASC") String order) {
        Page<Inventory> inventories = inventoryService.getAllInventories(page, perPage, sort, filter, order);
        return ResponseEntity.ok(inventories);
    }

    @PostMapping("/add")
    public ResponseEntity<?> createInventories(@RequestBody List<InventoryRequest> inventoryRequests) {
        List<Inventory> createdInventories = inventoryService.createInventories(inventoryRequests);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInventories);
    }
}
