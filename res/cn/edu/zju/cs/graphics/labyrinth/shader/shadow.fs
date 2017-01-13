uniform sampler2D uTexture;
uniform sampler2D uShadowMap;
varying vec2 vTextureCoordinate;
varying vec4 vLightPosition;

float texture2DCompare(sampler2D depths, vec2 uv, float compare){
    float depth = texture2D(depths, uv).r;
    return step(compare, depth);
}

float texture2DShadowLerp(sampler2D depths, vec2 size, vec2 uv, float compare){
    vec2 texelSize = vec2(1.0) / size;
    vec2 f = fract(uv * size + 0.5);
    vec2 centroidUV = floor(uv * size + 0.5) / size;
    float lb = texture2DCompare(depths, centroidUV + texelSize * vec2(0.0, 0.0), compare);
    float lt = texture2DCompare(depths, centroidUV + texelSize * vec2(0.0, 1.0), compare);
    float rb = texture2DCompare(depths, centroidUV + texelSize * vec2(1.0, 0.0), compare);
    float rt = texture2DCompare(depths, centroidUV + texelSize * vec2(1.0, 1.0), compare);
    float a = mix(lb, lt, f.y);
    float b = mix(rb, rt, f.y);
    float c = mix(a, b, f.x);
    return c;
}

float pcf_lerp(sampler2D depths, vec2 size, vec2 uv, float compare){
    float result = 0.0;
    for (int x = -1; x <= 1; ++x){
        for (int y = -1; y <= 1; ++y){
            vec2 off = vec2(x, y) / size;
            result += texture2DShadowLerp(depths, size, uv + off, compare);
        }
    }
    return result / 9.0;
}

float pcf(sampler2D shadowMap, vec2 shadowMapSize, vec3 coordinates) {
    float bias = 0.001;
    float shadow = 0.0;
    //vec2 texelSize = 1.0 / textureSize(shadowMap, 0);
    vec2 texelSize = 1.0 / shadowMapSize;
    //for(int x = -1; x <= 1; ++x) {
        //for(int y = -1; y <= 1; ++y) {
    // FIXME: Hard-coded 20 for the thickness of wall. If we could sample on a circle it would be
    // better.
    for(int x = -20; x <= 20; x += 2) {
        for(int y = -20; y <= 20; y += 2) {
            float shadowDepth = texture2D(shadowMap, coordinates.xy + vec2(x, y) * texelSize).r;
            shadow += coordinates.z - bias > shadowDepth ? 1.0 : 0.0;
        }
    }
    //shadow /= 3.0 * 3.0;
    shadow /= 21.0 * 21.0;
    // FIXME: For wall shadow.
    shadow *= 0.5;
    return shadow;
}

float calculateShadow(sampler2D shadowMap, vec4 lightPosition) {

    vec3 position = lightPosition.xyz / lightPosition.w;
    vec3 coordinates = 0.5 + 0.5 * position;

    //float shadowDepth = texture2D(shadowMap, coordinates.xy).r;
    //float depth = coordinates.z;
    //return depth > shadowDepth ? 0.5 : 0.0;

    // FIXME: Hard-coded shadowMapSize.
    vec2 shadowMapSize = vec2(1024.0, 1024.0);
    //float bias = 0.001;
    //float shadow = 1.0 - pcf_lerp(shadowMap, shadowMapSize, coordinates.xy, depth - bias);
    float shadow = pcf(shadowMap, shadowMapSize, coordinates);
    return shadow;
}

void main() {
    vec4 color = texture2D(uTexture, vec2(vTextureCoordinate.x, 1.0 - vTextureCoordinate.y));
    float shadow = calculateShadow(uShadowMap, vLightPosition);
    gl_FragColor = vec4((1.0 - shadow) * color.rgb, color.a);
}
