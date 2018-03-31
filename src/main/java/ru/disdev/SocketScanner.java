package ru.disdev;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.stream.IntStream;

public class SocketScanner {

    public static void main(String... args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter {host} {minport} {maxport} > ");
            String input = scanner.nextLine();
            String[] split = input.split(" ");
            if (split.length == 0) {
                continue;
            }
            String host;
            int minPort;
            int maxPort;
            host = split[0];
            if (host.equalsIgnoreCase("exit")) {
                break;
            }
            try {
                minPort = Integer.parseInt(split[1]);
                maxPort = Integer.parseInt(split[2]);
            } catch (Exception ex) {
                continue;
            }

            IntStream.range(minPort, maxPort + 1)
                    .parallel()
                    .forEach(port -> {
                        try (Socket socket = new Socket()) {
                            socket.connect(new InetSocketAddress(host, port), 100);
                            System.out.printf("%s : %d OPENED\n", host, port);
                        } catch (IOException e) {
                            System.out.printf("%s : %d CLOSED\n", host, port);
                        }
                    });
        }
    }
}
