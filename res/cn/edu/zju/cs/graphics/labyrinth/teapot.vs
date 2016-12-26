attribute vec4 aPosition;
attribute vec3 aNormal;
uniform mat4 uViewProj;
uniform vec3 uCameraPosition;
varying vec3 vDir;
varying vec3 vNormal;

void main() {
    vDir = aPosition.xyz - uCameraPosition;
    vNormal = aNormal;
    gl_Position = uViewProj * aPosition;
}
