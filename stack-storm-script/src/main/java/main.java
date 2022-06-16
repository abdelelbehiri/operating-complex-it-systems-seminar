import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Random;


class DummyFileCreator {
    static int filesize = 1024 * 1024;
    static int count = 20;

    static File dir = new File("/Users/abdelrahmanelbehari/Desktop/test");
    static String ext = ".txt";

    public static void main(String[] args) {
        byte[] bytes = new byte[filesize];
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;

        try {
            for (int i = 0; i < count; i++) {
                Random rand = new Random();

                String name = String.format("%s%s", System.currentTimeMillis(), rand.nextInt(100000) + ext);
                File file = new File(dir, name);

                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos);

                rand.nextBytes(bytes);
                bos.write(bytes);

                bos.flush();
                bos.close();
                fos.flush();
                fos.close();
            }

        } catch (FileNotFoundException fnfe) {
            System.out.println("File not found" + fnfe);
        } catch (IOException ioe) {
            System.out.println("Error while writing to file" + ioe);
        } finally {
            try {
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
            } catch (Exception e) {
                System.out.println("Error while closing streams" + e);
            }
        }
    }

}


public class main {


    public static void sendNotificationToWebHook(Integer fileSize) throws IOException, InterruptedException {
        var values = new HashMap<String, String>() {{
            put("type", "WARN");
            put ("size", fileSize.toString());
        }};

        var objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writeValueAsString(values);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:80/api/v1/webhooks/dir-status"))
                .header("Content-Type","application/json")
                .header("X-Auth-Token","de0d8ffdcc264c769a9c401503101635")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
    }

    public static void executeCommandInDOckerContainer(){
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("docker", "exec" ,  "c0c17658bafa", "ls").inheritIO();

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("\nExited with error code : " + exitCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        sendNotificationToWebHook(60);
    }
}



// Webhook tooken 712fcsBdnwTtboBsJjsZ2IBemqvhmjQJ



//display_information:
//        name: Demo App
//        settings:
//        org_deploy_enabled: false
//        socket_mode_enabled: false
//        is_hosted: false
//        token_rotation_enabled: false