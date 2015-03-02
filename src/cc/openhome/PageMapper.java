package cc.openhome;

import static cc.openhome.IO.*;
import static java.lang.System.out;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Justin
 */
public class PageMapper {
    private static PathContent template = pathContent(Paths.get("c:\\workspace\\template.html"));
    
    private static Map<String, Pattern> patterns = new HashMap<>();
    static {
        patterns.put("title", Pattern.compile("<title>(.*)</title>"));
        patterns.put("div class=\"article\"", Pattern.compile("<div class=\"article\">((.*\\s*)*?)</div>"));
        patterns.put("stripTags", Pattern.compile("\\<[^>]*>"));
    }    
            
    public static void main(String[] args) {
//        List<String> htmlFiles = htmlFiles(Paths.get("c:\\workspace\\NewJava\\"));
//        htmlFiles.stream()
//                .map(Paths::get)
//                // 排除首頁，因為比較複雜，要手動修改
//                .filter(path -> !path.getFileName().toString().equals("index.html"))
//                .map(IO::pathContent)
//                // 處理 div class="article" 與 title
//                .map(PageMapper::map2Template)
//                .forEach(IO::write);
          String content = tagContent(pathContent(Paths.get("c:\\workspace\\Java\\Abstract.html")).content, "div class=\"article\"");
          out.println(patterns.get("stripTags").matcher(content).replaceAll("").trim().substring(0, 100) + "...");
    }
    
    private static String tagContent(String content, String tag) {
        Matcher matcher = patterns.get(tag).matcher(content);
        matcher.find();
        return matcher.group(1);
    }
     
    private static PathContent map2Template(PathContent pathContent) {
        String content = tagContent(pathContent.content, "div class=\"article\"");
        
         pathContent.content = 
             template.content
                   .replace("#content#", content)
                   .replaceAll("#title#", tagContent(pathContent.content, "title"))
                   .replaceAll("#fileName#", pathContent.path.getFileName().toString())
                   .replaceAll("#description#", patterns.get("stripTags").matcher(content).replaceAll("").trim().substring(0, 100) + "...");
         return pathContent;
    }
}
