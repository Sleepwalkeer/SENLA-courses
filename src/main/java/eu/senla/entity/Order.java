package eu.senla.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@NamedEntityGraph(name = "graph.Order.allFields",
        attributeNodes = {
                @NamedAttributeNode(value = "items", subgraph = "subgraph.category"),
                @NamedAttributeNode(value = "customer", subgraph = "subgraph.credentials"),
                @NamedAttributeNode(value = "worker", subgraph = "subgraph.credentials")
        },
        subgraphs = {
                @NamedSubgraph(name = "subgraph.category",
                        attributeNodes = @NamedAttributeNode(value = "category")),
                @NamedSubgraph(name = "subgraph.credentials",
                        attributeNodes = @NamedAttributeNode(value = "credentials"))})
@Table(name = "rent_order")
public class Order {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Account customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", nullable = false)
    private Account worker;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "order_item",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items;


    @Column(name = "start_datetime", nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private LocalDateTime startDateTime;

    @Column(name = "end_datetime", nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private LocalDateTime endDateTime;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private Boolean deleted = false;
}
