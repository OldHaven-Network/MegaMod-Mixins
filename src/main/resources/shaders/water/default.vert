#version 130

varying vec4 texcoord; 
vec4 gl_ClipVertex;

out float gl_ClipDistance[1];

void main()
{
    texcoord = gl_MultiTexCoord0;
    // Transforming The Vertex
    
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    
    vec3 screen_pos = gl_Position.xyz / gl_Position.w;
    gl_ClipDistance[0] = screen_pos.x + 0.5; //required for clip planes in shader
    
}