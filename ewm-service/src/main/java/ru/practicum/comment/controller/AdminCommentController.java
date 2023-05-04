package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.CommentState;
import ru.practicum.comment.service.CommentService;

import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/comments")
@Validated
public class AdminCommentController {

    public final CommentService commentService;

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateStatusComment(
            @PathVariable @Positive Long commentId,
            @RequestParam CommentState state) {
        log.info("Processing a request to update state of comment with id: {}. State: {}", commentId, state);

        Comment updatedComment = commentService.updateState(commentId, state);
        CommentDto updatedCommentDto = CommentMapper.toCommentDto(updatedComment);

        return ResponseEntity.status(HttpStatus.OK).body(updatedCommentDto);
    }

}
