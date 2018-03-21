package ee.ttu.BookExchange.api.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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

    @Size(max = 2048)
    String conditiondesc;

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

    @Size(max = 32)
    String language;

    @Column(nullable = false, columnDefinition = "timestamp")
    Timestamp postdate;

    @Column(nullable = false)
    @Size(max = 32)
    String userid;

    @Size(max = 32)
    String genreid;

    @Size(max = 128)
    String city;
}
