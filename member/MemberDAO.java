package member;

import java.util.List;

public interface MemberDAO {

    // 회원 추가
    void insertMember(MemberVO member);

    // 회원 아이디로 회원 조회
    MemberVO selectMember(String id);

    // 회원 정보 수정
    void updateMember(MemberVO member);

    // 회원 삭제
    boolean deleteMember(String id);

    // 모든 회원 목록 조회
    List<MemberVO> selectAllMembers();
}
