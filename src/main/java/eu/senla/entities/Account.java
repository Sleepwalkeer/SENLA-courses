package eu.senla.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "account")

public class Account {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name", length = 25, nullable = false)
    private String firstName;

    @Column(name = "second_name", length = 25, nullable = false)
    private String secondName;

    @Column(name = "phone", length = 15, nullable = false, unique = true)
    private String phone;

    @Column(name = "email", length = 64, nullable = false, unique = true)
    private String email;

    @OneToOne( mappedBy = "account",fetch=FetchType.LAZY,cascade = CascadeType.REMOVE)
    @PrimaryKeyJoinColumn
    private Credentials credentials;
}
