package za.co.wethinkcode.mmayibo.fixme.broker.model;

import lombok.Getter;
import lombok.Setter;
import za.co.wethinkcode.mmayibo.fixme.core.model.Wallet;

@Getter @Setter
public class UserClientData {
    String name;
    String userName;
    Wallet wallet;
}
