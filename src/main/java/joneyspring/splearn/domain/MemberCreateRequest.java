package joneyspring.splearn.domain;

public record MemberCreateRequest(
        String email,
        String nickName,
        String passwordHash
) {

}
