package ice.practical;

import ice.graphic.gl_status.DepthController;
import ice.graphic.texture.Texture;
import ice.node.particle_system.Particle;
import ice.node.particle_system.PointParticleSystem;

import java.util.Random;

/**
 * User: ice
 * Date: 11-11-25
 * Time: 上午11:55
 */
public class TestParticleSystem extends PointParticleSystem {

    private class TestParticle extends Particle {
        public float speed;
        public float speedAngle;

        public TestParticle(float startX, float startY) {
            this.startX = startX;
            this.startY = startY;
        }

        public float getStartX() {
            return startX;
        }

        public float getStartY() {
            return startY;
        }

        private float startX, startY;
    }

    private long lastUpdateTime;

    public TestParticleSystem(int maxParticleNum, Texture texture) {
        super(maxParticleNum, texture, false);
        addGlStatusController(new DepthController(false));
    }

    @Override
    protected Particle[] onSetupParticles(int maxParticleNum) {
        TestParticle[] particles = new TestParticle[maxParticleNum];

        Random random = new Random();

        for (int i = 0; i < maxParticleNum; i++) {
            TestParticle particle = new TestParticle(0, 0);
            particle.size = 50 + random.nextInt(50);
            particle.alive = true;
            particle.speed = (i + 20) * 120 / (float) maxParticleNum;
            particle.speedAngle = (i + 10) * 10;
            particle.posX = particle.startX;
            particle.posY = particle.startY;
            particle.colorR = (float) Math.random();
            particle.colorG = (float) Math.random();
            particle.colorB = (float) Math.random();
            particles[i] = particle;
        }


        return particles;
    }

    @Override
    protected void onUpdateParticles(Particle[] particles, long current) {
        if (lastUpdateTime == 0) {
            lastUpdateTime = current;
            return;
        }

        float interval = (current - lastUpdateTime) / 1000.0f;

        for (int i = 0, size = particles.length; i < size; i++) {
            TestParticle particle = (TestParticle) particles[i];
            particle.posX = particle.startX + particle.speed * (float) Math.cos(particle.speedAngle);
            particle.posY = particle.startY + particle.speed * (float) Math.sin(particle.speedAngle);
            particle.speedAngle += (i + 20) * interval / size;
        }

        lastUpdateTime = current;
    }
}
