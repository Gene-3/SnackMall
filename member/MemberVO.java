package member;

public class MemberVO {
    private String id;
    private String pw;
    private String name;
    private String cardCompany;
    private String cardNumber;

    public MemberVO(String id, String pw, String name, String cardCompany, String cardNumber) {
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.cardCompany = cardCompany;
        this.cardNumber = cardNumber;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }
    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCardCompany() {
        return cardCompany;
    }
    public void setCardCompany(String cardCompany) {
        this.cardCompany = cardCompany;
    }

    public String getCardNumber() {
        return cardNumber;
    }
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public String toString() {
        return "MemberVO{" +
               "id='" + id + '\'' +
               ", pw='" + pw + '\'' +
               ", name='" + name + '\'' +
               ", cardCompany='" + cardCompany + '\'' +
               ", cardNumber='" + cardNumber + '\'' +
               '}';
    }
}
