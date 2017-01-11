package cn.edu.zju.cs.graphics.labyrinth.util;

public class ResourceUtils {

    private ResourceUtils() {}

    public static String makeResource(String type, String name) {
        return "cn/edu/zju/cs/graphics/labyrinth/" + type + "/" + name;
    }

    public static String makeShaderResource(String name) {
        return makeResource("shader", name);
    }

    public static String makeTextureResource(String name) {
        return makeResource("texture", name);
    }
}
