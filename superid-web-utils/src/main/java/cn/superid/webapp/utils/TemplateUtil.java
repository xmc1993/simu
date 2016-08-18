package cn.superid.webapp.utils;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class TemplateUtil {
    private static VelocityEngine velocityEngine = new VelocityEngine();

    static {
        velocityEngine.init();
    }

    private TemplateUtil() {
    }

    public static String renderWithVolocity(String templateStr, Map<String, Object> binding) throws IOException {
        if (templateStr == null) {
            return null;
        }
        Map<String, Object> tmplBinding = binding != null ? binding : new HashMap<String, Object>();
        VelocityContext context = new VelocityContext();
        for (String key : tmplBinding.keySet()) {
            context.put(key, tmplBinding.get(key));
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (PrintWriter writer = new PrintWriter(byteArrayOutputStream)) {
            Velocity.evaluate(context, writer, "app_template", templateStr);
            writer.flush();
            byte[] bytes = byteArrayOutputStream.toByteArray();
            return new String(bytes, "UTF-8");
        }
    }
}
