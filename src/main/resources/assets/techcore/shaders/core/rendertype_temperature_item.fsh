#version 150

#moj_import <light.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler2;

in vec4 vertexColor;
in vec2 texCoord0;
flat in ivec2 lightMapUV;

out vec4 fragColor;

void main() {
    vec4 tex = texture(Sampler0, texCoord0);

    if (tex.a < 0.10) {
        discard;
    }

    float gray = dot(tex.rgb, vec3(0.299, 0.587, 0.114));

    vec3 heatColor = vertexColor.rgb;
    float strength = vertexColor.a;

    float hotBoost = smoothstep(0.45, 0.74, strength);

    // Low and mid heat preserve texture strongly.
    // High heat gets brighter, but still keeps some pixel shape.
    float lowDetail = gray * 0.80 + 0.20;
    float hotDetail = gray * 0.35 + 0.65;
    float detail = mix(lowDetail, hotDetail, hotBoost);

    vec3 overlay = heatColor * detail;

    vec4 lightColor = minecraft_sample_lightmap(Sampler2, lightMapUV);

    // Keep lighting, but let white-hot stay visibly hot.
    vec3 lighting = mix(lightColor.rgb, vec3(1.0), hotBoost * 0.55);
    overlay *= lighting;

    // Small contrast-safe brightness boost.
    overlay *= 1.0 + hotBoost * 0.25;
    overlay = min(overlay, vec3(1.0));

    fragColor = vec4(overlay, tex.a * strength);
}