attribute vec4 aVertex;
uniform mat4 uModelMatrix;
uniform mat4 uViewProjectionMatrix;
uniform vec2 uTextureSize;
varying vec2 vTextureCoordinate;

void main() {
    vec4 modelPosition = uModelMatrix * aVertex;
    gl_Position = uViewProjectionMatrix * modelPosition;
    vTextureCoordinate = vec2(length(modelPosition.xy) / uTextureSize.x,
            modelPosition.z / uTextureSize.y);
}
