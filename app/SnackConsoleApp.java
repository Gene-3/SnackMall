package app;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import cart.CartService;
import member.MemberDAO;
import member.MemberService;
import member.MemberVO;
import member.TextFileHashMapMemberDAO;
import order.OrderService;
import order.OrderVO;
import order.OrderDAO;
import order.TextFileHashMapOrderDAO;
import snack.SnackDAO;
import snack.SnackService;
import snack.SnackVO;
import snack.TextFileHashMapSnackDAO;

public class SnackConsoleApp {

    private Scanner sc = new Scanner(System.in);

    private MemberService memberService;
    private SnackService snackService;
    private CartService cartService;
    private OrderService orderService;

    private MemberVO loginMember = null;

    private final String ADMIN_ID = "admin";
    private final String ADMIN_PW = "1234";
    private final String ADMIN_NAME = "관리자";
    private final String ADMIN_CARD_COMPANY = "none";
    private final String ADMIN_CARD_NUMBER = "0000000000000000";

    public SnackConsoleApp(MemberService memberService, SnackService snackService,
                           CartService cartService, OrderService orderService) {
        this.memberService = memberService;
        this.snackService = snackService;
        this.cartService = cartService;
        this.orderService = orderService;
    }

    public void run() {
        System.out.println("=== 간식 쇼핑몰에 오신 것을 환영합니다! ===");

        if (snackService.listSnacks().equals("등록된 간식이 없습니다.")) {
            initDefaultSnacks();
        }

        while (true) {
            if (loginMember == null) {
                showGuestMenu();
            } else if (ADMIN_ID.equals(loginMember.getId())) {
                showAdminMenu();
            } else {
                showUserMenu();
            }
        }
    }

    private void initDefaultSnacks() {
        System.out.println("기본 간식 데이터를 초기화합니다.");
        if (!snackService.listSnacks().equals("등록된 간식이 없습니다.")) return;
        snackService.registSnack("키트캣", "네슬레", "일본", 2500, 100, "초콜릿");
        snackService.registSnack("허쉬 초콜릿", "허쉬", "미국", 3000, 80, "초콜릿");
        snackService.registSnack("마루스", "페레로", "이탈리아", 3500, 50, "초콜릿");
        snackService.registSnack("오레오", "몬델리즈", "미국", 2000, 120, "과자");
        snackService.registSnack("프링글스", "프링글스", "미국", 3000, 70, "과자");
        snackService.registSnack("토블론", "페레로", "이탈리아", 3200, 90, "초콜릿");
    }

    // 비회원 메뉴
    private void showGuestMenu() {
        System.out.println("\n--- 비회원 메뉴 ---");
        System.out.println("1. 로그인");
        System.out.println("2. 회원가입");
        System.out.println("0. 종료");
        Integer choice = inputInt("선택> ", 0, 2);
        if (choice == null) {
            System.out.println("숫자를 입력하세요");
            return;
        }
        switch (choice) {
            case 1 -> login();
            case 2 -> signUp();
            case 0 -> exitProgram();
            default -> System.out.println("잘못된 선택입니다.");
        }
    }

    // 로그인 처리 메서드
    private void login() {
        System.out.println("\n--- 로그인 ---");
        String id = inputNonEmptyString("ID 입력 (b: 뒤로가기): ");
        if (id == null) return;
        String pw = inputNonEmptyString("비밀번호 입력 (b: 뒤로가기): ");
        if (pw == null) return;
        // 관리자 로그인 처리
        if (ADMIN_ID.equals(id) && ADMIN_PW.equals(pw)) {
            loginMember = new MemberVO(ADMIN_ID, ADMIN_PW, ADMIN_NAME, ADMIN_CARD_COMPANY, ADMIN_CARD_NUMBER);
            System.out.printf("관리자 로그인 성공! 환영합니다, %s님.\n", loginMember.getName());
            return;
        }
        // 일반 회원 로그인 처리
        MemberVO member = memberService.login(id, pw);
        if (member != null) {
            loginMember = member;
            System.out.printf("로그인 성공! 환영합니다, %s님.\n", loginMember.getName());
        } else {
            System.out.println("로그인 실패! 아이디 또는 비밀번호를 확인하세요.");
        }
    }

