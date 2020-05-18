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
    @Mock
    private OrderItem orderItem;

    @Test
    public void checkConfirmAfter25HoursOfSubmit_shouldThrowOrderExpiredException() {
        DateTime dateTime = DateTime.now();
        DatatimeClock clock = Mockito.mock(DatatimeClock.class);

        when(clock.getTime())
            .thenReturn(dateTime)
            .thenReturn(dateTime.plusHours(25));

        Order order = new Order(clock);
        order.addItem(orderItem);
        order.submit();

        Assertions.assertThrows(OrderExpiredException.class, () -> order.confirm());
        Assertions.assertEquals(State.CANCELLED, order.getOrderState());
    }

}
