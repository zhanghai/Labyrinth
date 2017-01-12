#define PI 3.14159265359

uniform sampler2D uTex;
varying vec2 vTexCoordinate;

void main() {
  gl_FragColor = texture2D(uTex, vec2(vTexCoordinate.x,-vTexCoordinate.y));

}
