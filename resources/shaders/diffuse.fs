#version 330 core
#define LIGHTS 8

in vec2 fs_texture;
in vec3 fs_normal;
in vec3 fs_fragment_position;

// uniform struct definitions
uniform struct { vec3 ambient; vec3 diffuse; vec3 specular; float shininess; } material;
uniform struct { vec3 color; } light[LIGHTS];
uniform vec3 view_position;

// texture maps
uniform sampler2D texture0; // diffuse

// (temporarily) static light data
const vec3 light_position = vec3(-250, 250, 450);
const vec3 light_ambient = vec3(0.05, 0.05, 0.05);
const vec3 light_color = vec3(1.0, 1.0, 1.0);

void main()
{
  // ambient lighting
  vec3 ambient = material.ambient * light_ambient;

  // diffuse lighting
  vec3 light_direction = normalize(light_position - fs_fragment_position);
  vec3 diffuse = max(dot(fs_normal, light_direction), 0.0f) * material.diffuse * light_color;
  diffuse = clamp(diffuse, 0.0, 1.0);

  // specular lighting
  vec3 specular = vec3(0.0f);
  if (dot(fs_normal, normalize(light_position)) > 0.0f) // only create specular highlights when our surface is actually lit
  {
    vec3 view_direction = normalize(view_position - fs_fragment_position);
    vec3 reflect_direction = reflect(light_direction, fs_normal);
    specular = pow(max(dot(view_direction, reflect_direction), 0.0f), material.shininess * 128.0f) * material.specular * light_color;
  }

  gl_FragColor = vec4(diffuse + specular + ambient, 1.0) * texture2D(texture0, fs_texture);
}