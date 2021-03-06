package spoon.common.net;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import spoon.common.utils.ErrorUtils;

import java.io.*;
import java.net.URL;

@Slf4j
public class HttpParsing {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";

    public static String getJson(String url) {
        try {
            return Jsoup.connect(url)
                    .ignoreContentType(true)
                    .userAgent(USER_AGENT)
                    .maxBodySize(0)
                    .timeout(30 * 1000)
                    .execute()
                    .body();
        } catch (HttpStatusException e) {
            log.warn("에러코드 : {}, 주소 : {}", e.getStatusCode(), url, e);
            log.warn("{}", ErrorUtils.trace(e.getStackTrace()));
        } catch (IOException e) {
            log.warn("사이트에 접속할 수 없습니다. - 에러코드: {}, 주소: {}", e.getMessage(), url, e);
            log.warn("{}", ErrorUtils.trace(e.getStackTrace()));
        }
        return null;
    }

    public static boolean saveImage(String flagUrl, String filePath) {
        File file = new File(filePath);
        file.deleteOnExit();
        try (InputStream in = new BufferedInputStream(new URL(flagUrl).openStream());
             ByteArrayOutputStream out = new ByteArrayOutputStream();
             FileOutputStream fos = new FileOutputStream(filePath)) {

            int n;
            byte[] buf = new byte[1024];
            while (-1 != (n = in.read(buf))) {
                out.write(buf, 0, n);
            }
            byte[] response = out.toByteArray();
            fos.write(response);

        } catch (IOException e) {
            log.warn("이미지를 저장하지 못하였습니다. - flagUrl: {}, filePath: {}, 에러코드: {}", flagUrl, filePath, e.getMessage());
            log.warn("{}", ErrorUtils.trace(e.getStackTrace()));
            return false;
        }
        return true;
    }
}
