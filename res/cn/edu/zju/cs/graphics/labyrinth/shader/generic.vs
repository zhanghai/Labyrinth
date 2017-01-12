attribute vec4 aPosition;
attribute vec2 aTextureCoordinate;
uniform mat4 uModelMatrix;
uniform mat4 uViewProjectionMatrix;
varying vec2 vTextureCoordinate;

void main() {
    gl_Position = uViewProjectionMatrix * uModelMatrix * aPosition;
    vTextureCoordinate = aTextureCoordinate;
}
