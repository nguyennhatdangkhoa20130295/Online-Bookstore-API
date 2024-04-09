package vn.edu.hcmuaf.fit.websubject.service;

import vn.edu.hcmuaf.fit.websubject.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<Comment> getListCommentByUserId(int idUser);
    void addComment(int idProduct, int rate, String description);
    void deleteComment(int idComment);
}
