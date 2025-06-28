package order;

import java.util.List;
import java.util.Map;

public interface OrderDAO {
    void saveOrder(String userId, Map<Integer, Integer> cart);
    List<String> getOrdersByUser(String userId);
    List<String> getAllOrders();
}
