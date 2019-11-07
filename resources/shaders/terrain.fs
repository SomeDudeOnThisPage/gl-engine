#version 330 core
#define LIGHTS 8

in vec3 fs_normal;
in vec3 fs_fragment_position;

const vec3 light_position = vec3(-250, 250, 450);
const vec3 light_ambient = vec3(0.25, 0.25, 0.25);
const vec3 light_color = vec3(1.0, 1.0, 1.0);

uniform vec3 col;

void main()
{
  // diffuse lighting
  vec3 light_direction = normalize(light_position - fs_fragment_position);
  vec3 diffuse = max(dot(normalize(fs_normal), light_direction), 0.0f) * light_color;
  diffuse = clamp(diffuse, 0.0, 1.0);

  gl_FragColor = vec4(col, 1.0f);//
}