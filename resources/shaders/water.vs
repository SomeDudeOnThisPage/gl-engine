#version 330 core

layout (location = 0) in vec3 v_position;
layout (location = 1) in vec3 v_normal;

out vec4 fs_texture_cs;
out vec3 fs_camera_vector;

uniform mat4 projection;
uniform mat4 model;
uniform mat4 view;

uniform vec3 view_position;

void main()
{
  vec4 position = projection * view * model * vec4(v_position, 1.0);

  fs_camera_vector = normalize(position.xyz - view_position);

  fs_texture_cs = position;
  gl_Position = position;
}