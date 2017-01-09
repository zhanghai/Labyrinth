attribute vec4 aPosition;
uniform mat4 uModelMatrix;
uniform mat4 uViewProjectionMatrix;
varying vec2 vTexCoordinate;

void main() {
    gl_Position =  uViewProjectionMatrix*uModelMatrix * aPosition ;
    vTexCoordinate = aPosition.xy;
}
