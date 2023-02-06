package eu.senla.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "rent_order")
public class Order {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="customer_id", nullable=false)
    private Account customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="worker_id", nullable=false)
    private Account worker;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "order_item",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private Set<Item> items;


    @Column(name = "start_datetime", nullable = false)
    private Timestamp startDateTime;

    @Column(name = "end_datetime", nullable = false)
    private Timestamp endDateTime;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    public Order(Account customer, Account worker, Set<Item> items, Timestamp startDateTime, Timestamp endDateTime, BigDecimal totalPrice) {
        this.customer = customer;
        this.worker = worker;
        this.items = items;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.totalPrice = totalPrice;
    }
}
