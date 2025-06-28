package member;

import java.util.List;

public class MemberService {
    private MemberDAO memberDAO;

    public MemberService(MemberDAO dao) {
        this.memberDAO = dao;
    }

    public boolean isValidCardNumber(String cardNumber) {
        return cardNumber != null && cardNumber.length() == 16 && cardNumber.matches("\\d{16}");
    }

    public String registMember(String id, String pw, String name, String cardCompany, String cardNumber) {
        if (memberDAO.selectMember(id) != null) {
            return "이미 존재하는 아이디입니다.";
        }
        if (!isValidCardNumber(cardNumber)) {
            return "올바른 카드번호를 입력하세요! (16자리 숫자)";
        }
        MemberVO member = new MemberVO(id, pw, name, cardCompany, cardNumber);
        memberDAO.insertMember(member);
        return "회원가입 성공!";
    }

    public MemberVO login(String id, String pw) {
        MemberVO member = memberDAO.selectMember(id);
        if (member != null && member.getPw().equals(pw)) {
            return member;
        }
        return null;
    }

    public List<MemberVO> listMembers() {
        return memberDAO.selectAllMembers();
    }

    public MemberVO getMember(String id) {
        return memberDAO.selectMember(id);
    }

    public void updateMember(MemberVO member) {
        memberDAO.updateMember(member);
    }

    public boolean deleteMember(String id) {
        return memberDAO.deleteMember(id);
    }
}