    // 회원가입 처리
    private void signUp() {
        System.out.println("\n--- 회원 가입 ---");
        String id = inputNonEmptyString("아이디 입력 (b: 뒤로가기): ");
        if (id == null) return;
        if (ADMIN_ID.equalsIgnoreCase(id)) {
            System.out.println("관리자 ID는 가입할 수 없습니다.");
            return;
        }
        String pw = inputNonEmptyString("비밀번호 입력 (b: 뒤로가기): ");
        if (pw == null) return;
        String name = inputNonEmptyString("이름 입력 (b: 뒤로가기): ");
        if (name == null) return;
        String cardCompany = inputNonEmptyString("카드 회사명 입력 (b: 뒤로가기): ");
        if (cardCompany == null) return;
        String cardNumber = inputNonEmptyString("카드 번호 입력 (16자리, b: 뒤로가기): ");
        if (cardNumber == null) return;
        if (cardNumber.length() != 16) {
            System.out.println("카드 번호는 16자리여야 합니다.");
            return;
        }
        String result = memberService.registMember(id, pw, name, cardCompany, cardNumber);
        System.out.println(result);
    }

    // 관리자 메뉴
    private void showAdminMenu() {
        System.out.printf("\n--- 관리자 메뉴 (로그인: %s) ---\n", loginMember.getName());
        System.out.println("1. 간식 등록");
        System.out.println("2. 간식 목록 보기");
        System.out.println("3. 회원 목록 보기");
        System.out.println("4. 회원 비밀번호 수정");
        System.out.println("5. 회원 삭제");
        System.out.println("6. 간식 재고 수정");
        System.out.println("7. 간식 삭제");
        System.out.println("8. 간식 판매 중지");
        System.out.println("9. 전체 주문 내역 보기");  // 추가
        System.out.println("10. 로그아웃");
        System.out.println("0. 종료");

        Integer choice = inputInt("선택> ", 0, 10);
        if (choice == null) return;
        switch (choice) {
            case 1 -> addSnack();
            case 2 -> System.out.println(snackService.listSnacks());
            case 3 -> showMemberList();
            case 4 -> editMemberPassword();
            case 5 -> deleteMember();
            case 6 -> updateSnackStock();
            case 7 -> deleteSnack();
            case 8 -> disableSnack();
            case 9 -> showAllOrders();  // 새 기능
            case 10 -> logout();
            case 0 -> exitProgram();
            default -> System.out.println("잘못된 선택입니다.");
        }
    }

