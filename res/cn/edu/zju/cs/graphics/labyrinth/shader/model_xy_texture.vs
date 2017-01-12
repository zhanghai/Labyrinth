attribute vec4 aPosition;
uniform mat4 uModelMatrix;
uniform mat4 uViewProjectionMatrix;
uniform vec2 uTextureSize;
varying vec2 vTextureCoordinate;

void main() {
    vec4 modelPosition = uModelMatrix * aPosition;
    gl_Position = uViewProjectionMatrix * modelPosition;
    vTextureCoordinate = vec2(modelPosition.x / uTextureSize.x, modelPosition.y / uTextureSize.y);
}
