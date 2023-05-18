package kberestbrewery.web.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BeerDTO {

    @Null
    private UUID id;
    //private Long version;

    @NotBlank
    private String beerName;

    //@NotBlank
    private BeerStyleEnum beerStyle;
    private String upc;
    private Integer quantityOnHand;
    private BigDecimal price;
    private Timestamp createdDate;
    private Timestamp lastModifiedDate;
}
