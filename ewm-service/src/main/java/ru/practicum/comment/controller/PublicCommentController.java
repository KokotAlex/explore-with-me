package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.CommentRequestParameters;
import ru.practicum.comment.model.CommentSort;
import ru.practicum.comment.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
@Validated
public class PublicCommentController {

    public final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentDto>> getComments(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) Long eventId,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end,
            @RequestParam(required = false) CommentSort sort,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        CommentRequestParameters parameters = CommentRequestParameters.builder()
                                                                      .text(text)
                                                                      .authorId(authorId)
                                                                      .eventId(eventId)
                                                                      .start(start)
                                                                      .end(end)
                                                                      .sort(sort)
                                                                      .from(from)
                                                                      .size(size)
                                                                      .build();
        log.info("Processing a request to get comments by parameters: {}", parameters);

        List<Comment> comments = commentService.getByParameters(parameters);
        List<CommentDto> commentDtos = comments.stream()
                                               .map(CommentMapper::toCommentDto)
                                               .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(commentDtos);
    }

}
