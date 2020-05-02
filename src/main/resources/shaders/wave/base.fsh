/* Bump mapping shader by daxnitro.  
   This shader implements parallax occlusion and specular mapping.  It requires terrain_nh.png and terrain_s.png to be included in the current texture pack. */

// To disable a particular feature of this shader, add two forward slashes to the beginning its line:

#define ENABLE_PARALLAX_OCCLUSION
//#define ENABLE_SPECULAR_MAPPING
#define ENABLE_FOG

#define INTESTINE
#define SOULSAND

/*
For example, to disable fog change the line above to this:

//#define ENABLE_FOG
*/

/* Sometimes textures are disabled. Two copies of this shader will be compiled.  One with ENABLE_GL_TEXTURE_2D and one without.
   A similar thing will happen for bump mapping.
   Don't mess with these next few lines. */
#ifdef _ENABLE_GL_TEXTURE_2D
uniform sampler2D sampler0;
centroid varying vec4 texCoord;
#ifdef _ENABLE_BUMP_MAPPING

/* Here, intervalMult might need to be tweaked per texture pack.  
   The first two numbers determine how many samples are taken per fragment.  They should always be the equal to eachother.
   The third number divided by one of the first two numbers is inversely proportional to the range of the height-map. */

//const vec3 intervalMult = vec3(0.00048828125, 0.00048828125, 0.145); // Fine for Misa's
//const vec3 intervalMult = vec3(0.0039, 0.0039, 4.5); // Fine for 16x16 tile size
//const vec3 intervalMult = vec3(0.0019, 0.0019, 0.5); // Fine for 32x32 tile size
const vec3 intervalMult = vec3(0.0009, 0.0009, 0.25); // Fine for 64x64 tile size
//const vec3 intervalMult = vec3(0.00048828125, 0.00048828125, 0.2); // Fine for 128x128 tile size
//const vec3 intervalMult = vec3(0.00008828125, 0.00008828125, 0.03); // Fine for 256x256 tile size

// !!!!!!!! THE TYPICAL USER DOESN'T NEED TO LOOK AT ANYTHING BELOW HERE !!!!!!!!

#version 130 // This will always get moved to the top of the code in pre-processing.

uniform sampler2D sampler1;
uniform sampler2D sampler2;

varying vec4 specMultiplier;

varying float useCelestialSpecularMapping;

varying vec3 lightVector;
varying vec3 viewVector;

const float MAX_DISTANCE = 100.0;
const float SQUIRM_DISTANCE = 30.0;

const int MAX_POINTS = 50;

#endif // ENABLE_GL_BUMP_MAPPING
#endif // ENABLE_GL_TEXTURE_2D

struct lightSource {
	int itemId;
	float magnitude;
	vec4 specular;
};

uniform lightSource heldLight;

#ifdef _ENABLE_BUMP_MAPPING
varying float distance;
#endif

const int GL_LINEAR = 9729;
const int GL_EXP = 2048;

uniform int fogMode;

varying vec4 vertColor;

const float PI = 3.1415926535897932384626433832795;
const float PI2 = 6.283185307179586476925286766559;

uniform float near;
uniform float far;

uniform int worldTime;
uniform int renderType;
varying float texID;
varying vec3 normal;

