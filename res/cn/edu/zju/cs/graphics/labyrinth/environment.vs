attribute vec4 aPosition;
uniform mat4 uInvViewProj;
varying vec3 vDir;

void main() {
  vec4 tmp = uInvViewProj * vec4(aPosition.xy, 1.0, 1.0);
  vDir = tmp.xyz / tmp.w;
  gl_Position = aPosition;
}
