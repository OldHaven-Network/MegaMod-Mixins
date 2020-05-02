/* Bump mapping shader by daxnitro. 
   This shader implements parallax occlusion and specular mapping.  It requires terrain_nh.png and terrain_s.png to be included in the current texture pack. */

// Comment => disable effect
// Uncomment => enable effect
#define ENABLE_WORLD_CURVATURE
//#define ENABLE_ACID_EFFECT     // Need to enable ENABLE_WORLD_CURVATURE to make it work
//#define ENABLE_INCEPTION     // Need to enable ENABLE_WORLD_CURVATURE to make it work
#define WAVING_WHEAT
#define WAVING_PLANTS
#define WAVING_LEAVES
#define SHORT_GRASS

//WAVE SETTINGS
#define WAVING_WATER
//#define WAVING_LAVA
const float WAVE_PITCH = 10.0; //Decrease to grow wave effect

//BFINDER SETTINGS
#define BFINDER_RED 1.0
#define BFINDER_GREEN 3.0
#define BFINDER_BLUE 4.0


const float BEND_AMOUNT         = 0.003;

// !!!!!!!! THE TYPICAL USER DOESN'T NEED TO LOOK AT ANYTHING BELOW HERE !!!!!!!!

#version 130 // This will always get moved to the top of the code in pre-processing.

attribute vec4 mc_Entity;
varying vec4 vertColor;

#ifdef _ENABLE_GL_TEXTURE_2D
centroid varying vec4 texCoord;
#ifdef _ENABLE_BUMP_MAPPING

varying vec3 viewVector;
varying vec3 lightVector;

uniform vec3 sunPosition;
uniform vec3 moonPosition;

varying vec4 specMultiplier;


varying float distance;
#endif
#endif

#ifdef ENABLE_WORLD_CURVATURE
   const float WORLD_RADIUS         = 1272.0; //2500.0;
   const float WORLD_RADIUS_SQUARED = 1617984.0; //6250000.0;
#endif

int getTextureID(vec2 coord) {
    int i = int(floor(16*coord.s));
    int j = int(floor(16*coord.t));
    return i + 16*j;
}

uniform int worldTime;
uniform int renderType;

varying float texID;

varying float useCelestialSpecularMapping;

const float PI = 3.1415926535897932384626433832795;
const float PI2 = 6.283185307179586476925286766559;

