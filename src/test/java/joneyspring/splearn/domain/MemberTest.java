package joneyspring.splearn.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class MemberTest {

    Member member;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        this.passwordEncoder = new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };
        member = Member.create(new MemberCreateRequest("joney@splearn.app", "joney", "hashedPassword"), passwordEncoder);
    }


    @Test
    void createMemter(){
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void activate(){
        member.activate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    void activateFail(){
        member.activate();

        assertThatThrownBy(() ->
                member.activate()
        ).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void deactivate(){

        member.activate();
        member.deactivate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    }

    @Test
    void deActivateFail(){

        assertThatThrownBy(member::deactivate).isInstanceOf(IllegalStateException.class);

        member.activate();
        member.deactivate();

        assertThatThrownBy(member::deactivate).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void verifyPassword(){
        assertThat(member.verifypassword("hashedPassword", passwordEncoder)).isTrue();
        assertThat(member.verifypassword("wrongPassword", passwordEncoder)).isFalse();
    }

    @Test
    void changeNickname(){
         assertThat(member.getNickName()).isEqualTo("joney");
         member.changeNickname("joney2");

         assertThat(member.getNickName()).isEqualTo("joney2");

    }

    @Test
    void changePassword(){
        member.changePassword("newPassword", passwordEncoder);
        assertThat(member.verifypassword("newPassword", passwordEncoder)).isTrue();
    }

    @Test
    void isActive(){
        assertThat(member.isActive()).isFalse();

        member.activate();

        assertThat(member.isActive()).isTrue();
        member.deactivate();
        assertThat(member.isActive()).isFalse();
    }

    @Test
    void invalidEmail(){
        assertThatThrownBy(() ->
            Member.create(new MemberCreateRequest("invalid email", "joney", "hashedPassword"), passwordEncoder)
        ).isInstanceOf(IllegalArgumentException.class);

        Member.create(new MemberCreateRequest("invalidemail@gmail.com", "joney", "hashedPassword"), passwordEncoder);
    }
}