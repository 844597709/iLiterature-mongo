package com.swust.kelab.service.metasearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

@Service
public class WebPageText {
    private Logger LOGGER = LoggerFactory.getLogger("datalog.metasearch");
    private List<String> lines;
    private final static int blocksWidth = 3;
    private int threshold;
    private String html;
    private boolean flag;
    private int start;
    private int end;
    private StringBuilder text;
    private ArrayList<Integer> indexDistribution;

    public WebPageText() {
        lines = new ArrayList<String>();
        indexDistribution = new ArrayList<Integer>();
        text = new StringBuilder();
        flag = false;
        /* 当待抽取的网页正文中遇到成块的新闻标题未剔除时，只要增大此阈值即可。 */
        /* 阈值增大，准确率提升，召回率下降；值变小，噪声会大，但可以保证抽到只有一句话的正文 */
        threshold = -1;
    }

    public String webCode(String webUrl, String codeFormat) {
        String urlString = webUrl.toString();
        String html = "";
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.2)");
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            int isOk = connection.getResponseCode();
            if (isOk == 200) {
                BufferedReader br;
                br = new BufferedReader(new InputStreamReader(connection.getInputStream(), codeFormat));
                String buff;
                buff = br.readLine();
                StringBuffer sb = new StringBuffer();
                while (buff != null) {
                    sb.append(buff);
                    buff = br.readLine();
                }
                html = sb.toString();
            } else
                html = "timeout";
        } catch (IOException e) {
            html = "timeout";
            LOGGER.error("抽取正文网络超时");
        }
        if (html.contains("<meta")) {
            String code1[] = html.split("charset=");
            String code = "";
            if (code1.length > 1) {
                String code2[] = code1[1].split("\"");
                if (code2[0].isEmpty()) {
                    code = code2[1];
                } else {
                    code = code2[0];
                }
                return code;
            } else {
                code = "wrong";
                return code;
            }
        } else {
            return "wrong";
        }

    }

    public String webHtml(String webUrl, String codeFormat) {
        String urlString = webUrl.toString();
        String html = "";
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/.0 (Windows; U; Windows NT 5.2)");
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            int isOk = connection.getResponseCode();
            if (isOk == 200) {
                BufferedReader br;
                br = new BufferedReader(new InputStreamReader(connection.getInputStream(), codeFormat));
                String buff;
                buff = br.readLine();
                StringBuffer sb = new StringBuffer();
                while (buff != null) {
                    sb.append(buff);
                    buff = br.readLine();
                }
                html = sb.toString();
                Document doc = Jsoup.parse(html);
                html = doc.toString();
            } else
                html = "timeout";
        } catch (IOException e) {
            html = "timeout";
            LOGGER.error("网络超时");
        }
        return html;
    }

    /**
     * 抽取网页正文，不判断该网页是否是目录型。即已知传入的肯定是可以抽取正文的主题类网页。
     * 
     * @param _html 网页HTML字符串
     * 
     * @return 网页正文string
     */
    public String parse(String _html) {
        return parse(_html, false);
    }

    /**
     * 判断传入HTML，若是主题类网页，则抽取正文；否则输出<b>"unkown"</b>。
     * 
     * @param _html 网页HTML字符串
     * @param _flag true进行主题类判断, 省略此参数则默认为false
     * 
     * @return 网页正文string
     */
    public String parse(String _html, boolean _flag) {
        flag = _flag;
        html = _html;
        html = preProcess(html);
        return getText();
    }

    private static int FREQUENT_URL = 30;
    private static Pattern links = Pattern.compile(
            "<[aA]\\s+[Hh][Rr][Ee][Ff]=[\"|\']?([^>\"\' ]+)[\"|\']?\\s*[^>]*>([^>]+)</a>(\\s*.{0," + FREQUENT_URL
                    + "}\\s*<a\\s+href=[\"|\']?([^>\"\' ]+)[\"|\']?\\s*[^>]*>([^>]+)</[aA]>){2,100}", Pattern.DOTALL);

    private static String preProcess(String source) {

        source = source.replaceAll("(?is)<!DOCTYPE.*?>", "");
        source = source.replaceAll("(?is)<!--.*?-->", ""); // remove html comment
        source = source.replaceAll("(?is)<script.*?>.*?</script>", ""); // remove javascript
        source = source.replaceAll("(?is)<style.*?>.*?</style>", ""); // remove css
        source = source.replaceAll("&.{2,5};|&#.{2,5};", " "); // remove special char

        // 剔除连续成片的超链接文本（认为是，广告或噪音）,超链接多藏于span中
        source = source.replaceAll("<[sS][pP][aA][nN].*?>", "");
        source = source.replaceAll("</[sS][pP][aA][nN]>", "");
        source = source.replaceAll(" ", "");

        int len = source.length();

        while ((source = links.matcher(source).replaceAll("")).length() != len) {
            len = source.length();
        }

        // source = links.matcher(source).replaceAll("");

        // 防止html中在<>中包括大于号的判断
        source = source.replaceAll("<[^>'\"]*['\"].*['\"].*?>", "");
        source = source.replaceAll("<.*?>", "");
        source = source.replaceAll("<.*?>", "");
        source = source.replaceAll("&middot;", ".");
        return source;

    }

    private String getText() {
        lines = Arrays.asList(html.split("\n"));

        indexDistribution.clear();

        int empty = 0;// 空行的数量
        for (int i = 0; i < lines.size() - blocksWidth; i++) {

            if (lines.get(i).length() == 0) {
                empty++;
            }

            int wordsNum = 0;
            for (int j = i; j < i + blocksWidth; j++) {
                lines.set(j, lines.get(j).replaceAll("\\s+", ""));
                wordsNum += lines.get(j).length();
            }
            indexDistribution.add(wordsNum);
            // System.out.println(wordsNum);
        }
        int sum = 0;

        for (int i = 0; i < indexDistribution.size(); i++) {
            sum += indexDistribution.get(i);
        }

        try {
            threshold = Math.min(100, (sum / indexDistribution.size()) << (empty / (lines.size() - empty) >>> 1));
            threshold = Math.max(50, threshold);
        } catch (Exception e) {
            LOGGER.info("爬取网页失败");
        }

        start = -1;
        end = -1;
        boolean boolstart = false, boolend = false;
        boolean firstMatch = true;// 前面的标题块往往比较小，应该减小与它匹配的阈值
        text.setLength(0);

        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < indexDistribution.size() - 1; i++) {

            if (firstMatch && !boolstart) {
                if (indexDistribution.get(i) > (threshold / 2) && !boolstart) {
                    if (indexDistribution.get(i + 1).intValue() != 0 || indexDistribution.get(i + 2).intValue() != 0) {
                        firstMatch = false;
                        boolstart = true;
                        start = i;
                        continue;
                    }
                }

            }
            if (indexDistribution.get(i) > threshold && !boolstart) {
                if (indexDistribution.get(i + 1).intValue() != 0 || indexDistribution.get(i + 2).intValue() != 0
                        || indexDistribution.get(i + 3).intValue() != 0) {
                    boolstart = true;
                    start = i;
                    continue;
                }
            }
            if (boolstart) {
                if (indexDistribution.get(i).intValue() == 0 || indexDistribution.get(i + 1).intValue() == 0) {
                    end = i;
                    boolend = true;
                }
            }

            if (boolend) {
                buffer.setLength(0);
                for (int ii = start; ii <= end; ii++) {
                    if (lines.get(ii).length() < 5)
                        continue;
                    buffer.append(lines.get(ii) + "\n");
                }
                String str = buffer.toString();
                if (str.contains("Copyright") || str.contains("版权所有"))
                    continue;
                text.append(str);
                boolstart = boolend = false;
            }
        }

        if (start > end) {
            buffer.setLength(0);
            int size_1 = lines.size() - 1;
            for (int ii = start; ii <= size_1; ii++) {
                if (lines.get(ii).length() < 5)
                    continue;
                buffer.append(lines.get(ii) + "\n");
            }
            String str = buffer.toString();
            if ((!str.contains("Copyright")) || (!str.contains("版权所有"))) {
                text.append(str);
            }
        }
        if (validedWebpageText(text.toString())) {
            return text.toString();
        } else {
            return "";
        }
    }

    public boolean validedWebpageText(String content) {
        boolean isSave = true;
        if (content.contains("�")) {
            isSave = false;
        }
        if (content.contains("x")) {
            List temp = Arrays.asList(content.split("x"));
            if (temp.size() > 20) {
                isSave = false;
            }
        }
        return isSave;
    }

    public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
        WebPageText webPageText = new WebPageText();
        // String codeFormat=webPageText.webCode("http://news.sina.com.cn/","utf-8");
        // String html=webPageText.webHtml("http://news.sina.com.cn/", codeFormat);
        String codeFormat = webPageText
                .webCode(
                        "http://baike.baidu.com/link?url=fYmQjJiQRY16rQnBq9QiMWMadYUthaLSzXBLxYdG5s565PN13TNRvTzLOwMz5Ul6wTH27R_Wri1Cj1w8WiYRcq",
                        "utf-8");
        String html = webPageText
                .webHtml(
                        "http://baike.baidu.com/link?url=fYmQjJiQRY16rQnBq9QiMWMadYUthaLSzXBLxYdG5s565PN13TNRvTzLOwMz5Ul6wTH27R_Wri1Cj1w8WiYRcq",
                        codeFormat);
        String content = webPageText.parse(html, true);
        System.out.println(content);
    }
}
