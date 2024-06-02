package vn.edu.hcmuaf.fit.websubject.service;

import vn.edu.hcmuaf.fit.websubject.entity.Promotion;

import java.util.List;

public interface PromotionService {
    List<Promotion> getAllPromotions();
    Promotion getPromotionById(int id);
    void addPromotion(Promotion promotion);
    void updatePromotion(Promotion promotion);
    void deletePromotion(int id);
}
