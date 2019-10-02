#version 330 core

in vec2 fs_texture_coordinates;
in vec2 fs_texture_position;

uniform sampler2D tex;

void main()
{
  vec4 character = texture2D(tex, fs_texture_coordinates);
  vec4 color = vec4(1.0, 1.0, 0.0, 1.0);
  gl_FragColor = mix(character, color, character.a) + vec4(0.3, 0.0, 0.0, 0.2);//vec4(1.0f, 0.0f, 0.0f, 1.0f);//
}