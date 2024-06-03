package vn.edu.hcmuaf.fit.websubject.service.impl;

import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.websubject.entity.Promotion;
import vn.edu.hcmuaf.fit.websubject.repository.PromotionRepository;
import vn.edu.hcmuaf.fit.websubject.service.PromotionService;

import java.util.List;
import java.util.Optional;

@Service
public class PromotionServiceImpl implements PromotionService {
    final
    PromotionRepository promotionRepository;

    public PromotionServiceImpl(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Override
    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }

    @Override
    public Promotion getPromotionById(int id) {
        return promotionRepository.findById(id).orElse(null);
    }

    @Override
    public Promotion getPromotionByCode(String code) {
        Optional<Promotion> promoCode = promotionRepository.findByCode(code);
        return promoCode.orElse(null);
    }


    @Override
    public void addPromotion(Promotion promotion) {

    }

    @Override
    public void updatePromotion(Promotion promotion) {

    }

    @Override
    public void deletePromotion(int id) {

    }
}
