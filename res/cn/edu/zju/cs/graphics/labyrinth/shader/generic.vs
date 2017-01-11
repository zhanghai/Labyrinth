attribute vec4 aPosition;
attribute vec3 aTextureCoordinate;
uniform mat4 uModelMatrix;
uniform mat4 uViewProjectionMatrix;
uniform mat3 uTextureMatrix;
varying vec3 vTextureCoordinate;

void main() {
    gl_Position = uViewProjectionMatrix * uModelMatrix * aPosition;
    vTextureCoordinate = uTextureMatrix * aTextureCoordinate;
}
