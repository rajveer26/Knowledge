package com.knowledge.microservice.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;

@Service("jwtConfig")
public class JwtConfig {

    private final Environment env;
    private final PublicKey publicKey;

    @Autowired
    public JwtConfig(Environment env) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        this.env = env;
        this.publicKey = generateJwtKeyDecryption();
    }

    public PublicKey generateJwtKeyDecryption() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        // Load resource using ClassPathResource
        ClassPathResource resource = new ClassPathResource(
            Objects.requireNonNull(env.getProperty("spring.rsocket.server.ssl.certificate"))
                .concat("public_key.pem")
        );

        // Read file contents as InputStream
        try (InputStream inputStream = resource.getInputStream()) {
            String publicKeyPEM = new String(inputStream.readAllBytes())
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

            // Decode the key, prepare specification, and generate PublicKey
            byte[] decodedKey = Base64.getDecoder().decode(publicKeyPEM);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            return keyFactory.generatePublic(keySpec);
        }
    }

    public PrivateKey generateJwtKeyEncryption() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        // Load resource using ClassPathResource
        ClassPathResource resource = new ClassPathResource(
                Objects.requireNonNull(env.getProperty("spring.rsocket.server.ssl.certificate"))
                        .concat("private_key.pem")
        );

        // Read file contents as InputStream
        try (InputStream inputStream = resource.getInputStream()) {
            String privateKeyPEM = new String(inputStream.readAllBytes())
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");

            // Decode the key, prepare specification, and generate PrivateKey
            byte[] keyBytes = Base64.getDecoder().decode(privateKeyPEM);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            return keyFactory.generatePrivate(keySpec);
        }
    }

}