package agents;

import models.ItemsEnum;

import java.util.Date;

public interface IOrderAgent {

    void submitNewOrder(ItemsEnum item);

    default Date getReplyDeadline() {
        return new Date(System.currentTimeMillis() + 5 * 1000);
    }
}
