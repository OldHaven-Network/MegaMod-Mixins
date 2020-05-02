#version 120

uniform sampler2D colorMap;
varying vec4 texcoord;

void main ()
{
    gl_FragColor = texture2D(colorMap,texcoord.rg);
}