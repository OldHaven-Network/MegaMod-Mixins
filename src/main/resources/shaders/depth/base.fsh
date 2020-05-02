/* Do-nothing shader by daxnitro.  
   This shader emulates the fixed pipeline. */

// To disable a fog, add two forward slashes to the beginning its line:

#define ENABLE_FOG

// !!!!!!!! THE TYPICAL USER DOESN'T NEED TO LOOK AT ANYTHING BELOW HERE !!!!!!!!

#version 120 // This will always get moved to the top of the code in pre-processing.

/* Sometimes textures are disabled. Two copies of this shader will be compiled.  One with ENABLE_GL_TEXTURE_2D and one without.
   A similar thing will happen for bump mapping.
   Don't mess with these next few lines. */
#ifdef _ENABLE_GL_TEXTURE_2D
uniform sampler2D sampler0;
centroid varying vec4 texCoord;
#endif

const int GL_LINEAR = 9729;
const int GL_EXP = 2048;

uniform int fogMode;

varying vec4 vertColor;

void main() {

#ifndef _ENABLE_GL_TEXTURE_2D

	gl_FragColor = vertColor;

#else

	gl_FragColor = texture2D(sampler0, texCoord.st) * vertColor;

#endif

#ifdef ENABLE_FOG
	if (fogMode == GL_EXP) {
		gl_FragColor.rgb = mix(gl_FragColor.rgb, gl_Fog.color.rgb, 1.0 - clamp(exp(-gl_Fog.density * gl_FogFragCoord), 0.0, 1.0));
	} else if (fogMode == GL_LINEAR) {
		gl_FragColor.rgb = mix(gl_FragColor.rgb, gl_Fog.color.rgb, clamp((gl_FogFragCoord - gl_Fog.start) * gl_Fog.scale, 0.0, 1.0));
	}
#endif
	
}