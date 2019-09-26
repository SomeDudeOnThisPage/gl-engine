#version 330 core
#define LIGHTS 8

in vec3 fs_normal;
in vec3 fs_fragment_position;

const vec4 water_color = vec4(0.2, 0.4, 0.7, 0.3);

const vec3 light_position = vec3(-250, 250, 450);
const vec3 light_ambient = vec3(0.15, 0.15, 0.15);
const vec3 light_color = vec3(1.0, 1.0, 1.0);

void main()
{
  // diffuse lighting
  vec3 light_direction = normalize(light_position - fs_fragment_position);
  vec3 diffuse = max(dot(fs_normal, light_direction), 0.0f) * light_color;
  diffuse = clamp(diffuse, 0.0, 1.0);

  gl_FragColor = vec4(light_ambient + diffuse, 1.0f) * water_color;
}