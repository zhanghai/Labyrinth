attribute vec4 aPosition;
uniform mat4 uViewProjection;

void main() {
  gl_Position = uViewProjection * aPosition;
}
