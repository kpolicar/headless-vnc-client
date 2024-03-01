import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import static kpolicar.KpolicarHelpers.splitQuery;

public class OnKeyDown implements HttpHandler {
    private final VncViewer viewer;

    public OnKeyDown(VncViewer v) {
        this.viewer = v;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            Map<String, String> query = splitQuery(httpExchange.getRequestURI());
            char key = query.get("key").charAt(0);

            viewer.rfb.writeKeyEvent(new KeyEvent(viewer,
                    KeyEvent.KEY_PRESSED,
                    0,
                    0,
                    KeyEvent.VK_UNDEFINED,
                    key));

            System.out.println("Key down: " + key);

            String response = "Success";
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
