package ice.node.particle_system;

/**
 * User: ice
 * Date: 11-11-25
 * Time: 上午10:32
 */
public class Particle {
    public float posX, posY;// 不要posZ考虑到混合时去掉了深度测试;
    public float size;
    public float colorR, colorG, colorB,colorA;
    public boolean alive;
}
