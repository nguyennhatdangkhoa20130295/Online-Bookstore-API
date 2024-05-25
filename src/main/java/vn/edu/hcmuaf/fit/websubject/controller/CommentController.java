package vn.edu.hcmuaf.fit.websubject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.websubject.entity.Comment;
import vn.edu.hcmuaf.fit.websubject.payload.request.CommentRequest;
import vn.edu.hcmuaf.fit.websubject.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    @GetMapping("/product/{id}")
    public ResponseEntity<List<Comment>> getListCommentByProductId(@PathVariable int id) {
        try {
            List<Comment> comments = commentService.getListCommentByProductId(id);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/auth/{id}")
    public ResponseEntity<List<Comment>> getListCommentById(@PathVariable int id) {
        try {
            List<Comment> comments = commentService.getListCommentByUserId(id);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/add/{productId}")
    public ResponseEntity<String> addComment(@PathVariable int productId, @RequestBody CommentRequest commentRequest) {
        try {
            if (commentRequest.getRate() < 1 || commentRequest.getRate() > 5) {
                return ResponseEntity.badRequest().body("Rate must be between 1 and 5");
            }
            commentService.addComment(productId, commentRequest.getRate(), commentRequest.getDetail());
            System.out.println(productId + " " + commentRequest.getRate() + " " + commentRequest.getDetail());
            return ResponseEntity.ok("Added comment successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error when adding comment");
        }
    }

    @PutMapping("/update/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable int commentId, @RequestBody CommentRequest commentRequest) {
        try {
            if (commentRequest.getRate() < 1 || commentRequest.getRate() > 5) {
                return ResponseEntity.badRequest().body("Rate must be between 1 and 5");
            }
            commentService.updateComment(commentId, commentRequest.getRate(), commentRequest.getDetail());
            System.out.println(commentId + " " + commentRequest.getRate() + " " + commentRequest.getDetail());
            return ResponseEntity.ok("Updated comment successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error when updating comment");
        }
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable int commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("Delete comment successfully");
    }
}
