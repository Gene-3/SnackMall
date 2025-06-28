package snack;

// 간식 정보 저장용 VO 클래스
public class SnackVO {
    private int snackNo;        // 간식 번호 (고유 식별자)
    private String name;        // 간식 이름
    private String company;     // 제조사
    private String country;     // 제조사 국가
    private int price;          // 가격
    private int stock;          // 재고 수량
    private String category;    // 분류

    // 원산지 제외 생성자 (필요시 사용 가능)
    public SnackVO(String name, String company, String country, int price, int stock, String category) {
        this.name = name;
        this.company = company;
        this.country = country;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    // 전체 필드 초기화 생성자
    public SnackVO(int snackNo, String name, String company, String country, int price, int stock, String category) {
        this.snackNo = snackNo;
        this.name = name;
        this.company = company;
        this.country = country;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    // getter / setter 메서드
    public int getSnackNo() {
        return snackNo;
    }
    public void setSnackNo(int snackNo) {
        this.snackNo = snackNo;
    }
    public String getName() {
        return name;
    }
    public String getCompany() {
        return company;
    }
    public String getCountry() {
        return country;
    }
    public int getPrice() {
        return price;
    }
    public int getStock() {
        return stock;
    }
    public String getCategory() {
        return category;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    // 객체 정보 출력 시 보기 좋은 문자열 반환 (toString 재정의)
    @Override
    public String toString() {
        return String.format("번호: %d | 이름: %s | 회사: %s | 원산지: %s | 가격: %d | 재고: %d | 분류: %s",
            snackNo, name, company, country, price, stock, category);
    }
}
