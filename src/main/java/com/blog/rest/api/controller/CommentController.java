package com.blog.rest.api.controller;

import com.blog.rest.api.entity.Comment;
import com.blog.rest.api.payload.PagedResponse;
import com.blog.rest.api.payload.comment.CommentRequest;
import com.blog.rest.api.security.CurrentUser;
import com.blog.rest.api.security.UserPrincipal;
import com.blog.rest.api.service.CommentService;
import com.blog.rest.api.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Comment> addComment(
            @Valid @RequestBody CommentRequest commentRequest,
            @PathVariable(name = "postId") Long postId,
            @CurrentUser UserPrincipal currentUser) {

        Comment newComment = commentService.addComment(commentRequest, postId, currentUser);
        return new ResponseEntity<>(newComment, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getComment(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "id") Long id) {

        Comment comment = commentService.getCommentById(postId, id);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PagedResponse<Comment>> getAllComments(
            @PathVariable(name = "postId") Long postId,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

        PagedResponse<Comment> allComments = commentService.getAllComments(postId, page, size);
        return new ResponseEntity<>(allComments, HttpStatus.OK);
    }

}
