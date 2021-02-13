package org.webdriver.patatiumwebui.web.simulation;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 截屏返回接口测试
 */
@Controller
@RequestMapping("/screen")
public class ScreenController {
    /**
     * 保持截屏
     * @param request
     * @return
     */
    @RequestMapping("/saveScreen.json")
    @ResponseBody
    public Map<String, Object> saveScreen(HttpServletRequest request,
                                          @RequestParam(required = true) String nationaltaxLoginname
                                            , @RequestParam(required = true) String taxName
                                            , @RequestParam(required = true) String belongDateBegin
                                            , @RequestParam(required = true) String belongDateEnd
                                            , @RequestParam("file")MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", "true");
        System.out.println("nationaltaxLoginname:" + nationaltaxLoginname);
        System.out.println("taxName:" + taxName);
        System.out.println("belongDateBegin:" + belongDateBegin);
        System.out.println("belongDateEnd:" + belongDateEnd);
        System.out.println(file);
        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File("E:\\hongjinqiu\\tmp\\test.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("/screen.htm")
    public String screen(HttpServletRequest request) {

        return "screen";
    }
}
