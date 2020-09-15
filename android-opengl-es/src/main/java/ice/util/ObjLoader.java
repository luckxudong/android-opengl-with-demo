package ice.util;

import ice.model.vertex.VertexAttribute;
import ice.model.vertex.VertexAttributes;
import ice.util.ResourceRetriever;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Loads Wavefront OBJ files, ignores material files.
 *
 * @author mzechner
 */
public class ObjLoader {

    private VertexAttributes attributes;
    private float[] vertexData;
    private int vertexNum;

    public void loadObj(String vertexData) {
        try {
            loadObj(ResourceRetriever.getResourceAsStream(vertexData));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadObj(InputStream in) {
        loadObj(in, false);
    }

    /**
     * Loads a Wavefront OBJ file from the given input stream.
     *
     * @param in    the InputStream
     * @param flipV whether to flip the v texture coordinate or not
     */
    public void loadObj(InputStream in, boolean flipV) {
        String line = "";

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuffer b = new StringBuffer();
            String l = reader.readLine();
            while (l != null) {
                b.append(l);
                b.append("\n");
                l = reader.readLine();
            }

            line = b.toString();
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        loadObjFromString(line, flipV);
    }

    /**
     * Loads a mesh from the given string in Wavefront OBJ format
     *
     * @param obj   The string
     * @param flipV whether to flip the v texture coordinate or not
     * @return The Mesh
     */
    public void loadObjFromString(String obj, boolean flipV) {
        String[] lines = obj.split("\n");

        float[] vertices = new float[lines.length * 3];
        float[] normals = new float[lines.length * 3];
        float[] uv = new float[lines.length * 3];

        int numVertices = 0;
        int numNormals = 0;
        int numUV = 0;
        int numFaces = 0;

        int[] facesVerts = new int[lines.length * 3];
        int[] facesNormals = new int[lines.length * 3];
        int[] facesUV = new int[lines.length * 3];
        int vertexIndex = 0;
        int normalIndex = 0;
        int uvIndex = 0;
        int faceIndex = 0;

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.startsWith("v ")) {
                String[] tokens = line.split("[ ]+");
                vertices[vertexIndex] = Float.parseFloat(tokens[1]);
                vertices[vertexIndex + 1] = Float.parseFloat(tokens[2]);
                vertices[vertexIndex + 2] = Float.parseFloat(tokens[3]);
                vertexIndex += 3;
                numVertices++;
                continue;
            }

            if (line.startsWith("vn ")) {
                String[] tokens = line.split("[ ]+");
                normals[normalIndex] = Float.parseFloat(tokens[1]);
                normals[normalIndex + 1] = Float.parseFloat(tokens[2]);
                normals[normalIndex + 2] = Float.parseFloat(tokens[3]);
                normalIndex += 3;
                numNormals++;
                continue;
            }

            if (line.startsWith("vt")) {
                String[] tokens = line.split("[ ]+");
                uv[uvIndex] = Float.parseFloat(tokens[1]);
                uv[uvIndex + 1] = flipV ? 1 - Float.parseFloat(tokens[2]) : Float.parseFloat(tokens[2]);
                uvIndex += 2;
                numUV++;
                continue;
            }

            if (line.startsWith("f ")) {
                String[] tokens = line.split("[ ]+");

                String[] parts = tokens[1].split("/");
                facesVerts[faceIndex] = getIndex(parts[0], numVertices);
                if (parts.length > 2) facesNormals[faceIndex] = getIndex(parts[2], numNormals);
                if (parts.length > 1) facesUV[faceIndex] = getIndex(parts[1], numUV);
                faceIndex++;

                parts = tokens[2].split("/");
                facesVerts[faceIndex] = getIndex(parts[0], numVertices);
                if (parts.length > 2) facesNormals[faceIndex] = getIndex(parts[2], numNormals);
                if (parts.length > 1) facesUV[faceIndex] = getIndex(parts[1], numUV);
                faceIndex++;

                parts = tokens[3].split("/");
                facesVerts[faceIndex] = getIndex(parts[0], numVertices);
                if (parts.length > 2) facesNormals[faceIndex] = getIndex(parts[2], numNormals);
                if (parts.length > 1) facesUV[faceIndex] = getIndex(parts[1], numUV);
                faceIndex++;
                numFaces++;
                continue;
            }
        }

        vertexNum = numFaces * 3;
        int size = vertexNum * (3 + (numNormals > 0 ? 3 : 0) + (numUV > 0 ? 2 : 0));
        vertexData = new float[size];

        for (int i = 0, vi = 0; i < vertexNum; i++) {
            int vertexIdx = facesVerts[i] * 3;
            vertexData[vi++] = vertices[vertexIdx];
            vertexData[vi++] = vertices[vertexIdx + 1];
            vertexData[vi++] = vertices[vertexIdx + 2];

            if (numNormals > 0) {
                int normalIdx = facesNormals[i] * 3;
                vertexData[vi++] = normals[normalIdx];
                vertexData[vi++] = normals[normalIdx + 1];
                vertexData[vi++] = normals[normalIdx + 2];
            }
            if (numUV > 0) {
                int uvIdx = facesUV[i] * 2;
                vertexData[vi++] = uv[uvIdx];
                vertexData[vi++] = uv[uvIdx + 1];
            }
        }

        ArrayList<VertexAttribute> attributeList = new ArrayList<VertexAttribute>();

        attributeList.add(
                new VertexAttribute(
                        VertexAttribute.Usage.Position,
                        3
                )
        );

        if (numNormals > 0) {
            attributeList.add(
                    new VertexAttribute(
                            VertexAttribute.Usage.Normal,
                            3
                    )
            );
        }

        if (numUV > 0) {
            attributeList.add(
                    new VertexAttribute(
                            VertexAttribute.Usage.TextureCoordinates,
                            2
                    )
            );
        }

        VertexAttribute[] attributesArray = new VertexAttribute[attributeList.size()];
        attributeList.toArray(attributesArray);
        attributes = new VertexAttributes(attributesArray);
    }

    private int getIndex(String index, int size) {
        if (index == null || index.length() == 0) return 0;
        int idx = Integer.parseInt(index);
        if (idx < 0)
            return size + idx;
        else
            return idx - 1;
    }


    public VertexAttributes getAttributes() {
        return attributes;
    }

    public int getVertexNum() {
        return vertexNum;
    }

    public float[] getVertexData() {
        return vertexData;
    }
}
