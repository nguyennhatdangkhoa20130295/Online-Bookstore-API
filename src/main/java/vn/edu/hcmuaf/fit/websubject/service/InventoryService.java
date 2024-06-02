package vn.edu.hcmuaf.fit.websubject.service;

import vn.edu.hcmuaf.fit.websubject.entity.Inventory;
import vn.edu.hcmuaf.fit.websubject.entity.Product;

import java.util.Optional;

public interface InventoryService {
    Optional<Inventory> getByProduct(Product product);
}
