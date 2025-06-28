package snack;

import java.util.List;

// 간식 데이터 접근을 위한 DAO 인터페이스
public interface SnackDAO {
    // 간식 등록
    void insertSnack(SnackVO snack);

    // 모든 간식 조회
    List<SnackVO> selectAllSnacks();

    // 번호로 간식 조회
    SnackVO selectSnack(int no);

    // 간식 정보 수정
    void updateSnack(SnackVO snack);

    // 간식 삭제 (성공 시 true 반환)
    boolean deleteSnack(int no);

    // 간식 재고 조회
    int getStock(int snackNo);

    // 간식 재고 차감
    void reduceStock(int snackNo, int quantity);

    // 간식 번호 자동 생성
    int getNextSnackNo();
}
