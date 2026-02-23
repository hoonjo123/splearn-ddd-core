package joneyspring.splearn.domain;

import java.util.regex.Pattern;

public record Email(
        String address
) {
    private final static Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    public Email {
        if (!EMAIL_PATTERN.matcher(address).matches()) {
            throw new IllegalArgumentException("유효한 이메일 형식이 아닙니다." + address);
        }
    }
}
