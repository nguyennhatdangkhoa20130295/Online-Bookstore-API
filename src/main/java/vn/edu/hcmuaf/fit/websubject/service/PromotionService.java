package vn.edu.hcmuaf.fit.websubject.service;

import org.springframework.data.domain.Page;
import vn.edu.hcmuaf.fit.websubject.entity.Contact;
import vn.edu.hcmuaf.fit.websubject.entity.Promotion;

import java.util.Date;
import java.util.List;

public interface PromotionService {
    List<Promotion> getAllPromotions();
    public Page<Promotion> findAllByIsCode(int page, int size, String sort, String order, String filter);
    Promotion getPromotionById(int id);
    Promotion getPromotionByCode(String code);
    boolean checkPromoCode(String code);
    void addPromotion(Integer idProduct, String code, int discount, Date startDate, Date endDate);
    void updatePromotion(int id, Integer idProduct, String code, int discount, Date startDate, Date endDate);
    void deletePromotion(int id);
}
