package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.dto.UpdateCommentRequest;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.CommentRequestParameters;
import ru.practicum.comment.model.CommentSort;
import ru.practicum.comment.model.CommentState;
import ru.practicum.comment.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
@Validated
public class PrivateCommentController {

    public final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDto> addComment(
            @PathVariable @Positive Long userId,
            @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info("Processing a request to add new comment: {} from user: {}", newCommentDto, userId);

        Comment comment = CommentMapper.toComment(newCommentDto);
        Comment savedComment = commentService.save(comment, userId, newCommentDto.getEvent());
        CommentDto commentToReturn = CommentMapper.toCommentDto(savedComment);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentToReturn);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long commentId) {
        log.info("Processing a request to delete comment: {} from user: {}", commentId, userId);

        commentService.delete(commentId, userId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long commentId,
            @RequestBody UpdateCommentRequest updateCommentRequest) {
        log.info("Processing a request to update comment with id: {} for user with id: {}", commentId, userId);

        Comment updatedComment = commentService.update(updateCommentRequest, commentId, userId);
        CommentDto updatedCommentDto = CommentMapper.toCommentDto(updatedComment);

        return ResponseEntity.status(HttpStatus.OK).body(updatedCommentDto);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getComment(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long commentId) {
        log.info("Processing a request to get comment by id: {} for user: {}", commentId, userId);

        Comment comment = commentService.getUsersCommentById(userId, commentId);
        CommentDto commentToReturn = CommentMapper.toCommentDto(comment);

        return ResponseEntity.status(HttpStatus.OK).body(commentToReturn);
    }


    @GetMapping
    public ResponseEntity<List<CommentDto>> getAuthorsComments(
            @PathVariable @Positive Long userId,
            @RequestParam(required = false) String text,
            @RequestParam(required = false) Long eventId,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end,
            @RequestParam(required = false) CommentSort sort,
            @RequestParam(required = false) CommentState state,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        CommentRequestParameters parameters = CommentRequestParameters.builder()
                                                                      .text(text)
                                                                      .eventId(eventId)
                                                                      .start(start)
                                                                      .end(end)
                                                                      .state(state)
                                                                      .sort(sort)
                                                                      .from(from)
                                                                      .size(size)
                                                                      .build();
        log.info("Processing a request to get user's comments by parameters: {}", parameters);

        List<Comment> comments = commentService.getUsersCommentsByParameters(userId, parameters);
        List<CommentDto> commentDtos = comments.stream()
                                               .map(CommentMapper::toCommentDto)
                                               .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(commentDtos);
    }
}
