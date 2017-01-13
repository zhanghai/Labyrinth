attribute vec4 aVertex;
uniform mat4 uModelMatrix;
uniform mat4 uViewProjectionMatrix;

void main() {
    gl_Position = uViewProjectionMatrix * uModelMatrix * aVertex;
}
