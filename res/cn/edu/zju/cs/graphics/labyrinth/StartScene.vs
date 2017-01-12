attribute vec4 aPosition;
uniform mat4 uModelMatrix;
varying vec2 vTexCoordinate;
uniform mat4 uViewProjectionMatrix;

void main() {
  vTexCoordinate = vec2((aPosition.x+1.0)/2.0,(aPosition.y+1.0)/2.0);
  gl_Position = uViewProjectionMatrix * uModelMatrix * aPosition;
}