void main() {

#ifndef _ENABLE_GL_TEXTURE_2D

	gl_FragColor = vertColor;

#elif defined(_ENABLE_BUMP_MAPPING) // _ENABLE_GL_TEXTURE_2D

	if (distance <= MAX_DISTANCE && viewVector.z < 0.0) {
		
		vec3 coord = vec3(texCoord.st, 1.0);

#ifdef INTESTINE
		if (round(texID) == 103.0 && distance <= SQUIRM_DISTANCE) {
			float t = float(mod(worldTime, 500))/500.0;
			vec3 offset, base;
			coord = modf(16.0*coord, base);
			offset = vec3(cos(PI2*coord.s)*cos(PI2*(coord.t + 2.0*t))*cos(PI2*t)/40.0,
                     -cos(PI2*(coord.s + t))*sin(2.0*PI2*coord.t)/40.0,0);

			coord = mod(coord + offset, vec3(1.0)) + base;
			coord = coord/16;
//			gl_FragColor = texture2D(sampler0, coordsq.st/16.0) * vertColor;
		}
#endif

#ifdef SOULSAND
		if (round(texID) == 104.0 && distance <= SQUIRM_DISTANCE) {
			float t = float(mod(worldTime, 2000))/2000.0;
			vec3 offset, base;
			coord = modf(16.0*coord, base);
			offset = vec3(cos(PI2*coord.s)*cos(PI2*(coord.t + 2.0*t))*cos(PI2*t)/24.0,
                     -cos(PI2*(coord.s + t))*sin(2.0*PI2*coord.t)/32.0,0);

			coord = mod(coord + offset, vec3(1.0)) + base;
			coord = coord/16;
//			gl_FragColor = texture2D(sampler0, coordsq.st/16.0) * vertColor;
		}
#endif  
		
#ifdef ENABLE_PARALLAX_OCCLUSION
		if (texture2D(sampler1, coord.st).a < 1.0) {
			vec2 minCoord = vec2(texCoord.s - mod(texCoord.s, 0.0625), texCoord.t - mod(texCoord.t, 0.0625));
			vec2 maxCoord = vec2(minCoord.s + 0.0625, minCoord.t + 0.0625);
		
			vec3 interval = viewVector * intervalMult; 
		
			for (int loopCount = 0; texture2D(sampler1, coord.st).a < coord.z && loopCount < MAX_POINTS; ++loopCount) {
				coord += interval;
				if (coord.s < minCoord.s) {
					coord.s += 0.0625;
				} else if (coord.s >= maxCoord.s) {
					coord.s -= 0.0625;
				}
				if (coord.t < minCoord.t) {
					coord.t += 0.0625;
				} else if (coord.t >= maxCoord.t) {
					coord.t -= 0.0625;
				}
			}
		}
#endif

		gl_FragColor = texture2D(sampler0, coord.st) * vertColor;

#ifdef ENABLE_SPECULAR_MAPPING
		vec3 bump = texture2D(sampler1, coord.st).xyz * 2.0 - 1.0;
		vec4 specular = texture2D(sampler2, coord.st);
		if (useCelestialSpecularMapping > 0.5) {
			float s = max(dot(reflect(-lightVector, bump), viewVector), 0.0);
			gl_FragColor = min(gl_FragColor + specular * s * s * s * specMultiplier, 1.0);
		}
		float intensity = 1.0 - min(distance / heldLight.magnitude, 1.0);
		float s = max(dot(reflect(-viewVector, bump), viewVector), 0.0);
		gl_FragColor = min(gl_FragColor + intensity * specular * s * s * s * heldLight.specular, 1.0);
		
#endif
			
	} else {

		gl_FragColor = texture2D(sampler0, texCoord.st) * vertColor;

	}

#else  // _ENABLE_GL_TEXTURE_2D

	gl_FragColor = texture2D(sampler0, texCoord.st) * vertColor;

#endif  // _ENABLE_GL_TEXTURE_2D

#ifdef ENABLE_FOG
	if (fogMode == GL_EXP) {
		gl_FragColor.rgb = mix(gl_FragColor.rgb, gl_Fog.color.rgb, 1.0 - clamp(exp(-gl_Fog.density * gl_FogFragCoord), 0.0, 1.0));
	} else if (fogMode == GL_LINEAR) {
		gl_FragColor.rgb = mix(gl_FragColor.rgb, gl_Fog.color.rgb, clamp((gl_FogFragCoord - gl_Fog.start) * gl_Fog.scale, 0.0, 1.0));
	}
#endif
	
}
