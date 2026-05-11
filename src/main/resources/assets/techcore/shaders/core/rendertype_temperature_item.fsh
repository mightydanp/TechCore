uniform sampler2D Sampler0;
uniform vec4 ColorModulator;

in vec4 vertexColor;
in vec2 texCoord0;

out vec4 fragColor;

void main() {
    vec4 tex = texture(Sampler0, texCoord0);

    if (tex.a < 0.01) {
        discard;
    }

    float gray = dot(tex.rgb, vec3(0.299, 0.587, 0.114));

    vec3 temperatureColor = vertexColor.rgb;
    float strength = vertexColor.a;

    vec3 result = vec3(gray) * temperatureColor;

    fragColor = vec4(result, tex.a * strength) * ColorModulator;
}