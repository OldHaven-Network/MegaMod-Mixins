varying vec4 texcoord; 
varying vec4 position;

void main()
{
    texcoord = gl_MultiTexCoord0;
    position = gl_Vertex;
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}