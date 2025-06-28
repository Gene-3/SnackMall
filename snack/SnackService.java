// 예시: 간식 번호로 SnackVO 조회 메서드가 있어야 아래에서 정상 작동합니다.

package snack;

import java.util.List;

public class SnackService {
    private SnackDAO snackDAO;

    public SnackService(SnackDAO snackDAO) {
        this.snackDAO = snackDAO;
    }

    public String listSnacks() {
        List<SnackVO> list = snackDAO.selectAllSnacks();
        if (list.isEmpty()) {
            return "등록된 간식이 없습니다.";
        }
        StringBuilder sb = new StringBuilder();
        for (SnackVO snack : list) {
            sb.append(snack).append("\n");
        }
        return sb.toString();
    }

    public String registSnack(String name, String company, String country, int price, int stock, String category) {
        SnackVO snack = new SnackVO(name, company, country, price, stock, category);
        snackDAO.insertSnack(snack);
        return "간식 등록 완료: " + name;
    }

    public boolean existsSnack(int no) {
        return snackDAO.selectSnack(no) != null;
    }

    public void updateSnackStock(int no, int stock) {
        SnackVO snack = snackDAO.selectSnack(no);
        if (snack != null) {
            snack.setStock(stock);
            snackDAO.updateSnack(snack);
        }
    }

    public boolean deleteSnack(int no) {
        return snackDAO.deleteSnack(no);
    }

    public SnackVO getSnackVO(int no) {
        return snackDAO.selectSnack(no);
    }

    public void updateSnack(SnackVO snack) {
        snackDAO.updateSnack(snack);
    }
}
