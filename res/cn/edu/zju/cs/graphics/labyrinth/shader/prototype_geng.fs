uniform vec4 uColor;
uniform sampler2D uTexture;
varying vec2 vTexCoordinate;
void main() {
    vec2 change;
    change.x = vTexCoordinate.x*0.3 + 0.3;
    change.y = vTexCoordinate.y*0.3 + 0.3;
    gl_FragColor = texture2D(uTexture, change);
}
