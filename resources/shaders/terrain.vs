#version 330 core

layout (location = 0) in vec3 v_position;

out vec3 gs_fragment_position;

uniform mat4 projection;
uniform mat4 model;
uniform mat4 view;

void main()
{
  gs_fragment_position = vec3(model * vec4(v_position, 1.0));
  gl_Position = vec4(v_position, 1.0);
}