import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import static kpolicar.KpolicarHelpers.splitQuery;

public class OnMouseDown implements HttpHandler {
    private final VncViewer viewer;

    public OnMouseDown(VncViewer v) {
        this.viewer = v;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            Map<String, String> query = splitQuery(httpExchange.getRequestURI());
            int x = Integer.parseInt(query.get("x"));
            int y = Integer.parseInt(query.get("y"));
            boolean isLeft = query.get("mouse") != "right";

            viewer.rfb.writePointerEvent(new MouseEvent(viewer,
                    MouseEvent.MOUSE_PRESSED,
                    0,
                    isLeft ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3_MASK,
                    x,
                    y,
                    1,
                    false,
                    isLeft ? 1 : 3));

            System.out.println("Mouse down x:" +x+" y:" +y+" mouse:" +query.get("mouse"));

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
