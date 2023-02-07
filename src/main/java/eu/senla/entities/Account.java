package eu.senla.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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


    @OneToOne( fetch=FetchType.LAZY,cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "id")
    private Credentials credentials;


    public Account( String firstName, String secondName, String phone, String email, Credentials credentials) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.phone = phone;
        this.email = email;
        this.credentials = credentials;
    }
    public Account( String firstName, String secondName, String phone, String email) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.phone = phone;
        this.email = email;
    }

    public Account(int id) {
        this.id = id;
    }

    public Account(int id, String firstName, String secondName, String phone, String email) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.phone = phone;
        this.email = email;
    }
}
