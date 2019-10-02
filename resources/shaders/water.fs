#version 330 core
#define LIGHTS 8

in vec4 fs_texture_cs;
in vec3 fs_camera_vector;

const vec3 water_color = vec3(0.404, 0.567, 0.951);
const vec3 fs_normal = vec3(0.0f, 1.0f, 0.0f);
const float fresnel_intensity = 2.5f;

uniform sampler2D refraction;
uniform sampler2D reflection;
uniform sampler2D depth_map;

float fresnel()
{
  vec3 view = fs_camera_vector;
  vec3 normal = normalize(fs_normal);
  float factor = dot(view, normal);
  factor = pow(factor, fresnel_intensity);
  return clamp(factor, 0.0, 1.0);
}

vec2 clipSpaceToTexCoords(vec4 clipSpace){
  vec2 ndc = (clipSpace.xy / clipSpace.w);
  vec2 texCoords = ndc / 2.0 + 0.5;
  return texCoords;
}

void main()
{
  vec2 refraction_texture = clipSpaceToTexCoords(fs_texture_cs);
  vec2 reflection_texture = vec2(refraction_texture.x, 1.0 - refraction_texture.y);

  float depth = texture(depth_map, refraction_texture).r;
  float near = 0.1;
  float far = 100.0f;
  float depth_distance = 2.0 * near * far / (far + near - (2.0 * depth - 1.0) * (far - near));

  depth = gl_FragCoord.z;
  float water_distance = 2.0 * near * far / (far + near - (2.0 * depth - 1.0) * (far - near));
  float water_depth = depth_distance - water_distance;

  vec3 reflect_t = mix(texture(reflection, reflection_texture).xyz, water_color, 0.9f);
  vec3 refract_t = texture(refraction, refraction_texture).xyz;

  vec3 wc = mix(reflect_t, refract_t, fresnel());

  gl_FragColor = vec4(wc, clamp(water_depth * 1.25, 0.0, 1.0));
}