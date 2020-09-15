package ice.model.vertex;

import static ice.model.Constants.*;

public final class VertexAttributes {

    private VertexAttribute[] attributes;

    /**
     * the size of a single vertex in bytes *
     */
    public final int vertexSize;

    public VertexAttributes(VertexAttribute... attributes) {
        if (attributes.length == 0) throw new IllegalArgumentException("attributes must be >= 1");

        this.attributes = attributes;

        checkValidity();
        vertexSize = calculateOffsets();
    }

    public int size() {
        return attributes.length;
    }

    public VertexAttribute get(int index) {
        return attributes[index];
    }

    private int calculateOffsets() {
        int count = 0;

        for (VertexAttribute attribute : attributes) {
            attribute.setOffset(count);

            count += BYTE_OF_FLOAT * attribute.getDimension();
        }

        return count;
    }

    private void checkValidity() {
        boolean pos = false;
        boolean cols = false;
        boolean nors = false;

        for (int i = 0; i < attributes.length; i++) {
            VertexAttribute attribute = attributes[i];
            VertexAttribute.Usage usage = attribute.getUsage();
            switch (usage) {
                case Position:
                    if (pos)
                        throw new IllegalArgumentException("two position attributes were specified");
                    pos = true;
                    break;

                case Normal:
                    if (nors)
                        throw new IllegalArgumentException("two normal attributes were specified");
                    nors = true;
                    break;

                case Color:
                    if (attribute.getDimension() != 4)
                        throw new IllegalArgumentException("color attribute must have 4 components");

                    if (cols)
                        throw new IllegalArgumentException("two color attributes were specified");

                    cols = true;
                    break;
            }

        }

        if (pos == false)
            throw new IllegalArgumentException("no position attribute was specified");
    }

}
