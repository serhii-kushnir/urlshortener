package ua.shortener.user;


import jakarta.persistence.*;
import lombok.Data;
import ua.shortener.link.Link;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_name", length = 100, nullable = false)
    private String name;

    @Column(nullable = false, length = 100,
            columnDefinition = "VARCHAR(100) CHECK (password ~ '^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$')")
    private String password;

    @Column(nullable = false, length = 100,
            columnDefinition = "VARCHAR(100) CHECK (email ~ '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$')")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean enabled;

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Link> links = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
