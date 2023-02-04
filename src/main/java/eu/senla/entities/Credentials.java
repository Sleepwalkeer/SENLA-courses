package eu.senla.entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "credentials")
public class Credentials {

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "username", length = 30, nullable = false, unique = true)
    private String username;

    @Column(name = "password", length = 30, nullable = false)
    private String password;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Account account;

}
