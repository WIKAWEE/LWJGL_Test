#version 450

layout (location = 0) in vec3 position;
uniform vec3 xtrans;
mat4 

void main(){
    gl_Position = vec4(position, 1.0);
}