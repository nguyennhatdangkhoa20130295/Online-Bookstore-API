package vn.edu.hcmuaf.fit.websubject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.websubject.entity.Promotion;
import vn.edu.hcmuaf.fit.websubject.payload.request.AddPromoRequest;
import vn.edu.hcmuaf.fit.websubject.service.PromotionService;

@RestController
@RequestMapping("/api/promotion")
public class PromotionController {
    @Autowired
    PromotionService promotionService;

    @GetMapping("")
    public ResponseEntity<Page<Promotion>> getAllPromotions(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "") String filter,
                                                            @RequestParam(defaultValue = "5") int perPage,
                                                            @RequestParam(defaultValue = "id") String sort,
                                                            @RequestParam(defaultValue = "DESC") String order) {
        try {
            Page<Promotion> promotions = promotionService.findAllByIsCode(page, perPage, sort, order, filter);
            return ResponseEntity.ok(promotions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Promotion> getPromotionById(@PathVariable int id) {
        try {
            Promotion promo = promotionService.getPromotionById(id);
            return ResponseEntity.ok(promo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Promotion> getPromotionByCode(@PathVariable String code) {
        try {
            Promotion promo = promotionService.getPromotionByCode(code);
            return ResponseEntity.ok(promo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/check/{code}")
    public ResponseEntity<String> checkPromoCode(@PathVariable String code) {
        try {
            boolean check = promotionService.checkPromoCode(code);
            if (check) {
                return ResponseEntity.ok("Voucher code đã tồn tại");
            } else {
                return ResponseEntity.ok("Voucher code chưa tồn tại");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addPromotion(@RequestBody AddPromoRequest promotion) {
        try {
            promotionService.addPromotion(promotion.getIdProduct(), promotion.getCode(), promotion.getDiscount(), promotion.getStartDate(), promotion.getEndDate());
            return ResponseEntity.ok().body("Added promotion successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<String> updatePromotion(@PathVariable int id, @RequestBody AddPromoRequest promotion) {
        try {
            promotionService.updatePromotion(id, promotion.getIdProduct(), promotion.getCode(), promotion.getDiscount(), promotion.getStartDate(), promotion.getEndDate());
            return ResponseEntity.ok().body("Updated promotion successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePromotion(@PathVariable int id) {
        try {
            promotionService.deletePromotion(id);
            return ResponseEntity.ok("Deleted");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
