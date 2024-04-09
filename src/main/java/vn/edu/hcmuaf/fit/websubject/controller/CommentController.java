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

    @GetMapping("/{id}")
    public ResponseEntity<List<Comment>> getListCommentById(@PathVariable int id) {
        List<Comment> comments = commentService.getListCommentByUserId(id);
        return ResponseEntity.ok(comments);
    }
    @PutMapping("/add/{productId}")
    public ResponseEntity<String> addComment(@PathVariable int productId, @RequestBody CommentRequest commentRequest) {
        commentService.addComment(productId, commentRequest.getRate(), commentRequest.getDetail());
        System.out.println(productId + " " + commentRequest.getRate() + " " + commentRequest.getDetail());
        return ResponseEntity.ok("Added comment successfully");
    }
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable int commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("Delete comment successfully");
    }
}
