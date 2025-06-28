package order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cart.CartService;
import snack.SnackDAO;
import snack.SnackVO;

public class OrderService {
    private OrderDAO orderDAO;
    private CartService cartService;
    private SnackDAO snackDAO;

    public OrderService(OrderDAO orderDAO, CartService cartService, SnackDAO snackDAO) {
        this.orderDAO = orderDAO;
        this.cartService = cartService;
        this.snackDAO = snackDAO;
    }

    public String orderSnack(String userId) {
        List<CartService.CartItem> cartItems = cartService.getUserCart(userId);
        if (cartItems == null || cartItems.isEmpty()) {
            return "장바구니가 비어있습니다.";
        }

        for (CartService.CartItem item : cartItems) {
            SnackVO snack = snackDAO.selectSnack(item.getSnackNo());
            if (snack == null) {
                return "간식 " + item.getSnackNo() + "가 존재하지 않습니다.";
            }
            if (snack.getStock() < item.getQuantity()) {
                return "간식 " + item.getSnackNo() + " 재고가 부족합니다.";
            }
        }

        for (CartService.CartItem item : cartItems) {
            SnackVO snack = snackDAO.selectSnack(item.getSnackNo());
            snack.setStock(snack.getStock() - item.getQuantity());
            snackDAO.updateSnack(snack);
        }

        Map<Integer, Integer> orderMap = new HashMap<>();
        for (CartService.CartItem item : cartItems) {
            orderMap.put(item.getSnackNo(), item.getQuantity());
        }

        orderDAO.saveOrder(userId, orderMap);
        cartService.clearCart(userId);
        return "주문이 완료되었습니다.";
    }

    public List<OrderVO> getOrdersByUser(String userId) {
        List<OrderVO> allOrders = orderDAO.getAllOrders().stream()
                .map(OrderVO::fromString)
                .filter(o -> o != null)
                .collect(Collectors.toList());

        return allOrders.stream()
                .filter(order -> userId.equals(order.getUserId()))
                .collect(Collectors.toList());
    }
    
    public List<OrderVO> getAllOrders() {
        return orderDAO.getAllOrders().stream()
                .map(OrderVO::fromString)
                .filter(o -> o != null)
                .collect(Collectors.toList());
    }


    public List<String> listOrders(String userId) {
        return orderDAO.getOrdersByUser(userId);
    }
}
