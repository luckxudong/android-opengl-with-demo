package ice.model.vertex;

import ice.graphic.GlRes;
import ice.graphic.gl_status.GlStatusController;

import javax.microedition.khronos.opengles.GL11;

public interface VertexData extends GlStatusController, GlRes {

    void onDrawVertex(GL11 gl);

}
