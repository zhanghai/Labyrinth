attribute vec4 aVertex;
attribute vec3 aNormal;
//attribute vec2 aTextureCoordinate;
uniform mat4 uModelMatrix;
uniform mat4 uViewProjectionMatrix;
uniform mat3 uNormalMatrix;
varying vec3 vPosition;
varying vec3 vNormal;
//varying vec2 vTextureCoordinate;

void main() {
    vec4 modelPosition = uModelMatrix * aVertex;
    gl_Position = uViewProjectionMatrix * modelPosition;
    vPosition = modelPosition.xyz;
    // Using mat3() to remove translation.
    //vNormal = mat3(transpose(inverse(uModelMatrix))) * aNormal;
    vNormal = uNormalMatrix * aNormal;
    //vTextureCoordinate = aTextureCoordinate;
}
