//#version 120

varying vec4 texcoord;
//varying vec4 position; //position is equal to texture coordinate :/ (one image plane)
//vec4 gl_ClipVertex;

void main()
{
    texcoord = gl_MultiTexCoord0;
    // Transforming The Vertex
    
    //position = gl_Vertex;
    
    gl_Position = gl_ProjectionMatrix * gl_Vertex;
    
    //clipping test
    //vec3 screen_pos = gl_Position.xyz / gl_Position.w;
    //gl_ClipDistance[0] = screen_pos.x + 0.5; //required for clip planes in shader
    
}