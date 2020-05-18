package edu.iis.mto.time;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import edu.iis.mto.time.Order.State;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderTest {

    private DateTime dateTime;

    @Test
    public void checkIfTryToRealizeFromCreatedState() {
        Order order = new Order();
        order.addItem(new OrderItem());
        Assertions.assertThrows(OrderStateException.class, () -> order.realize());
    }

    @Test
    public void checkIfConfirmAfter24HoursOfSubmitShouldThrowOrderExpiredException() {
        dateTime = DateTime.now();
        DatatimeClock clock = Mockito.mock(DatatimeClock.class);
        when(clock.getTime())
            .thenReturn(dateTime)
            .thenReturn(dateTime.plusHours(25));

        Order order = new Order(clock);
        order.addItem(new OrderItem());
        order.submit();
        order.confirm();

        Assertions.assertThrows(OrderExpiredException.class, () -> order.confirm());
        Assertions.assertEquals(State.CANCELLED, order.getOrderState());
    }

    @Test
    public void checkIfConfirmAfter23HoursShouldSetStateRealized() {
        dateTime = DateTime.now();
        DatatimeClock clock = Mockito.mock(DatatimeClock.class);
        when(clock.getTime())
            .thenReturn(dateTime)
            .thenReturn(dateTime.plusHours(23));

        Order order = new Order(clock);
        order.addItem(new OrderItem());
        order.submit();
        order.confirm();
        order.realize();

        Assertions.assertEquals(State.REALIZED, order.getOrderState());
    }
}
