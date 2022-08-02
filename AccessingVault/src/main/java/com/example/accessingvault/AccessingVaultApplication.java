package com.example.accessingvault;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.vault.core.VaultKeyValueOperationsSupport;
import org.springframework.vault.core.VaultSysOperations;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.core.VaultTransitOperations;
import org.springframework.vault.support.VaultMount;
import org.springframework.vault.support.VaultResponse;

@SpringBootApplication
public class AccessingVaultApplication implements CommandLineRunner {


    @Autowired
    private VaultTemplate vaultTemplate;

    public static void main(String[] args) {
        SpringApplication.run(AccessingVaultApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        VaultResponse response = vaultTemplate
                .opsForKeyValue("secret", VaultKeyValueOperationsSupport.KeyValueBackend.KV_2).get("github");
        System.out.println("Value of github.oauth2.key");
        System.out.println("-------------------------------");
        System.out.println(response.getData().get("github.oauth2.key"));
        System.out.println("-------------------------------");
        System.out.println();

        VaultTransitOperations transitOperations = vaultTemplate.opsForTransit();

        VaultSysOperations sysOperations = vaultTemplate.opsForSys();

        if (!sysOperations.getMounts().containsKey("transit/")) {
            sysOperations.mount("transit", VaultMount.create("transit"));
            transitOperations.createKey("foo-key");
        }

        String ciphertext = transitOperations.encrypt("foo-key", "Secure message");

        System.out.println("Encrypted value");
        System.out.println("-------------------------------");
        System.out.println(ciphertext);
        System.out.println("-------------------------------");
        System.out.println();

        String plaintext = transitOperations.decrypt("foo-key", ciphertext);

        System.out.println("Decrypted value");
        System.out.println("-------------------------------");
        System.out.println(plaintext);
        System.out.println("-------------------------------");
        System.out.println();
    }
}
