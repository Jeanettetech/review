package codingblackfemales.gettingstarted;

import codingblackfemales.algo.AlgoLogic;
import codingblackfemales.sotw.OrderState;
import codingblackfemales.sotw.SimpleAlgoState;
import junit.framework.Assert;
import messages.order.Side;
import org.junit.Test;
import codingblackfemales.sotw.ChildOrder;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

/**
 * This test plugs together all of the infrastructure, including the order book (which you can trade against)
 * and the market data feed.
 *
 * If your algo adds orders to the book, they will reflect in your market data coming back from the order book.
 *
 * If you cross the srpead (i.e. you BUY an order with a price which is == or > askPrice()) you will match, and receive
 * a fill back into your order from the order book (visible from the algo in the childOrders of the state object.
 *
 * If you cancel the order your child order will show the order status as cancelled in the childOrders of the state object.
 *
 */
public class MyAlgoBackTest extends AbstractAlgoBackTest {

    @Override
    public AlgoLogic createAlgoLogic() {
        return new codingblackfemales.gettingstarted.MyAlgoLogic();
    }

    @Test
    public void testExampleBackTest() throws Exception {
        //create a sample market data tick....
        send(createTick());

        //ADD asserts when you have implemented your algo logic
        //assertEquals(container.getState().getChildOrders().size(), 3);

        //when: market data moves towards us
        send(createTick2());

        //then: get the state
        var state = container.getState();
        assertEquals(state.getChildOrders().size(), 4);

        //Check things like filled quantity, cancelled order count etc....
        //long filledQuantity = state.getChildOrders().stream().map(ChildOrder::getFilledQuantity).reduce(Long::sum).get();
        //and: check that our algo state was updated to reflect our fills when the market data
        //assertEquals(225, filledQuantity);
    }

    @Test
    public void activeTest() throws Exception {
        //create a sample market data tick....
        send(createTick());

        //ADD asserts when you have implemented your algo logic
        //assertEquals(container.getState().getChildOrders().size(), 3);

        //when: market data moves towards us
        send(createTick2());

        //then: get the state
        var state = container.getState();
        assertEquals(state.getActiveChildOrders().size(), 3);

        //Check things like filled quantity, cancelled order count etc....
        //long filledQuantity = state.getChildOrders().stream().map(ChildOrder::getFilledQuantity).reduce(Long::sum).get();
        //and: check that our algo state was updated to reflect our fills when the market data
        //assertEquals(225, filledQuantity);
    }

    @Test
    public void ChildOrderSize() throws Exception {
        // given

        send(createTick());

        //when
        var state = container.getState();

        //then
        assertEquals(state.getChildOrders().size(), 2);
    }

    @Test
    public void BuyOrderCreated() throws Exception {
        // given

        send(createTick());

        //when
        var state = container.getState();
        final var buyOrders = state.getActiveChildOrders().stream().filter(order -> order.getSide().equals(Side.BUY)).toList();


        //then
        assertEquals("Buy order size is 1", 1, buyOrders.size());
    }

    @Test
    public void SellOrderCreated() throws Exception {
        // given

        send(createTick());

        //when
        var state = container.getState();
        final var sellOrders = state.getActiveChildOrders().stream().filter(order -> order.getSide().equals(Side.SELL)).toList();
        //then
        assertTrue("Sell order size is 0", sellOrders.isEmpty());
    }

    @Test
    public void FillOrderCreated() throws Exception {
        // given

        send(createTick());

        //when
        SimpleAlgoState state = container.getState();
        final var filledState = state.getChildOrders().stream().filter(order -> order.getState() == OrderState.FILLED).toList();
        //then
        assertEquals("Filled order size is 1", 1, filledState.size());
    }

    @Test
    public void OrderCancelled() throws Exception {
        // given

        send(createTick());

        //when
        var state = container.getState();
        final var cancelledOrders = state.getChildOrders().stream().filter(order -> order.getState() == OrderState.CANCELLED).toList();
        //then
        assertEquals("Cancelled order size is 1", 1, cancelledOrders.size());
    }

    @Test
    public void VWAPCalculation() throws Exception {
        // given

        send(createTick());

        var state = container.getState(); // Ensuring state is retrieved
        codingblackfemales.gettingstarted.MyAlgoLogic algoLogic = new codingblackfemales.gettingstarted.MyAlgoLogic();//instance of algo logic to call the test on
        long calculatedVWAP = algoLogic.calculateVWAP(state); // Capturing result of the calculation

        // then

        assertEquals("VWAP calculation is", 100, calculatedVWAP);

    }

    @Test
    public void volumeImbalanceIndicator() throws Exception {
        // given

        send(createTick());

        //when
        var state = container.getState(); // Ensuring state is retrieved
        codingblackfemales.gettingstarted.MyAlgoLogic algoLogic = new codingblackfemales.gettingstarted.MyAlgoLogic();
        double calculatedVolumeImbalanceIndication = algoLogic.calculateVolumeImbalance(state); // Capturing result of the calculation

        // then

        assertEquals("Volume Imbalance calculation is", -0.3333333333333333, calculatedVolumeImbalanceIndication);

    }

    @Test
    public void FilledQuantity() throws Exception {
        // given


        send(createTick());

        // when
        codingblackfemales.gettingstarted.MyAlgoLogic algoLogic = new codingblackfemales.gettingstarted.MyAlgoLogic();
        SimpleAlgoState state = container.getState();

        long filledQuantity = state.getChildOrders().stream()
                .mapToLong(ChildOrder::getFilledQuantity)
                .sum();

        // then
        assertEquals("Filled order quantity is 101", 101,filledQuantity);


    }
}




