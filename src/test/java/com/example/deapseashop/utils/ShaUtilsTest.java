package com.example.deapseashop.utils;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class ShaUtilsTest {

    @Test
    void IsEncryptSame() {
        String hello1 = ShaUtils.encryptSHA256("dkssudgktpdy");
        String hello2 = ShaUtils.encryptSHA256("dkssudgktpdy");

        assertThat(hello1).isEqualTo(hello2);
    }
}
