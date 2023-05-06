package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.dto.UpdateCommentRequest;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.CommentRequestParameters;
import ru.practicum.comment.model.CommentState;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.util.List;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private  final CommentRepository commentRepository;
    public final UserService userService;
    public final EventService eventService;

    @Override
    @Transactional
    public Comment save(Comment commentToSave, Long authorId, Long eventId) {
        log.info("Start saving comment: {} with author: {} for event: {}", commentToSave, authorId, eventId);

        // Найдем пользователя.
        User user = userService.getById(authorId);
        // Найдем событие.
        Event event = eventService.getById(eventId);

        // Заполним данные комментария.
        commentToSave.setAuthor(user);
        commentToSave.setEvent(event);
        commentToSave.setCreated(now());
        commentToSave.setStatus(CommentState.PENDING);

        // Сохраним сформированный комментарий.
        return commentRepository.save(commentToSave);
    }

    @Override
    public Comment getById(Long commentId) {
        log.info("Start getting comment by id: {}", commentId);

        return commentRepository
                .findById(commentId)
                .orElseThrow(() -> new NotFoundException(Comment.class.getSimpleName(), commentId));
    }

    @Override
    public Comment getUsersCommentById(Long userId, Long commentId) {
        log.info("Start getting comment by id: {} for user: {}", commentId, userId);

        // Найдем пользователя.
        User user = userService.getById(userId);

        CommentRequestParameters parameters = CommentRequestParameters.builder()
                                                                      .authorId(userId)
                                                                      .build();
        List<Comment> comments = commentRepository.findCommentsByParameters(parameters);
        if (comments.isEmpty()) {
            throw new NotFoundException(String.format("Not found comment with id: %d with author %s", commentId, user));
        }

        return comments.get(0);
    }

    @Override
    @Transactional
    public void delete(Long commentId, Long authorId) {
        log.info("Start deleting comment: {} from user: {}", commentId, authorId);

        // Найдем комментарий.
        Comment comment = getById(commentId);

        // Проверим, что комментарий удаляет автор комментария.
        if (!comment.getAuthor().getId().equals(authorId)) {
            throw new BadRequestException("Only the author can delete a comment");
        }

        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public Comment update(UpdateCommentRequest updateCommentRequest, Long commentId, Long authorId) {
        log.info("Start updating comment by id: {} for user with id {}", commentId, authorId);

        // Найдем комментарий.
        Comment commentToUpdate = getById(commentId);

        // Проверим, что комментарий обновляет автор комментария.
        if (!commentToUpdate.getAuthor().getId().equals(authorId)) {
            throw new BadRequestException("Only the author can update a comment");
        }

        // Обновим описание комментария.
        String description = updateCommentRequest.getDescription();
        if (!description.isBlank()) {
            commentToUpdate.setDescription(description);
        }

        // Обновим статус комментария.
        UpdateCommentRequest.State state = updateCommentRequest.getStatus();
        if (state != null && state.equals(UpdateCommentRequest.State.CANCELED)) {
            commentToUpdate.setStatus(CommentState.CANCELED);
        } else {
            // Во всех остальных случаях устанавливаем статус PENDING
            commentToUpdate.setStatus(CommentState.PENDING);
        }

        // Сохраним сформированный комментарий.
        return commentRepository.save(commentToUpdate);
    }

    @Override
    @Transactional
    public Comment updateState(Long commentId, CommentState state) {
        log.info("Start updating state of comment with id: {}. State: {}", commentId, state);

        // Найдем комментарий.
        Comment commentToUpdate = getById(commentId);

        // Установим соответствующий статус.
        commentToUpdate.setStatus(state);

        // Сохраним обновленный комментарий.
        return commentRepository.save(commentToUpdate);
    }

    @Override
    public List<Comment> getByParameters(CommentRequestParameters parameters) {
        log.info("Start finding comments by parameters: {}", parameters);

        // Будем выдавать только опубликованные комментарии.
        parameters.setState(CommentState.PUBLISHED);

        return commentRepository.findCommentsByParameters(parameters);
    }

    @Override
    public List<Comment> getUsersCommentsByParameters(Long userId, CommentRequestParameters parameters) {
        log.info("Start finding comments by parameters: {} for user: {}", parameters, userId);

        // Будем выдавать только комментарии пользователя.
        parameters.setAuthorId(userId);

        return commentRepository.findCommentsByParameters(parameters);
    }
}
