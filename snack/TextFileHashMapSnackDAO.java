package snack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 간식 정보를 파일에 저장하고 불러오는 DAO 구현체
public class TextFileHashMapSnackDAO implements SnackDAO {
    // 간식 번호와 SnackVO 객체를 저장하는 Map
    private Map<Integer, SnackVO> snack = new HashMap<>();
    // 다음 간식 번호를 관리하는 변수
    private int nextNo = 1;
    // 데이터 파일 경로
    private final String DATA_FILE = "./data/snack.txt";

    // 생성자에서 기존 데이터 파일 로딩 또는 기본 데이터 삽입
    public TextFileHashMapSnackDAO() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            insertDefaultSnacks();  // 기본 간식 데이터 삽입
            saveSnacks();           // 파일로 저장
        } else {
            loadSnacks();           // 파일에서 데이터 로드
        }
    }

    // 기본 간식 데이터 삽입
    private void insertDefaultSnacks() {
        snack.put(nextNo, new SnackVO(nextNo++, "KitKat", "Nestle", "영국", 1500, 50, "초콜릿"));
        snack.put(nextNo, new SnackVO(nextNo++, "Toblerone", "Mondelez", "스위스", 3000, 40, "초콜릿"));
        snack.put(nextNo, new SnackVO(nextNo++, "Haribo Goldbears", "Haribo", "독일", 2500, 60, "젤리"));
        snack.put(nextNo, new SnackVO(nextNo++, "Lindt Lindor", "Lindt", "스위스", 3500, 30, "초콜릿"));
        snack.put(nextNo, new SnackVO(nextNo++, "Kinder Bueno", "Ferrero", "이탈리아", 2000, 45, "초콜릿"));
    }

    // 데이터 파일에서 간식 목록을 읽어와 Map에 저장
    private void loadSnacks() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int maxNo = 0;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7) {
                    int no = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    String company = parts[2];
                    String country = parts[3];
                    int price = Integer.parseInt(parts[4]);
                    int stock = Integer.parseInt(parts[5]);
                    String category = parts[6];
                    SnackVO snackVO = new SnackVO(no, name, company, country, price, stock, category);
                    snack.put(no, snackVO);
                    if (no > maxNo) maxNo = no;
                }
            }
            nextNo = maxNo + 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 간식 정보를 파일에 저장
    private void saveSnacks() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (SnackVO snackVO : snack.values()) {
                String line = snackVO.getSnackNo() + "," +
                              snackVO.getName() + "," +
                              snackVO.getCompany() + "," +
                              snackVO.getCountry() + "," +
                              snackVO.getPrice() + "," +
                              snackVO.getStock() + "," +
                              snackVO.getCategory();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertSnack(SnackVO snackVO) {
        snackVO.setSnackNo(nextNo++);
        snack.put(snackVO.getSnackNo(), snackVO);
        saveSnacks();
    }

    @Override
    public List<SnackVO> selectAllSnacks() {
        return new ArrayList<>(snack.values());
    }

    @Override
    public SnackVO selectSnack(int snackNo) {
        return snack.get(snackNo);
    }

    @Override
    public void updateSnack(SnackVO snackVO) {
        snack.put(snackVO.getSnackNo(), snackVO);
        saveSnacks();
    }

    @Override
    public boolean deleteSnack(int snackNo) {
        if (snack.remove(snackNo) != null) {
            saveSnacks();
            return true;
        }
        return false;
    }

    @Override
    public int getStock(int snackNo) {
        SnackVO snackVO = snack.get(snackNo);
        return (snackVO != null) ? snackVO.getStock() : 0;
    }

    @Override
    public void reduceStock(int snackNo, int quantity) {
        SnackVO snackVO = snack.get(snackNo);
        if (snackVO != null) {
            int newStock = snackVO.getStock() - quantity;
            snackVO.setStock(Math.max(newStock, 0));
            saveSnacks();
        }
    }

    @Override
    public int getNextSnackNo() {
        return nextNo;
    }
}
