attribute vec4 aPosition;

uniform mat4 uModelMatrix;
uniform mat4 uViewProjectionMatrix;
//varying vec2 vTextureCoordinate;

void main() {
    gl_Position = uViewProjectionMatrix * uModelMatrix * aPosition;
    //vPos = uModelMatrix * aPosition;
    //vTextureCoordinate = aTextureCoordinate;
}
