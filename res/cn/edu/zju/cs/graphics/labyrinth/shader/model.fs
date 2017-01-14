uniform vec3 uLightPosition;
uniform vec3 uViewPosition;
uniform vec3 uAmbientColor;
uniform vec3 uDiffuseColor;
uniform vec3 uSpecularColor;
//uniform sampler2D uTexture;
varying vec3 vPosition;
varying vec3 vNormal;
//varying vec2 vTextureCoordinate;

void main() {
    //gl_FragColor = texture2D(uTexture, vec2(vTextureCoordinate.x, 1.0 - vTextureCoordinate.y))
    float ambientStrength = 0.7;
    float diffuseStrength = 0.3;
    float specularStrength = 0.3;
    float shininess = 2.0;
    vec3 ambientColor = ambientStrength * uAmbientColor;
    vec3 normal = normalize(vNormal);
    vec3 lightDirection = normalize(uLightPosition - vPosition);
    vec3 diffuseColor = diffuseStrength * max(0.0, dot(normal, lightDirection)) * uDiffuseColor;
    vec3 viewDirection = normalize(uViewPosition - vPosition);
    vec3 reflectDirection = reflect(-lightDirection, normal);
    vec3 specularColor = specularStrength
            * pow(max(dot(viewDirection, reflectDirection), 0.0), shininess) * uSpecularColor;
    gl_FragColor = vec4(ambientColor + diffuseColor + specularColor, 1.0);
}
