package order;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TextFileHashMapOrderDAO implements OrderDAO {
    private static final String FILE_NAME = "./data/order.txt";
    private int orderSeq = 111;

    public TextFileHashMapOrderDAO() {
        loadOrderSeq();
    }

    private void loadOrderSeq() {
        int maxOrderNo = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    OrderVO order = OrderVO.fromString(line);
                    if (order.getOrderId() > maxOrderNo) maxOrderNo = order.getOrderId();
                } catch (Exception e) {
                    System.err.println("주문 데이터 파싱 실패: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("[주문 정보 DB 로딩] " + FILE_NAME + "이 없습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        orderSeq = maxOrderNo + 1;
    }

    @Override
    public synchronized void saveOrder(String userId, Map<Integer, Integer> cart) {
        OrderVO order = new OrderVO(orderSeq++, userId, cart);
        saveToFile(order);
    }

    private void saveToFile(OrderVO order) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bw.write(order.toString());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getOrdersByUser(String userId) {
        List<String> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    OrderVO order = OrderVO.fromString(line);
                    if (order.getUserId().equals(userId)) {
                        result.add(line);
                    }
                } catch (Exception e) {
                    System.err.println("주문 데이터 파싱 실패: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<String> getAllOrders() {
        List<String> allOrders = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                allOrders.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allOrders;
    }
}
