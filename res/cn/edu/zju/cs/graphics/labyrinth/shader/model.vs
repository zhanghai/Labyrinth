attribute vec4 aVertex;
attribute vec4 aNormal;
//attribute vec2 aTextureCoordinate;
uniform mat4 uModelMatrix;
uniform mat4 uViewProjectionMatrix;
varying vec3 vNormal;
//varying vec2 vTextureCoordinate;

void main() {
    gl_Position = uViewProjectionMatrix * uModelMatrix * aVertex;
    vNormal = normalize((uViewProjectionMatrix * uModelMatrix * aNormal).xyz);
    //vTextureCoordinate = aTextureCoordinate;
}
