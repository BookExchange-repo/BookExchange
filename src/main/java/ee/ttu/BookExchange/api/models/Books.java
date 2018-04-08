package ee.ttu.BookExchange.api.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class Books {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    int id;

    @Column(nullable = false)
    @Size(max = 256)
    String title;

    @Size(max = 256)
    String author;

    @Column(nullable = false)
    @Size(max = 8192)
    String description;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "conditiondesc", referencedColumnName = "id", nullable = false)
    ConditionEng conditiondesc;

    @Digits(integer = 8, fraction = 2)
    @Column(nullable = false, columnDefinition = "decimal(8,2) unsigned")
    BigDecimal price;

    @Column(columnDefinition = "int default 0")
    int likes = 0;

    @Size(max = 16)
    String isbn;

    @Column(nullable = false)
    @Size(max = 512)
    String imagepath;

    @Size(max = 128)
    String publisher;

    @Size(max = 6)
    String pubyear;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "language", referencedColumnName = "id", nullable = false)
    LanguageEng language;

    @Column(nullable = false, columnDefinition = "timestamp")
    Timestamp postdate;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "userid", referencedColumnName = "id", nullable = false)
    Users userid;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "genreid", referencedColumnName = "id", nullable = false)
    GenreEng genreid;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "city", referencedColumnName = "id", nullable = false)
    City city;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "status", referencedColumnName = "id", nullable = false, columnDefinition = "int default 1")
    StatusEng status;
}
