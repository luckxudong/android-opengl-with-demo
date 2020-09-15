package ice.node.particle_system;

import android.view.animation.AnimationUtils;
import ice.graphic.texture.Texture;
import ice.node.Overlay;

import javax.microedition.khronos.opengles.GL11;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static ice.model.Constants.BYTE_OF_FLOAT;
import static javax.microedition.khronos.opengles.GL11.*;

/**
 * User: ice
 * Date: 11-11-25
 * Time: 上午10:32
 */
public abstract class ParticleSystem extends Overlay {

    public ParticleSystem(int maxParticleNum, Texture texture) {
        this.maxParticleNum = maxParticleNum;
        this.texture = texture;

        ByteBuffer vfb = ByteBuffer.allocateDirect(BYTE_OF_FLOAT * 4 * (2 + 2) * maxParticleNum);
        vfb.order(ByteOrder.nativeOrder());
        vertexBuffer = vfb.asFloatBuffer();

        vfb = ByteBuffer.allocateDirect(BYTE_OF_FLOAT * 3 * maxParticleNum);
        vfb.order(ByteOrder.nativeOrder());
        colorBuffer = vfb.asFloatBuffer();
    }

    public void init() {
        if (inited) {
            System.out.println("already inited !");
            return;
        }

        Particle[] particles = onSetupParticles(maxParticleNum);
        if (particles.length > maxParticleNum)
            throw new IllegalStateException("first generated particles size shoud smaller than " + maxParticleNum);

        this.particles = particles;
        inited = true;
    }

    public void reset() {
        inited = false;
    }


    public void step() {
        if (!inited) throw new IllegalStateException("particles not setup yet !");
        onUpdateParticles(particles, AnimationUtils.currentAnimationTimeMillis());
    }

    protected abstract Particle[] onSetupParticles(int maxParticleNum);

    protected abstract void onUpdateParticles(Particle[] particles, long current);

    @Override
    protected void onDraw(GL11 gl) {
        int changedSize = fillActive();

        if (changedSize > 0) {
            drawActiveParticles(gl, changedSize);
        }

        step();
    }

    private void drawActiveParticles(GL11 gl, int changedSize) {
        gl.glEnableClientState(GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL_COLOR_ARRAY);

        texture.attach(gl);

        vertexBuffer.position(0);
        gl.glVertexPointer(2, GL_FLOAT, BYTE_OF_FLOAT * (2 + 2), vertexBuffer);

        vertexBuffer.position(2);
        gl.glTexCoordPointer(2, GL_FLOAT, BYTE_OF_FLOAT * (2 + 2), vertexBuffer);

        colorBuffer.position(0);
        gl.glColorPointer(3, GL_FLOAT, 0, colorBuffer);

        //gl.glDrawArrays(GL_POLYGON, 0, changedSize / (2 + 2));        //todo

        gl.glDisableClientState(GL_VERTEX_ARRAY);
        gl.glDisable(GL_TEXTURE_2D);
        gl.glDisableClientState(GL_TEXTURE_COORD_ARRAY);
    }

    private int fillActive() {
        vertexBuffer.position(0);
        vertexBuffer.limit(vertexBuffer.capacity());

        colorBuffer.position(0);
        colorBuffer.limit(colorBuffer.capacity());

        int changedSize = 0;
        float[] squarePoints = new float[4 * (2 + 2)];
        for (int index = 0; index < maxParticleNum; index++) {
            Particle particle = particles[index];

            if (particle.alive) {
                fillPos(squarePoints, particle);
                fillColorNormal(particle);
                vertexBuffer.put(squarePoints);
                changedSize += squarePoints.length;
            }

        }

        vertexBuffer.position(0);
        vertexBuffer.limit(changedSize);

        colorBuffer.limit(3 * changedSize / 16);
        return changedSize;
    }

    protected void fillColorNormal(Particle particle) {
        colorBuffer.put(particle.colorR);
        colorBuffer.put(particle.colorG);
        colorBuffer.put(particle.colorB);
    }

    private void fillPos(float[] squarePoints, Particle particle) {
        squarePoints[0] = particle.posX - particle.size / 2;
        squarePoints[1] = particle.posY + particle.size / 2;
        squarePoints[2] = 0;
        squarePoints[3] = 0;

        squarePoints[4] = squarePoints[0];
        squarePoints[5] = squarePoints[1] + particle.size;
        squarePoints[6] = 0;
        squarePoints[7] = 1;

        squarePoints[8] = squarePoints[4] + particle.size;
        squarePoints[9] = squarePoints[5];
        squarePoints[10] = 1;
        squarePoints[11] = 1;

        squarePoints[12] = squarePoints[8];
        squarePoints[13] = squarePoints[1];
        squarePoints[14] = 1;
        squarePoints[15] = 0;
    }

    private Texture texture;
    private int maxParticleNum;
    private boolean inited;
    protected Particle particles[];
    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;
}
