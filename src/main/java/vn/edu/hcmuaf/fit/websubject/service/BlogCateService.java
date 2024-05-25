package vn.edu.hcmuaf.fit.websubject.service;

import org.springframework.data.domain.Page;
import vn.edu.hcmuaf.fit.websubject.entity.BlogCategory;

import java.util.List;
import java.util.Optional;

public interface BlogCateService {
    List<BlogCategory> getAllCate();

    Optional<BlogCategory> getCateById(int id);

    Page<BlogCategory> findAll(int page, int size, String sort, String order, String filter);

    Page<BlogCategory> getAllBlogCate(int page, int perPage);

    Optional<BlogCategory> findByBlogId(int id);
}
