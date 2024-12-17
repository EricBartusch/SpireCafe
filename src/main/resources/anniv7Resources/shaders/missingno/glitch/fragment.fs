//	Glitch Effect Shader by Yui Kinomoto @arlez80

//	MIT License
varying vec2 v_texCoord;

uniform vec2 u_screenSize;
uniform sampler2D u_texture;
uniform float u_time;
uniform float u_shake_power;
uniform float u_shake_rate;
uniform float u_shake_speed;
uniform float u_shake_block_size;
uniform float u_shake_color_rate;

float random(float seed) {
    return fract(543.2543 * sin(dot(vec2(seed, seed), vec2(3525.46, -54.3415))));
}

void main() {
    float enable_shift = float(random(floor(u_time * u_shake_speed)) < u_shake_rate);
    vec2 fixed_uv = gl_FragCoord.xy / u_screenSize.xy;

    fixed_uv.x += (random(fixed_uv.y * u_shake_block_size + u_time) - 0.5) * u_shake_power * enable_shift;
    fixed_uv.y += (random(fixed_uv.x * u_shake_block_size + u_time) - 0.5) * u_shake_power * enable_shift;

    vec4 pixel_color = texture2D(u_texture, fixed_uv);
    pixel_color.r = mix(pixel_color.r, texture2D(u_texture, fixed_uv + vec2(u_shake_color_rate, 0.0)).r, enable_shift);
    pixel_color.b = mix(pixel_color.b, texture2D(u_texture, fixed_uv + vec2(-u_shake_color_rate, 0.0)).b, enable_shift);

    gl_FragColor = pixel_color;
}

