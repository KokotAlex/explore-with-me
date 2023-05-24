package ru.practicum.comment.service;

import ru.practicum.comment.dto.UpdateCommentRequest;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.CommentRequestParameters;
import ru.practicum.comment.model.CommentState;

import java.util.List;

public interface CommentService {

    Comment save(Comment commentToSave, Long authorId, Long eventId);

    Comment getById(Long commentId);

    Comment getUsersCommentById(Long userId, Long commentId);

    void delete(Long commentId, Long authorId);

    Comment update(UpdateCommentRequest updateCommentRequest, Long commentId, Long authorId);

    List<Comment> getByParameters(CommentRequestParameters parameters);

    List<Comment> getUsersCommentsByParameters(Long userId, CommentRequestParameters parameters);

    Comment updateState(Long commentId, CommentState state);

}