void main() {
   vec4 position = gl_Vertex;
   int tex = getTextureID(gl_MultiTexCoord0.st);

#ifdef WAVING_WHEAT
    if (87 < tex && tex < 96 && renderType != 0) {
        float t = mod(float(worldTime), 200.0)/200.0;
        vec2 pos = position.xz/16.0;
        if (floor((16.0*gl_MultiTexCoord0.t)+0.5) <= floor(16.0*gl_MultiTexCoord0.t)) {
            position.x += (sin(PI2*(2.0*pos.x + pos.y - 3.0*t)) + 0.6)/20.0;
        }
    }
#endif

#ifdef WAVING_WATER
   if ( ((tex >= 204 && tex <= 207) || (tex >= 221 && tex <= 223)) && renderType == 0) {
        float t1 = float(mod(worldTime, 1000))/400.0;
        vec2 pos1 = position.xz/16.0;
            position.y += (cos((PI*2.0)*(2.0 * (pos1.x + pos1.y) + PI * t1)) + 0.2)/WAVE_PITCH;
    }
#endif

#ifdef WAVING_LAVA
   if ( ((tex >= 236 && tex <= 239) || (tex >= 253 && tex <= 255)) && renderType == 0) {
        float t1 = float(mod(worldTime, 1000))/400.0;
        vec2 pos1 = position.xz/16.0;
            position.y += (cos((PI*2.0)*(2.0 * (pos1.x + pos1.y) + PI * t1)) + 0.2)/WAVE_PITCH;
    }
#endif

#ifdef WAVING_LEAVES
if ((tex == 52 || tex == 53 || tex == 132 || tex == 133)&& renderType == 1)  {
        float t = mod(float(worldTime), 800.0)/800.0;
        vec2 pos = position.xz/16.0;
        if (floor(8.0*gl_MultiTexCoord0.t+0.5) <= floor(16.32*gl_MultiTexCoord0.t)) {
            position.x -= (sin(PI2*(2.0*pos.x + pos.y - 3.0*t)) + 0.6)/24.0;
            position.y -= (sin(PI2*(3.0*pos.x + pos.y - 4.0*t)) + 1.2)/32.0;
            position.z -= (sin(PI2*(1.0*pos.x + pos.y - 1.5*t)) + 0.3)/8.0;
        }
    }
#endif

#ifdef TALL_GRASS
    if ((tex == 109 || tex == 110 || tex == 111) && renderType == 1) {
        float t = float(mod(worldTime, 300))/300.0;
        vec2 pos = position.xz/16.0;
        if (round(16.0*gl_MultiTexCoord0.t) <= floor(16.0*gl_MultiTexCoord0.t)) {
            position.x -= (sin(PI2*(2.0*pos.x + pos.y - 3.0*t)) + 0.6)/12.0;
        }
    }
#endif

#ifdef SHORT_GRASS
    if (((87 < tex && tex < 93) || tex == 12  || tex == 13  || tex == 15) && renderType == 1) {
        float t = float(mod(worldTime, 200))/200.0;
        vec2 pos = position.xz/16.0;
        if (round(16.0*gl_MultiTexCoord0.t) <= floor(16.0*gl_MultiTexCoord0.t)) {
            position.x -= (sin(PI2*(2.0*pos.x + pos.y - 3.0*t)) + 0.6)/32.0;
        }
    }
#endif

#ifdef WAVING_PLANTS
if ((tex == 12 || tex == 13 || tex == 15 || tex == 28 || tex == 29 || tex == 39 || tex == 56 || tex == 63 || tex == 79) && renderType == 1)  {
        float t = mod(float(worldTime), 500.0)/500.0;
        vec2 pos = position.xz/16.0;
        if ( floor((16.0*gl_MultiTexCoord0.t)+0.5) <= floor(16.0*gl_MultiTexCoord0.t) ) {
            position.x -= (sin(PI2*(2.0*pos.x + pos.y - 3.0*t)) + 0.6)/8.0;
        }
    }
#endif

#ifdef ENABLE_WORLD_CURVATURE
    position = gl_ModelViewMatrix * position;
    if (gl_Color.a != 0.8) {
        // Not a cloud.
        float flatDistanceSquared = position.x * position.x + position.z * position.z;
#ifdef ENABLE_INCEPTION
    mat4 unrotate = gl_ModelViewMatrix;
    unrotate[3] = vec4(0.0, 0.0, 0.0, 1.0);
    mat4 rotate = gl_ModelViewMatrixInverse;
    rotate[3] = vec4(0.0, 0.0, 0.0, 1.0);
        position = position * unrotate;
        position.y += flatDistanceSquared * BEND_AMOUNT;
        position = position * rotate;
//    gl_Position = gl_ProjectionMatrix * position;
//    gl_ClipVertex = gl_Position;
#endif
#ifdef ENABLE_ACID_EFFECT
        position.y += 5*sin(flatDistanceSquared*sin(float(worldTime)/143.0)/1000);
        float y = position.y;
        float x = position.x;
        float om = sin(flatDistanceSquared*sin(float(worldTime)/256.0)/5000) * sin(float(worldTime)/200.0);
        position.y = x*sin(om)+y*cos(om);
        position.x = x*cos(om)-y*sin(om);
#else
        position.y -= WORLD_RADIUS - sqrt(max(1.0 - flatDistanceSquared / WORLD_RADIUS_SQUARED, 0.0)) * WORLD_RADIUS;
#endif
#ifdef _ENABLE_BUMP_MAPPING
      distance = sqrt(flatDistanceSquared + position.y * position.y);
#endif
   }
   gl_Position = gl_ProjectionMatrix * position;
   gl_ClipVertex = gl_Position;

#elif defined(_ENABLE_BUMP_MAPPING)
   vec4 gposition = gl_ModelViewMatrix * gl_Vertex;
   distance = sqrt(gposition.x * gposition.x + gposition.y * gposition.y + gposition.z * gposition.z);
   gl_Position = gl_ProjectionMatrix * gposition;
#else
   gl_Position = ftransform();
#endif // ENABLE_WORLD_CURVATURE

   vertColor = gl_Color;

#ifdef _ENABLE_GL_TEXTURE_2D
   texCoord = gl_MultiTexCoord0;
#ifdef _ENABLE_BUMP_MAPPING

   vec3 normal = normalize(gl_NormalMatrix * gl_Normal);
   vec3 tangent;
   vec3 binormal;
   
   useCelestialSpecularMapping = 1.0;

   if (gl_Normal.x > 0.5) {
      //  1.0,  0.0,  0.0
      tangent  = normalize(gl_NormalMatrix * vec3( 0.0,  0.0, -1.0));
      binormal = normalize(gl_NormalMatrix * vec3( 0.0, -1.0,  0.0));
   } else if (gl_Normal.x < -0.5) {
      // -1.0,  0.0,  0.0
      tangent  = normalize(gl_NormalMatrix * vec3( 0.0,  0.0,  1.0));
      binormal = normalize(gl_NormalMatrix * vec3( 0.0, -1.0,  0.0));
   } else if (gl_Normal.y > 0.5) {
      //  0.0,  1.0,  0.0
      tangent  = normalize(gl_NormalMatrix * vec3( 1.0,  0.0,  0.0));
      binormal = normalize(gl_NormalMatrix * vec3( 0.0,  0.0,  1.0));
   } else if (gl_Normal.y < -0.5) {
      //  0.0, -1.0,  0.0
      useCelestialSpecularMapping = 0.0;
      tangent  = normalize(gl_NormalMatrix * vec3( 1.0,  0.0,  0.0));
      binormal = normalize(gl_NormalMatrix * vec3( 0.0,  0.0,  1.0));
   } else if (gl_Normal.z > 0.5) {
      //  0.0,  0.0,  1.0
      tangent  = normalize(gl_NormalMatrix * vec3( 1.0,  0.0,  0.0));
      binormal = normalize(gl_NormalMatrix * vec3( 0.0, -1.0,  0.0));
   } else if (gl_Normal.z < -0.5) {
      //  0.0,  0.0, -1.0
      tangent  = normalize(gl_NormalMatrix * vec3(-1.0,  0.0,  0.0));
      binormal = normalize(gl_NormalMatrix * vec3( 0.0, -1.0,  0.0));
   }
   
   mat3 tbnMatrix = mat3(tangent.x, binormal.x, normal.x,
                          tangent.y, binormal.y, normal.y,
                          tangent.z, binormal.z, normal.z);
   
   viewVector = (gl_ModelViewMatrix * gl_Vertex).xyz;
   viewVector = normalize(tbnMatrix * viewVector);

   if (worldTime < 12000 || worldTime > 23250) {
      lightVector = normalize(tbnMatrix * -sunPosition);
      specMultiplier = vec4(1.0, 1.0, 1.0, 1.0);
   } else {
      lightVector = normalize(tbnMatrix * -moonPosition);
      specMultiplier = vec4(0.5, 0.5, 0.5, 0.5);
   }
   specMultiplier *= clamp(abs(float(worldTime) / 500.0 - 46.0), 0.0, 1.0) * clamp(abs(float(worldTime) / 500.0 - 24.5), 0.0, 1.0);

#endif // _ENABLE_GL_BUMP_MAPPING
#endif // _ENABLE_GL_TEXTURE_2D

#ifdef BFINDER
	if (mc_Entity.x == BFINDER_RED) {

		gl_Position.z = 0.0;
		vertColor = vec4(1.0, 0.0, 0.0, 1.0);
	} else if (mc_Entity.x == BFINDER_GREEN) {
		gl_Position.z = 0.0;
		vertColor = vec4(0.0, 1.0, 0.0, 1.0);
	} else if (mc_Entity.x == BFINDER_BLUE) {
		gl_Position.z = 0.0;
		vertColor = vec4(0.0, 0.0, 1.0, 1.0);
	} else {
		vertColor = gl_Color;
	}
#endif

   if (renderType != 0) {
      texID = float(getTextureID(gl_MultiTexCoord0.st));
   }
   else {
      texID = -1.0;
   }
   
   gl_FogFragCoord = gl_Position.z;
}

