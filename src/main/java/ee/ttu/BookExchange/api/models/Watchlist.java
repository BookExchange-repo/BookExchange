package ee.ttu.BookExchange.api.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Watchlist {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    int id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "userid", referencedColumnName = "id", nullable = false)
    Users userid;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "bookid", referencedColumnName = "id", nullable = false)
    Books bookid;
}
