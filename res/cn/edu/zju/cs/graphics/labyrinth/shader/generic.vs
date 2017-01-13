attribute vec4 aVertex;
attribute vec2 aTextureCoordinate;
uniform mat4 uModelMatrix;
uniform mat4 uViewProjectionMatrix;
varying vec2 vTextureCoordinate;

void main() {
    gl_Position = uViewProjectionMatrix * uModelMatrix * aVertex;
    vTextureCoordinate = aTextureCoordinate;
}
