#version 330 core

layout (triangles) in;
layout (triangle_strip, max_vertices = 3) out;

in vec3 gs_fragment_position[3];

out vec3 fs_normal;
out vec3 fs_fragment_position;

uniform mat4 projection;
uniform mat4 model;
uniform mat4 view;

uniform vec4 clip;

vec3 surface_normal()
{
  vec3 tangent1 = gl_in[1].gl_Position.xyz - gl_in[0].gl_Position.xyz;
  vec3 tangent2 = gl_in[2].gl_Position.xyz - gl_in[0].gl_Position.xyz;
  vec3 normal = cross(tangent1, tangent2);

  return normalize(normal);
}

void main(void)
{
  vec3 normal = surface_normal();

  for(int i = 0; i < 3; i++)
  {
    fs_normal = normal;
    fs_fragment_position = gs_fragment_position[i];

    gl_Position = projection * view * model * gl_in[i].gl_Position;
    // moved clipping to geometry shader
    gl_ClipDistance[0] = dot(vec4(gs_fragment_position[i], 1.0), clip);
    EmitVertex();
  }

  EndPrimitive();
}