package ru.practicum.requests.model;

import lombok.*;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Заявка на участие в событии
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "requests")
public class Request {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // Дата и время создания заявки
  @Column(nullable = false)
  private LocalDateTime created;

  // Событие
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "event_id", nullable = false)
  private Event event;

  // Пользователь, отправивший заявку
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "requester_id", nullable = false)
  private User requester;

  // Статус заявки
  @Enumerated
  @Column(nullable = false)
  private MembershipRequestStatus status;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Request request = (Request) o;
    return id.equals(request.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}

