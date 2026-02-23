package joneyspring.splearn.domain;

import lombok.Getter;
import lombok.ToString;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.state;

@Getter
@ToString
public class Member {

    private Email email;

    private String nickName;

    private String passwordHash;

    private MemberStatus status;

//    기본생성자
//    private으로 선언함으로써 멤버 생성 시, create 메서드를 사용하도록 강제할 수 있다.
    private Member() {

    }

//    생성자를 대체하는 정적 팩토리 매서드
//    builder를 사용해도 되긴 하지만, 추후 entity가 추가되거나 로직이 복잡해질 경우, 파라미터의 순서가
//    바뀌거나, 누락될 수 있기 때문에, 정적 팩토리 매서드를 사용하는 것을 권장한다.
    public static Member create(MemberCreateRequest memberCreateRequest, PasswordEncoder passwordEncoder) {
        Member member = new Member();

        String email = memberCreateRequest.email();

        member.email = new Email(memberCreateRequest.email());
        member.nickName = requireNonNull(memberCreateRequest.nickName());
        member.passwordHash = requireNonNull(passwordEncoder.encode(memberCreateRequest.passwordHash()));

        member.status = MemberStatus.PENDING;

        return member;
    }

    public void activate() {
        state(status == MemberStatus.PENDING, "회원 상태가 PENDING이 아닙니다.");

        this.status = MemberStatus.ACTIVE;
    }

    public void deactivate() {
        state(status == MemberStatus.ACTIVE, "회원 상태가 ACTIVE가 아닙니다.");

        this.status = MemberStatus.DEACTIVATED;
    }

    public boolean verifypassword(String hashedPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(hashedPassword, this.passwordHash);
    }

    public void changeNickname(String nickName) {
        this.nickName = requireNonNull(nickName);
    }

    public void changePassword(String password, PasswordEncoder passwordEncoder) {
        this.passwordHash = passwordEncoder.encode(requireNonNull(password));
    }

    public boolean isActive() {
        return this.status == MemberStatus.ACTIVE;
    }
}
