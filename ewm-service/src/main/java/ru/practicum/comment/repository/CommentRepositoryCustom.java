package ru.practicum.comment.repository;

import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.CommentRequestParameters;

import java.util.List;

public interface CommentRepositoryCustom {

    List<Comment> findCommentsByParameters(CommentRequestParameters parameters);

}
