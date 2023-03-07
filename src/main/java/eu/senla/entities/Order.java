package eu.senla.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@NamedEntityGraph(name = "graph.Order.itemsCategory",
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
    private Timestamp startDateTime;

    @Column(name = "end_datetime", nullable = false)
    private Timestamp endDateTime;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

}