    // 관리자 전체 주문 내역 출력
    private void showAllOrders() {
        System.out.println("\n--- 전체 주문 내역 ---");
        List<OrderVO> orders = orderService.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("주문 내역이 없습니다.");
            return;
        }
        for (OrderVO order : orders) {
            printOrderDetails(order);
        }
    }


    private void addSnack() {
        System.out.println("\n--- 간식 등록 ---");
        String name = inputNonEmptyString("간식 이름 (b: 뒤로가기): ");
        if (name == null) return;
        String company = inputNonEmptyString("회사명 (b: 뒤로가기): ");
        if (company == null) return;
        String country = inputNonEmptyString("원산지 (b: 뒤로가기): ");
        if (country == null) return;
        Integer price = inputInt("가격 (0 이상, b: 뒤로가기): ", 0, Integer.MAX_VALUE);
        if (price == null) return;
        Integer stock = inputInt("재고 (0 이상, b: 뒤로가기): ", 0, Integer.MAX_VALUE);
        if (stock == null) return;
        String category = inputNonEmptyString("분류 (b: 뒤로가기): ");
        if (category == null) return;
        System.out.println(snackService.registSnack(name, company, country, price, stock, category));
    }

    private void showMemberList() {
        System.out.println("\n--- 회원 목록 ---");
        memberService.listMembers().forEach(m -> System.out.printf("ID: %s, 이름: %s\n", m.getId(), m.getName()));
    }

    private void editMemberPassword() {
        System.out.println("\n--- 회원 비밀번호 수정 ---");
        String id = inputNonEmptyString("비밀번호 변경할 회원 ID (b: 뒤로가기): ");
        if (id == null) return;
        MemberVO member = memberService.getMember(id);
        if (member == null) {
            System.out.println("해당 ID의 회원이 없습니다.");
            return;
        }
        String newPw = inputNonEmptyString("새 비밀번호 입력 (b: 뒤로가기): ");
        if (newPw == null) return;
        member.setPw(newPw);
        memberService.updateMember(member);
        System.out.println("비밀번호가 변경되었습니다.");
    }

    private void deleteMember() {
        System.out.println("\n--- 회원 삭제 ---");
        String id = inputNonEmptyString("삭제할 회원 ID (b: 뒤로가기): ");
        if (id == null) return;
        if (ADMIN_ID.equals(id)) {
            System.out.println("운영자 계정은 삭제할 수 없습니다.");
            return;
        }
        boolean success = memberService.deleteMember(id);
        System.out.println(success ? "회원이 삭제되었습니다." : "삭제 실패 또는 회원이 없습니다.");
    }

    private void updateSnackStock() {
        System.out.println("\n--- 간식 재고 수정 ---");
        Integer no = inputInt("수정할 간식 번호 (b: 뒤로가기): ", 1, Integer.MAX_VALUE);
        if (no == null) return;
        if (!snackService.existsSnack(no)) {
            System.out.println("해당 간식이 없습니다.");
            return;
        }
        Integer stock = inputInt("새 재고 입력 (0 이상, b: 뒤로가기): ", 0, Integer.MAX_VALUE);
        if (stock == null) return;
        snackService.updateSnackStock(no, stock);
        System.out.println("재고가 수정되었습니다.");
    }

    private void deleteSnack() {
        System.out.println("\n--- 간식 삭제 ---");
        Integer no = inputInt("삭제할 간식 번호 (b: 뒤로가기): ", 1, Integer.MAX_VALUE);
        if (no == null) return;
        boolean success = snackService.deleteSnack(no);
        System.out.println(success ? "간식이 삭제되었습니다." : "삭제 실패 또는 간식이 없습니다.");
    }

    private void disableSnack() {
        System.out.println("\n--- 간식 판매 중지 ---");
        Integer no = inputInt("판매 중지할 간식 번호 (b: 뒤로가기): ", 1, Integer.MAX_VALUE);
        if (no == null) return;
        SnackVO snack = snackService.getSnackVO(no);
        if (snack == null) {
            System.out.println("해당 간식이 없습니다.");
            return;
        }
        snack.setCategory("판매 중지");
        snackService.updateSnack(snack);
        System.out.println("간식이 판매 중지 처리되었습니다.");
    }

    private void logout() {
        loginMember = null;
        System.out.println("로그아웃 되었습니다.");
    }

    // 사용자 메뉴에 '내 주문 내역 보기' 추가
    private void showUserMenu() {
        System.out.printf("\n--- 사용자 메뉴 (로그인: %s) ---\n", loginMember.getName());
        System.out.println("1. 간식 목록 보기");
        System.out.println("2. 장바구니에 간식 추가");
        System.out.println("3. 장바구니 목록 보기");
        System.out.println("4. 주문하기");
        System.out.println("5. 내 주문 내역 보기"); // 추가
        System.out.println("6. 로그아웃");
        System.out.println("0. 종료");

        Integer choice = inputInt("선택> ", 0, 6);
        if (choice == null) return;

        switch (choice) {
            case 1 -> System.out.println(snackService.listSnacks());
            case 2 -> addToCart();
            case 3 -> showCart();
            case 4 -> orderSnack();
            case 5 -> showMyOrders();
            case 6 -> logout();
            case 0 -> exitProgram();
            default -> System.out.println("잘못된 선택입니다.");
        }
    }

    // 주문 내역 보기: 사용자별 주문 리스트(OrderVO) 출력
    private void showMyOrders() {
        System.out.println("\n--- 내 주문 내역 ---");
        List<OrderVO> orders = orderService.getOrdersByUser(loginMember.getId());
        if (orders.isEmpty()) {
            System.out.println("주문 내역이 없습니다.");
            return;
        }
        for (OrderVO order : orders) {
            printOrderDetails(order);
        }
    }

    private void printOrderDetails(OrderVO order) {
        System.out.println("주문 번호: " + order.getOrderId());
        System.out.println("주문자 ID: " + order.getUserId());
        System.out.println("주문 내역:");
        Map<Integer, Integer> cart = order.getCart();
        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
            int snackNo = entry.getKey();
            int quantity = entry.getValue();
            SnackVO snack = snackService.getSnackVO(snackNo);
            String snackName = (snack != null) ? snack.getName() : "알 수 없는 간식";
            System.out.printf("  - %s: %d개\n", snackName, quantity);
        }
        System.out.println("-------------------------");
    }

    private void addToCart() {
        System.out.println("\n--- 장바구니에 간식 추가 ---");
        Integer no = inputInt("추가할 간식 번호 (b: 뒤로가기): ", 1, Integer.MAX_VALUE);
        if (no == null) return;
        if (!snackService.existsSnack(no)) {
            System.out.println("해당 간식이 없습니다.");
            return;
        }
        Integer qty = inputInt("수량 (1 이상, b: 뒤로가기): ", 1, Integer.MAX_VALUE);
        if (qty == null) return;
        cartService.addToCart(loginMember.getId(), no, qty);
        System.out.println("장바구니에 추가되었습니다.");
    }

    private void showCart() {
        System.out.println("\n--- 장바구니 목록 ---");
        var cartItems = cartService.getUserCart(loginMember.getId());
        if (cartItems.isEmpty()) {
            System.out.println("장바구니가 비어있습니다.");
        } else {
            cartItems.forEach(System.out::println);
        }
    }

    private void orderSnack() {
        System.out.println("\n--- 주문하기 ---");
        var cartItems = cartService.getUserCart(loginMember.getId());
        if (cartItems.isEmpty()) {
            System.out.println("장바구니가 비어있습니다.");
            return;
        }
        String result = orderService.orderSnack(loginMember.getId());
        System.out.println(result);
    }

    private Integer inputInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if ("b".equalsIgnoreCase(input)) return null;
            try {
                int val = Integer.parseInt(input);
                if (val < min || val > max) {
                    System.out.printf("입력값은 %d 이상 %d 이하만 가능합니다.\n", min, max);
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력하거나 'b'를 입력하세요.");
            }
        }
    }

    private String inputNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if ("b".equalsIgnoreCase(input)) return null;
            if (input.isEmpty()) {
                System.out.println("빈 문자열은 입력할 수 없습니다. 다시 입력해주세요.");
                continue;
            }
            return input;
        }
    }

    private void exitProgram() {
        System.out.println("프로그램을 종료합니다.");
        sc.close();
        System.exit(0);
    }

    public static void main(String[] args) {
        MemberDAO memberDAO = new TextFileHashMapMemberDAO();
        SnackDAO snackDAO = new TextFileHashMapSnackDAO();
        OrderDAO orderDAO = new TextFileHashMapOrderDAO();

        MemberService memberService = new MemberService(memberDAO);
        SnackService snackService = new SnackService(snackDAO);
        CartService cartService = new CartService(snackDAO);
        OrderService orderService = new OrderService(orderDAO, cartService, snackDAO);

        SnackConsoleApp app = new SnackConsoleApp(memberService, snackService, cartService, orderService);
        app.run();
    }
}
