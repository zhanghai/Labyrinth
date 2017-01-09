attribute vec4 aPosition;
uniform mat4 uModelMatrix;
uniform mat4 uViewProjectionMatrix;
attribute vec2 a_TexCiirdinate;
varying vec2 v_TexCoordinate;

void main() {
    gl_Position =  uModelMatrix * aPosition;
}
