package io.security.springsecuritymaster.controller.rest_controller;

import io.security.springsecuritymaster.domain.comment.CommentRequestDto;
import io.security.springsecuritymaster.domain.comment.CommentResponseDto;
import io.security.springsecuritymaster.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{clothesId}")
    public ResponseEntity<Void> addComment(
            @PathVariable Long clothesId,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody CommentRequestDto commentRequestDto
    ) {
        commentService.addComment(clothesId, authHeader, commentRequestDto);
        return new ResponseEntity<>(HttpStatusCode.valueOf(201));
    }

    @GetMapping("/{clothesId}")
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long clothesId) {
        List<CommentResponseDto> commentList = commentService.getComments(clothesId);
        return new ResponseEntity<>(commentList, HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        commentService.deleteComment(commentId, authHeader);
        return new ResponseEntity<>(HttpStatusCode.valueOf(204));
    }
}
