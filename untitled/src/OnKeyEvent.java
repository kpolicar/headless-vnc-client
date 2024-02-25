import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.OutputStream;

public class OnKeyEvent implements HttpHandler {
    private final VncViewer viewer;

    public OnKeyEvent(VncViewer v) {
        this.viewer = v;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        char key = query.replace("key=", "").charAt(0);
        viewer.rfb.writeKeyEvent(new KeyEvent(viewer,
                KeyEvent.KEY_PRESSED,
                0,
                0,
                KeyEvent.VK_UNDEFINED,
                key));
        viewer.rfb.writeKeyEvent(new KeyEvent(viewer,
                KeyEvent.KEY_RELEASED,
                0,
                0,
                KeyEvent.VK_UNDEFINED,
                key));

        String response = "Success";
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
