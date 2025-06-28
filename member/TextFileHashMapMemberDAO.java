package member;

import java.io.*;
import java.util.*;

public class TextFileHashMapMemberDAO implements MemberDAO {
    private Map<String, MemberVO> memberMap = new HashMap<>();
    private final String DATA_FILE = "./data/member.txt";

    public TextFileHashMapMemberDAO() {
        loadMembers();
    }

    private void loadMembers() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                	// 파일에서 읽은 각 줄을 콤마(,)로 분리한 후,
                	// MemberVO 객체 생성 (순서: 아이디, 비밀번호, 이름, 카드 회사, 카드 번호)
                    MemberVO member = new MemberVO(parts[0], parts[1], parts[2], parts[3], parts[4]);
                    memberMap.put(member.getId(), member);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveMembers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (MemberVO member : memberMap.values()) {
                String line = String.join(",", member.getId(), member.getPw(), member.getName(),
                        member.getCardCompany(), member.getCardNumber());
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertMember(MemberVO member) {
        memberMap.put(member.getId(), member);
        saveMembers();
    }

    @Override
    public MemberVO selectMember(String id) {
        return memberMap.get(id);
    }

    @Override
    public void updateMember(MemberVO member) {
        memberMap.put(member.getId(), member);
        saveMembers();
    }

    @Override
    public boolean deleteMember(String id) {
        if (memberMap.remove(id) != null) {
            saveMembers();
            return true;
        }
        return false;
    }

    @Override
    public List<MemberVO> selectAllMembers() {
        return new ArrayList<>(memberMap.values());
    }
}
