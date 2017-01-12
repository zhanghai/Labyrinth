package cn.edu.zju.cs.graphics.labyrinth.util;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class MatrixUtils {

    public static final Matrix4f IDENTITY_4 = new Matrix4f();

    public static final Matrix3f IDENTITY_3 = new Matrix3f();

    private static final Matrix4f sTempMatrix = new Matrix4f();

    private MatrixUtils() {}

    public static Matrix4f skewXAroundY(Matrix4f matrix, float angle, Matrix4f destination) {
        return matrix.mul(sTempMatrix.identity().m20((float) Math.tan(angle)), destination);
    }

    public static Matrix4f skewXAroundY(Matrix4f matrix, float angle) {
        return skewXAroundY(matrix, angle, matrix);
    }

    public static Matrix4f skewYAroundX(Matrix4f matrix, float angle, Matrix4f destination) {
        return matrix.mul(sTempMatrix.identity().m21((float) Math.tan(angle)), destination);
    }

    public static Matrix4f skewYAroundX(Matrix4f matrix, float angle) {
        return skewYAroundX(matrix, angle, matrix);
    }
}
