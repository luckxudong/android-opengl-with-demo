package ice.model.vertex;

public final class VertexAttribute {

    public static enum Usage {
        Position, Color, Normal, TextureCoordinates
    }


    public VertexAttribute(Usage usage, int dimension) {
        this.usage = usage;
        this.dimension = dimension;
    }

    public Usage getUsage() {
        return usage;
    }

    public int getDimension() {
        return dimension;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    private Usage usage;
    private int dimension;
    private int offset;
    private String alias; //the alias for the attribute used in a ShaderProgram
}
