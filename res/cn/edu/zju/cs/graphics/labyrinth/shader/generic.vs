attribute vec4 aPosition;
attribute vec3 aTextureCoordinate;
uniform mat4 uModelMatrix;
uniform mat4 uViewProjectionMatrix;
uniform mat3 uTextureMatrix;
varying vec2 vTextureCoordinate;

void main() {
    gl_Position = uViewProjectionMatrix * uModelMatrix * aPosition;
    vTextureCoordinate = (uTextureMatrix * aTextureCoordinate).xy;
}
