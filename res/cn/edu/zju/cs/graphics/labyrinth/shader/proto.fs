uniform vec4 uColor;
uniform sampler2D uTexture;
varying vec2 vTexCoordinate;
void main() {
    vec2 change;
    change.x = vTexCoordinate.x ;
    change.y = vTexCoordinate.y ;
    gl_FragColor = texture2D(uTexture, change);
}
