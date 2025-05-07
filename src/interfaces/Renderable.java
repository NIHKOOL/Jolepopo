package interfaces;

import camera.Camera;
import javafx.scene.canvas.GraphicsContext;

public interface Renderable {
	void render(GraphicsContext gc, Camera camera);
}
