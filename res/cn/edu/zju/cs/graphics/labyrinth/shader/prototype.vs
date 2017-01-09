attribute vec4 aPosition;
uniform mat4 uModelMatrix;
uniform mat4 uViewProjectionMatrix;

void main() {
    gl_Position = uViewProjectionMatrix * uModelMatrix * aPosition;
}
