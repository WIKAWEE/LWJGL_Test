#version 450

uniform vec3 baseColor;

out vec4 fragColor;


void main(){
    fragColor = vec4(baseColor, 0.5);
}