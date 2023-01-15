package org.example.resources;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class SpeechToTextApiResource implements Handler {

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
//        var output = new Object(){ String message = ""; };

        byte[] fileBytes = ctx.bodyAsBytes();
        String fileName = UUID.randomUUID().toString();
        File outputFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(outputFile)){
            fos.write(fileBytes);
            String sha256OfFileContent = getSHA256OfFileContent(fileName);
            ctx.status(200);
            ctx.json(sha256OfFileContent);
        } catch (IOException e) {
            e.printStackTrace();
            ctx.status(400);
            ctx.json("Upload error!");
        }
//        ctx.bodyInputStream();
//        if (ctx.uploadedFiles("file").size() == 1) {
//            ctx.uploadedFiles("file").stream()
//                    .findFirst()
//                    .map(UploadedFile::content)
//                    .ifPresent(inputStream -> {
//                        Optional<File> file = salvaFile(inputStream);
//                        if(file.isPresent()) {
//                            try {
//                                output.message = getSHA256OfFileContent(file.get().getPath());
//                            } catch (Exception e) {}
//                        }
//                        ctx.json("File upload success!");
//                    });
//            ctx.json(output.message);
//        } else {
//            ctx.json("Only one file at a time is accepted.");
//        }
    }

    private Optional<File> salvaFile(InputStream inputStream) {
        String filePath = UUID.randomUUID().toString();
        File outputFile = new File(filePath);
        try (OutputStream outputStream = new FileOutputStream(outputFile)) {
            int read;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (Exception e) {
            log.error("Error", e);
            return Optional.empty();
        }
        return Optional.ofNullable(outputFile);
    }

    public String getSHA256OfFileContent(String fileName) throws Exception {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        FileInputStream fis = new FileInputStream(fileName);
        DigestInputStream dis = new DigestInputStream(fis, sha256);
        byte[] buffer = new byte[8192];
        while (dis.read(buffer) != -1) ;
        byte[] digest = sha256.digest();
        dis.close();
        fis.close();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
}
