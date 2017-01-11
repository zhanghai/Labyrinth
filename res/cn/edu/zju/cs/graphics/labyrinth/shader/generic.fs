uniform sampler2D uTexture;
varying vec3 vTextureCoordinate;

void main() {
    gl_FragColor = texture2D(uTexture, vTextureCoordinate.xy);
}
