attribute vec3 a_position;
attribute vec2 a_texCoord;

varying vec2 v_texCoord;

uniform mat4 u_projMatrix;
uniform mat4 u_viewMatrix;
uniform mat4 u_modelMatrix;

void main() {
    gl_Position = u_projMatrix * u_viewMatrix * u_modelMatrix * vec4(a_position, 1.0);

    v_texCoord = a_texCoord;
}
