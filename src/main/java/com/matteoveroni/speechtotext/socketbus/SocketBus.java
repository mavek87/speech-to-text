package com.matteoveroni.speechtotext.socketbus;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class SocketBus {

    public void waitResponse() {
        // Wait for the response on the callback address and port
        try (Socket callbackSocket = new Socket()) {
            callbackSocket.connect(new InetSocketAddress("localhost", 8888));
            byte[] response = new byte[4096];
            int read = callbackSocket.getInputStream().read(response);
            // Print the response
            System.out.println(new String(response));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendData(String host, int port, String jsonData) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("localhost", 8080));

            // Prepare the data to send to the server
            Map<String, Object> data = new HashMap<>();
            data.put("callback_address", "localhost");
            data.put("callback_port", 8888);
            data.put("id", "123456");
            data.put("signal", "TRANSCRIBE_AUDIO");
            data.put("file_name", "/path/to/audio_file.wav");

            // Convert the data to JSON and send it to the server
//            ObjectMapper objectMapper = new ObjectMapper();
//            byte[] jsonData = objectMapper.writeValueAsBytes(data);

            byte[] bytesJsonData = jsonData.getBytes(StandardCharsets.UTF_8);
            socket.getOutputStream().write(bytesJsonData);

            // Close the connection to the server
            socket.close();

            // Wait for the response on the callback address and port
            try (Socket callbackSocket = new Socket()) {
                callbackSocket.connect(new InetSocketAddress("localhost", 8888));
                byte[] response = new byte[4096];
                callbackSocket.getInputStream().read(response);
                // Print the response
                System.out.println(new String(response));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
