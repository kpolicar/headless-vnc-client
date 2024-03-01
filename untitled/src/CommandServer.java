import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class CommandServer implements Runnable {
    private final VncViewer viewer;

    public CommandServer(VncViewer v) {
        this.viewer = v;
    }

    public void run() {
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(8010), 0);
            server.createContext("/key/press", new OnKeyPress(viewer));
            server.createContext("/key/down", new OnKeyDown(viewer));
            server.createContext("/key/up", new OnKeyUp(viewer));
            server.createContext("/mouse/move", new OnMouseMove(viewer));
            server.createContext("/mouse/click", new OnMouseClick(viewer));
            server.createContext("/mouse/down", new OnMouseDown(viewer));
            server.createContext("/mouse/up", new OnMouseUp(viewer));
            server.setExecutor(null);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
            if (server != null)
                server.stop(0);
            run();
        }
    }
}
