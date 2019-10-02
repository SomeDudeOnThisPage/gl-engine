#version 330 core

in vec3 v_position;
in vec2 texture;
in vec3 char_position;
in vec2 texture_position;

out vec2 fs_texture_coordinates;
out vec2 fs_texture_position;

uniform mat4 pr_matrix;
uniform float window_w;
uniform float window_h;
uniform float scale;

uniform float position_x;
uniform float position_y;

void main()
{
  // Assuming bitmap fonts are always 16x16 characters
  int atlas_size = 16;
  float worldx;
  float worldy;

  // If the position is positive, draw at offset from left, if negative, draw with offset from right
  if (position_x > 0) { worldx = -window_w + char_position.x + position_x; }
  else { worldx = window_w + char_position.x + position_x; }
  if (position_y > 0) { worldy = window_h - char_position.y - position_y; }
  else { worldy = -window_h + char_position.y + position_y; }

  mat4 pos_matrix = mat4(
    vec4(scale, 0, 0, 0),
    vec4(0, scale, 0, 0),
    vec4(0, 0, scale, 0),
    vec4(worldx, worldy, 1.0, 1.0)
  );

  fs_texture_coordinates = texture / atlas_size + texture_position;
  fs_texture_position = texture_position;

  gl_Position = pr_matrix * pos_matrix * vec4(v_position.x, v_position.y, v_position.z, 1.0);
}