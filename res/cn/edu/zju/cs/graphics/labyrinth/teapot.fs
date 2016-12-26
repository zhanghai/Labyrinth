#define PI 3.14159265359

uniform sampler2D uTex;
varying vec3 vDir;
varying vec3 vNormal;

void main() {
    vec3 c = normalize(reflect(vDir, vNormal));
    vec2 t = vec2(atan(c.z, c.x) / PI, acos(c.y) * 2.0 / PI - 1.0) * 0.5 + vec2(0.5);
    //gl_FragColor = texture2D(uTex, t);
    //gl_FragColor = vec4(0, 0, 1, 1);
    // HACK for some better effect, no real meaning here.
    gl_FragColor = texture2D(uTex, normalize(vDir).xy);
}
