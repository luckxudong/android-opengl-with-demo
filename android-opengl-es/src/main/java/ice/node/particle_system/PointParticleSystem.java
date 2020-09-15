package ice.node.particle_system;

import android.view.animation.AnimationUtils;
import ice.graphic.texture.Texture;
import ice.node.Overlay;

import javax.microedition.khronos.opengles.GL11;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static javax.microedition.khronos.opengles.GL11.*;
import static ice.model.Constants.*;

/**
 * User: jason
 * Date: 12-1-11
 * Time: 下午3:29
 */
public abstract class PointParticleSystem extends Overlay {

    public PointParticleSystem(int maxParticleNum, Texture texture, boolean sameSize) {
        this.maxParticleNum = maxParticleNum;
        this.texture = texture;
        this.sameSize = sameSize;

        if (texture != null)
            texture.setCoordSupliedBySystem(true);

        ByteBuffer vfb = ByteBuffer.allocateDirect(SIZE_OF_FLOAT * 2 * maxParticleNum);
        vfb.order(ByteOrder.nativeOrder());
        vertexBuffer = vfb.asFloatBuffer();

        if (!sameSize) {
            vfb = ByteBuffer.allocateDirect(SIZE_OF_FLOAT * 1 * maxParticleNum);
            vfb.order(ByteOrder.nativeOrder());
            sizeBuffer = vfb.asFloatBuffer();
        }

        vfb = ByteBuffer.allocateDirect(SIZE_OF_FLOAT * 4 * maxParticleNum);
        vfb.order(ByteOrder.nativeOrder());
        colorBuffer = vfb.asFloatBuffer();


        init();
    }

    private void init() {

        Particle[] particles = onSetupParticles(maxParticleNum);
        if (particles.length > maxParticleNum)
            throw new IllegalStateException("first generated particles size should smaller than " + maxParticleNum);

        this.particles = particles;
    }

    public void step() {

        onUpdateParticles(particles, AnimationUtils.currentAnimationTimeMillis());

    }

    protected abstract Particle[] onSetupParticles(int maxParticleNum);

    protected abstract void onUpdateParticles(Particle[] particles, long current);

    @Override
    protected void onDraw(GL11 gl) {

        int liveCount = fillActive();

        if (liveCount > 0) {
            drawActiveParticles(gl, liveCount);
        }

        step();
    }

    private void drawActiveParticles(GL11 gl, int liveCount) {

        gl.glEnable(GL_POINT_SPRITE_OES);
        gl.glEnableClientState(GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL_COLOR_ARRAY);

        texture.attach(gl);

        //An advantage of representing particles with point sprites is that texture coordinate generation can be handled by the system
        gl.glTexEnvi(GL_POINT_SPRITE_OES, GL_COORD_REPLACE_OES, GL_TRUE);


        vertexBuffer.position(0);
        colorBuffer.position(0);
        colorBuffer.limit(4 * liveCount);
        vertexBuffer.limit(2 * liveCount);

        gl.glVertexPointer(2, GL_FLOAT, 0, vertexBuffer);
        gl.glColorPointer(4, GL_FLOAT, 0, colorBuffer);

        if (sameSize) {
            gl.glPointSize(particles[0].size);
        }
        else {
            gl.glEnableClientState(GL_POINT_SIZE_ARRAY_OES);

            sizeBuffer.position(0);
            sizeBuffer.limit(liveCount);
            gl.glPointSizePointerOES(GL_FLOAT, 0, sizeBuffer);
        }


        gl.glDrawArrays(GL_POINTS, 0, liveCount / 2);

        texture.detach(gl, this);

        gl.glDisableClientState(GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL_COLOR_ARRAY);

        if (!sameSize)
            gl.glDisableClientState(GL11.GL_POINT_SIZE_ARRAY_OES);

        gl.glDisable(GL11.GL_POINT_SPRITE_OES);
    }

    private int fillActive() {

        vertexBuffer.position(0);
        if (!sameSize) sizeBuffer.position(0);
        colorBuffer.position(0);


        int liveCount = 0;

        for (int index = 0; index < maxParticleNum; index++) {

            Particle particle = particles[index];

            if (particle.alive) {
                vertexBuffer.put(
                        new float[]{particle.posX, particle.posY}
                );

                if (!sameSize) sizeBuffer.put(particle.size);

                colorBuffer.put(
                        new float[]{particle.colorR, particle.colorG, particle.colorB, particle.colorA}
                );


                liveCount++;
            }

        }

        return liveCount;
    }

    private boolean sameSize;
    private Texture texture;
    private int maxParticleNum;
    protected Particle particles[];
    private FloatBuffer vertexBuffer;
    private FloatBuffer sizeBuffer;
    private FloatBuffer colorBuffer;
}
