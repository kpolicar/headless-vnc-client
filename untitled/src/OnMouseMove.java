import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import static kpolicar.KpolicarHelpers.splitQuery;

public class OnMouseMove implements HttpHandler {
    private final VncViewer viewer;

    public OnMouseMove(VncViewer v) {
        this.viewer = v;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            Map<String, String> query = splitQuery(httpExchange.getRequestURI());
            int x = Integer.parseInt(query.get("x"));
            int y = Integer.parseInt(query.get("y"));

            viewer.rfb.writePointerEvent(new MouseEvent(viewer,
                    MouseEvent.MOUSE_MOVED,
                    0,
                    0,
                    x,
                    y,
                    1,
                    false));

            System.out.println("Mouse move x:" +x+" y:" +y);

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
