#version 330 core

layout (location = 0) in vec3 v_position;
layout (location = 1) in vec2 v_texture;
layout (location = 2) in vec3 v_normal;

out vec2 fs_texture;
out vec3 fs_normal;
out vec3 fs_fragment_position;

uniform mat4 projection;
uniform mat4 model;
uniform mat4 view;

void main()
{
  fs_texture = v_texture;
  fs_normal = normalize(mat3(transpose(inverse(model))) * v_normal);
  fs_fragment_position = vec3(model * vec4(v_position, 1.0));

  gl_Position = projection * view * model * vec4(v_position, 1.0);
}