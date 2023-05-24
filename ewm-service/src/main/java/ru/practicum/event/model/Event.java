package ru.practicum.event.model;


import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.category.model.Category;
import ru.practicum.comment.model.Comment;
import ru.practicum.requests.model.Request;
import ru.practicum.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

/**
 * Список состояний жизненного цикла события
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "events")
public class Event {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;

  // Краткое описание.
  @Length(min = 20, max = 2000)
  @Column(nullable = false, length = 2000)
  private String annotation;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  // Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss").
  @PastOrPresent
  @Column(name = "created_on", nullable = false)
  private LocalDateTime createdOn;

  // Полное описание события.
  @Length(min = 20, max = 7000)
  @Column(nullable = false, length = 7000)
  private String description;

  // Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss").
  @FutureOrPresent
  @Column(name = "event_date", nullable = false)
  private LocalDateTime eventDate;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @NotNull
  @Embedded
  private Location location;

  // Нужно ли оплачивать участие.
  @Column(nullable = false)
  private Boolean paid;

  // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения.
  @PositiveOrZero
  @Column(name = "participant_limit")
  private Integer participantLimit;

  // Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss")
  @PastOrPresent
  @Column(name = "published_on")
  private LocalDateTime publishedOn;

  // Нужна ли пре-модерация заявок на участие
  @Column(name = "request_moderation", nullable = false)
  private Boolean requestModeration;

  // Состояние события
  @Enumerated
  @Column(nullable = false)
  private EventState state;

  // Заголовок
  @Length(min = 3, max = 120)
  @Column(nullable = false, length = 120)
  private String title;

  // Количество одобренных заявок на участие в данном событии/
  @Transient
  private Long confirmedRequests;

  // Количество просмотрев события.
  @Transient
  private Long views;

  // Запросы на участие
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
  private Set<Request> requests;

  // Комментарии
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
  private Set<Comment> comments;

  public Event(Long id,
               String annotation,
               Category category,
               LocalDateTime createdOn,
               String description,
               LocalDateTime eventDate,
               User user,
               Location location,
               Boolean paid,
               Integer participantLimit,
               LocalDateTime publishedOn,
               Boolean requestModeration,
               EventState state,
               String title,
               Long confirmedRequests
//               Set<Request> requests


  ) {
    this.id = id;
    this.annotation = annotation;
    this.category = category;
    this.createdOn = createdOn;
    this.description = description;
    this.eventDate = eventDate;
    this.user = user;
    this.location = location;
    this.paid = paid;
    this.participantLimit = participantLimit;
    this.publishedOn = publishedOn;
    this.requestModeration = requestModeration;
    this.state = state;
    this.title = title;
    this.confirmedRequests = confirmedRequests;
//    this.requests = requests;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Event event = (Event) o;
    return id.equals(event.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}

