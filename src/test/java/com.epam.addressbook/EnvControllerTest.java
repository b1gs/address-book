package com.epam.addressbook;

import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class EnvControllerTest {
    @Test
    public void getEnv_whenControllerIsCreated_returnsEnvVars() {
        EnvController controller = new EnvController("9876", "8G", "15", "cf.instance.address");

        Map<String, String> env = controller.getEnv();

        assertThat(env.get("PORT")).isEqualTo("9876");
        assertThat(env.get("MEMORY_LIMIT")).isEqualTo("8G");
        assertThat(env.get("CF_INSTANCE_INDEX")).isEqualTo("15");
        assertThat(env.get("CF_INSTANCE_ADDR")).isEqualTo("cf.instance.address");
    }
}