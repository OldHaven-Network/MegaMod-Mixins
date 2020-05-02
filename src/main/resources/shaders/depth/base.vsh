// Shader that curves the Minecraft world by daxnitro.

#version 120 // This will always get moved to the top of the code in pre-processing.

const float WORLD_RADIUS         = 2500.0;
const float WORLD_RADIUS_SQUARED = 6250000.0;

#ifdef _ENABLE_GL_TEXTURE_2D
centroid varying vec4 texCoord;
#endif

varying vec4 vertColor;

void main() {
	vec4 position = gl_ModelViewMatrix * gl_Vertex;
	
	if (gl_Color.a != 0.8) {
		// Not a cloud.
		
		float distanceSquared = position.x * position.x + position.z * position.z;
		
		position.y -= WORLD_RADIUS - sqrt(max(1.0 - distanceSquared / WORLD_RADIUS_SQUARED, 0.0)) * WORLD_RADIUS;
	}
	
	gl_Position = gl_ProjectionMatrix * position;

#ifdef _ENABLE_GL_TEXTURE_2D
	texCoord = gl_MultiTexCoord0;
#endif
		
	vertColor = gl_Color;
	
	gl_FogFragCoord = gl_Position.z;
}

