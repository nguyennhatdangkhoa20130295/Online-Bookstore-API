package vn.edu.hcmuaf.fit.websubject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.websubject.entity.Comment;
import vn.edu.hcmuaf.fit.websubject.entity.Product;
import vn.edu.hcmuaf.fit.websubject.entity.User;
import vn.edu.hcmuaf.fit.websubject.repository.CommentRepository;
import vn.edu.hcmuaf.fit.websubject.repository.ProductRepository;
import vn.edu.hcmuaf.fit.websubject.repository.UserRepository;
import vn.edu.hcmuaf.fit.websubject.service.CommentService;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Comment> getListCommentByUserId(int idUser) {
        return commentRepository.findAllByUserId(idUser);
    }

    @Override
    public void addComment(int idProduct, int rate, String description) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetailsImpl customUserDetails = (CustomUserDetailsImpl) authentication.getPrincipal();
        Optional<User> user = userRepository.findByUsername(customUserDetails.getUsername());
        if (user.isPresent()) {
            User currentUser = user.get();
            Optional<Product> productOptional = productRepository.findById(idProduct);
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                Comment comment = new Comment();
                comment.setProduct(product);
                comment.setUser(currentUser);
                comment.setRating(rate);
                comment.setCmtDetail(description);
                commentRepository.save(comment);
            } else {
                System.out.println("Không thể lưu bình luận");
            }
        }
    }

    @Override
    public void deleteComment(int idComment) {
        commentRepository.deleteById(idComment);
    }

}
