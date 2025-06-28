package order;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class OrderVO {
    private int orderId;
    private String userId;
    private Map<Integer, Integer> cart = new HashMap<>();

    public OrderVO(int orderId, String userId, Map<Integer, Integer> cart) {
        this.orderId = orderId;
        this.userId = userId;
        this.cart = cart;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public Map<Integer, Integer> getCart() {
        return cart;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(orderId).append("|").append(userId).append("|");

        if (cart.isEmpty()) {
            sb.append("");
        } else {
            StringJoiner joiner = new StringJoiner(",");
            for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
                joiner.add(entry.getKey() + ":" + entry.getValue());
            }
            sb.append(joiner.toString());
        }
        return sb.toString();
    }

    public static OrderVO fromString(String s) {
        String[] parts = s.split("\\|");
        if (parts.length != 3) throw new IllegalArgumentException("Invalid order string format");

        int orderId = Integer.parseInt(parts[0]);
        String userId = parts[1];
        Map<Integer, Integer> cart = new HashMap<>();

        if (!parts[2].isEmpty()) {
            String[] items = parts[2].split(",");
            for (String item : items) {
                String[] kv = item.split(":");
                if (kv.length == 2) {
                    try {
                        int snackNo = Integer.parseInt(kv[0]);
                        int quantity = Integer.parseInt(kv[1]);
                        cart.put(snackNo, quantity);
                    } catch (NumberFormatException ignored) {}
                }
            }
        }
        return new OrderVO(orderId, userId, cart);
    }
}
