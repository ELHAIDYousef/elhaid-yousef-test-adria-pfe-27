package ma.enset.transactionservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 *
 * @author Yousef ELHAID
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private UUID id;
    private String customerReference;
    private String status;
    private String currency;
    private double balance;
}