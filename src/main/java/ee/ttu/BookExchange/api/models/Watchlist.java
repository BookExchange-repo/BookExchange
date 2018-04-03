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

    @Column(nullable = false)
    int userid;

    @Column(nullable = false)
    int bookid;
}
