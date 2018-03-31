package ru.disdev;

import org.apache.commons.io.IOUtils;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

public class Crypt {

    private static final String KEY = "da8467vL";

    public static void main(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        DesService service = new DesService(KEY);
        while (true) {
            System.out.print("enc|dec {source} {target} > ");
            String s = scanner.nextLine();
            String[] split = s.split(" ");
            String cmd = split[0];

            if (cmd.equalsIgnoreCase("exit")) {
                return;
            }

            if (split.length != 3) {
                continue;
            }

            String arg = split[1];
            File source = new File(arg);
            if (!source.exists()) {
                System.out.println("File " + arg + "not exists");
                continue;
            }
            boolean isEncrypt = cmd.equalsIgnoreCase("enc");
            Cipher cipher = isEncrypt ? service.getEnc() : service.getDec();
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                inputStream = new FileInputStream(source);
                outputStream = new FileOutputStream(split[2]);
                if (isEncrypt) {
                    outputStream = new CipherOutputStream(outputStream, cipher);
                } else {
                    inputStream = new CipherInputStream(inputStream, cipher);
                }
                IOUtils.copy(inputStream, outputStream);
                System.out.println("Success!");
            } finally {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            }

        }
    }

    public static class DesService {

        private final Cipher enc;
        private final Cipher dec;

        public DesService(String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
            DESKeySpec spec = new DESKeySpec(key.getBytes());
            SecretKey secret = SecretKeyFactory.getInstance("DES")
                    .generateSecret(spec);
            enc = Cipher.getInstance("DES");
            dec = Cipher.getInstance("DES");
            enc.init(Cipher.ENCRYPT_MODE, secret);
            dec.init(Cipher.DECRYPT_MODE, secret);
        }

        public byte[] encode(byte[] source) throws BadPaddingException, IllegalBlockSizeException {
            return enc.doFinal(source);
        }

        public byte[] decode(byte[] source) throws BadPaddingException, IllegalBlockSizeException {
            return dec.doFinal(source);
        }

        public Cipher getDec() {
            return dec;
        }

        public Cipher getEnc() {
            return enc;
        }
    }
}
