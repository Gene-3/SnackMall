package cart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import snack.SnackDAO;
import snack.SnackVO;

public class CartService {

    // 사용자별 장바구니 저장
    private Map<String, Map<Integer, Integer>> userCartMap = new HashMap<>();

    // 간식 데이터 접근용 DAO
    private SnackDAO snackDAO;

    // 생성자
    public CartService(SnackDAO snackDAO) {
        this.snackDAO = snackDAO;
    }

    // 장바구니에 간식 추가
    public String addToCart(String userId, int snackNo, int qty) {
        SnackVO snack = snackDAO.selectSnack(snackNo);
        if (snack == null) return "해당 번호의 간식이 없습니다.";
        if (qty <= 0) return "수량은 1 이상이어야 합니다.";

        int stock = snack.getStock();
        Map<Integer, Integer> cart = userCartMap.getOrDefault(userId, new HashMap<>());
        int currentQty = cart.getOrDefault(snackNo, 0);

        if (stock < currentQty + qty) {
            int available = stock - currentQty;
            return "간식 '" + snack.getName() + "' 재고가 부족합니다. 현재 남은 수량: " + available + "개";
        }

        cart.put(snackNo, currentQty + qty);
        userCartMap.put(userId, cart);

        return "간식 '" + snack.getName() + "' " + qty + "개가 장바구니에 추가되었습니다.";
    }

    // 장바구니 비우기
    public void clearCart(String userId) {
        userCartMap.remove(userId);
    }

    // 장바구니 아이템 클래스
    public static class CartItem {
        private int snackNo;
        private String name;
        private int price;
        private int quantity;

        public CartItem(int snackNo, String name, int price, int quantity) {
            this.snackNo = snackNo;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        public int getSnackNo() { return snackNo; }
        public String getName() { return name; }
        public int getPrice() { return price; }
        public int getQuantity() { return quantity; }

        @Override
        public String toString() {
            return String.format("번호: %d | 이름: %s | 가격: %d | 수량: %d", snackNo, name, price, quantity);
        }
    }

    // 사용자 장바구니 목록 반환
    public List<CartItem> getUserCart(String userId) {
        List<CartItem> cartItems = new ArrayList<>();
        Map<Integer, Integer> cart = userCartMap.getOrDefault(userId, new HashMap<>());

        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
            int snackNo = entry.getKey();
            int qty = entry.getValue();
            SnackVO snack = snackDAO.selectSnack(snackNo);
            if (snack != null) {
                cartItems.add(new CartItem(snackNo, snack.getName(), snack.getPrice(), qty));
            }
        }
        return cartItems;
    }
}
