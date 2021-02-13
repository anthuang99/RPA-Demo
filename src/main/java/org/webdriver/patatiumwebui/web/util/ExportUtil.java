package org.webdriver.patatiumwebui.web.util;

import org.apache.commons.io.FileUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;

public class ExportUtil {
    /**
     * 文件导出
     * @param temp
     * @param showName
     * @param res
     * @throws Exception
     */
    public static void exportFile(File temp, String showName, HttpServletResponse res) throws Exception {
        OutputStream os = res.getOutputStream();
        try {
            res.reset();
            showName = new String(showName.getBytes("UTF-8"), "ISO-8859-1");
            res.setHeader("Content-Disposition", "attachment; filename=\"" + showName+"\";charset=UTF-8");
            res.setContentType("application/octet-stream; charset=utf-8");
            os.write(FileUtils.readFileToByteArray(temp));
            os.flush();
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }
}
