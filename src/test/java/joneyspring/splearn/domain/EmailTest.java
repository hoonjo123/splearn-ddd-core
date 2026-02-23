package joneyspring.splearn.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class EmailTest {
    @Test
    void equality(){
        var email1 = new Email("joney@gmail.com");
        var email2 = new Email("joney@gmail.com");

        assertThat(email1).isEqualTo(email2);
    }
}