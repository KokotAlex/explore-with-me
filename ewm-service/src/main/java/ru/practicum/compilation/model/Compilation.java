package ru.practicum.compilation.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.event.model.Event;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "compilations")
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(min = 3, max = 200)
    @Column(nullable = false, unique = true, length = 150)
    private String title;

    @Column(nullable = false)
    private Boolean pinned;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "compilations_event",
            joinColumns = @JoinColumn(name = "compilation_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "event_id", nullable = false))
    private Set<Event> events;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Compilation that = (Compilation) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Compilation{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", pinned=" + pinned +
                '}';
    }
}
