package tempMe;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.plugin.markdown.MarkdownStyle;
import com.deepoove.poi.plugin.markdown.MarkdownRenderData;
import com.deepoove.poi.plugin.markdown.MarkdownRenderPolicy;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;

import java.util.HashMap;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.File;
import java.util.Map;

public class App {
    public static void main(String[] args) throws Exception {
	String template = args[0];
	String outputFile = args[1];
	String inputDir = args[2];
        new App().renderTemplate(template, outputFile, inputDir);
    }

    public void renderTemplate(String template, String outputFile, String inputDir) throws Exception {
        ConfigureBuilder configBuilder = Configure.builder();
        MarkdownStyle style = MarkdownStyle.newStyle();
        style.setShowHeaderNumber(true);

        Map<String, Object> data = new HashMap<>();
	
	System.out.println(inputDir);
        File folder = new File(inputDir);
	File[] listOfFiles = folder.listFiles();
	for (File file : listOfFiles) {
            if (file.isFile()) {
	    	if (file.getName().startsWith("txt_")) {
                        data.put(file.getName(), new String(Files.readAllBytes(file.toPath())));
	    	}
	    	if (file.getName().startsWith("md_")) {
	            MarkdownRenderData code = new MarkdownRenderData();
                    code.setMarkdown(new String(Files.readAllBytes(file.toPath())));
                    code.setStyle(style);
                    data.put(file.getName(), code);
	            configBuilder.bind(file.getName(), new MarkdownRenderPolicy());
	    	}
            }
        }

	//https://github.com/Sayi/poi-tl/blob/2ba3d247a96a1101097f24a1f5d6772f141e20f0/poi-tl-plugin-markdown/src/main/java/com/deepoove/poi/plugin/markdown/MarkdownStyle.java#L41

        XWPFTemplate.compile(template, configBuilder.build())
                .render(data)
                .writeToFile(outputFile);
    }
}
