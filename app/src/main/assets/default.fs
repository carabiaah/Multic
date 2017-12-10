precision mediump float;

varying vec2 v_texCoord;

uniform sampler2D u_texture;
uniform int u_hasTexture;
uniform vec4 u_color;

void main() {
    if(u_hasTexture == 1) {
        gl_FragColor = texture2D(u_texture, v_texCoord);
    } else {
        gl_FragColor = u_color;
    }
}