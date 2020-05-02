//for water transparency modification

varying vec4 texcoord;
varying vec4 vert_color;
varying float is_vertical;
varying float fogFactor;

void main()
{
    texcoord = gl_MultiTexCoord0;
    vert_color = gl_Color;
    is_vertical = gl_Normal.y;
    
    vec4 vertex = gl_ModelViewMatrix * gl_Vertex;
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
	
	//try linear
	fogFactor = (gl_Fog.end - gl_Position.z) / (gl_Fog.end - gl_Fog.start);
	fogFactor = clamp(fogFactor, 0.0, 1.0);
}