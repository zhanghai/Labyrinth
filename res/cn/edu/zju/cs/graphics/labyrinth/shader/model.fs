//uniform sampler2D uTexture;
varying vec3 vNormal;
//varying vec2 vTextureCoordinate;

void main() {
    //gl_FragColor = texture2D(uTexture, vec2(vTextureCoordinate.x, 1.0 - vTextureCoordinate.y))
    gl_FragColor = vec4(0.0, 1.0, 0.00001 * vNormal.x, 1.0);
}
