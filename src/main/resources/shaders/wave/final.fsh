/*
------------------------------------------------
Note:
    - DOF won't work with other shaders
    for now. Sorry !
    20110727: This is still an issue

    - Values for SSAO and Bloom will
    differ with the Texture Pack you
    choose. You will have to tweak !

Credits:
    - azraeil ( DOF Shader )
    - Yourself ( CEL Shading / Crepuscular effect)
    - Dilla/snowcrash ( Bloom Shader )
    - Leadwerks ( SSAO Shader )
    - .jhn ( pseudo-AA Shader )
    - damajor (shaders: - thermal vision
                        - black&white dreams
                        - Lens
                        - Moving Blackhole
                        - Want pixel )
------------------------------------------------
*/

// To disable one of these features put a "//" in front of the "#define"

#define GAMMA 1.2
//#define DOF_ENABLED
#define BLOOM_ENABLED
//#define AA_ENABLED
//#define SSAO_ENABLED
#define GOD_RAYS
//#define CEL_SHADING
//#define THERMAL_VISION
//#define BW_DREAMS
//#define LENS
//#define WANT_PIXELS
//#define MOVING_BLACKHOLE

// LENS Params
#define LENS_INRAD 0.18
#define LENS_OUTRAD 0.25
// GOD_RAYS Params
#ifdef GOD_RAYS
    const float GR_DECAY    = 0.95;
    const float GR_DENSITY  = 0.5;
    const float GR_EXPOSURE = 0.2;
    const int GR_SAMPLES    = 32;
#endif
// CEL_SHADING Params
#ifdef CEL_SHADING
    const float CEL_EDGE_THRESHOLD = 0.25;
    const float CEL_EDGE_THICKNESS = 1.65;
#endif
// BLOOM Params
#ifdef BLOOM_ENABLED
    const float BLOOM_STRENGTH = 0.095;
#endif
//DOF Params
#ifdef DOF_ENABLED
    const float HYPERFOCAL = 4.0;
#endif

/*
-------------------------------------------------------
 The standard user should not mess with anything below
-------------------------------------------------------
*/

uniform sampler2D sampler0;
uniform sampler2D sampler1;
uniform sampler2D sampler2;
uniform sampler2D bgl_RenderedTexture;

uniform float displayWidth;
uniform float displayHeight;
uniform float aspectRatio;
uniform float near;
uniform float far;
uniform vec3 sunPosition;
uniform int worldTime;

const float INFINITY = 1000.0;

float getDepth( vec2 coord ) {
    float depth = texture2D( sampler1, coord ).x;
    float depth2 = texture2D( sampler2, coord ).x;
    if ( depth2 < 1.0 ) {
        depth = depth2;
    }
    if ( depth == 1.0 ) {
        return INFINITY;
    }
    depth = -near / (-1+depth * ((far-near)/far));
    //depth = 2.0 * near * far / ( far + near - ( 2.0 * depth - 1.0 ) * ( far - near ) );
    //return 2.0 * near * far / ( far + near - ( 2.0 * depth - 1.0 ) * ( far - near ) );
    return depth;
}

#ifdef GOD_RAYS
vec2 getDepthGR(vec2 coord) {
    float depth = texture2D(sampler1, coord).x;
    float depth2 = texture2D(sampler2, coord).x;
    float fg = -1.0;
    if (depth2 < 1.0) {
        depth = depth2;
        fg = 1.0;
    }
    return vec2(2.0 * near * far / (far + near - (2.0 * depth - 1.0) * (far - near)), fg);
}
#endif

#ifdef SSAO_ENABLED // Stuff only SSAO uses
    float compareDepths( in float depth1, in float depth2 ) {
        float aoCap = 1.0;
        float aoMultiplier = 5000.0;
        float depthTolerance = 0.0;
        float aorange = 512.0; // units in space the AO effect extends to (this gets divided by the camera far range
        float diff = sqrt( clamp( 1.0 - ( depth1 - depth2 ) / ( aorange / ( far - near ) ), 0.0, 1.0 ) );
        float ao = min( aoCap, max( 0.0, depth1 - depth2 - depthTolerance ) * aoMultiplier ) * diff;
        return ao;
    }

	vec4 getSSAO() {
		float depth = getDepth( gl_TexCoord[0].st );
		float d;
		
		float pw = 1.0 / displayWidth;
		float ph = 1.0 / displayHeight;
		float aoCap = 1.0;
		float ao = 0.0;
		float aoMultiplier = 5000.0;
		float depthTolerance = 0.0;
		float aoscale=1.0;

		d=getDepth( vec2(gl_TexCoord[0].x+pw,gl_TexCoord[0].y+ph));
		ao+=compareDepths(depth,d)/aoscale;

		d=getDepth( vec2(gl_TexCoord[0].x-pw,gl_TexCoord[0].y+ph));
		ao+=compareDepths(depth,d)/aoscale;

		d=getDepth( vec2(gl_TexCoord[0].x+pw,gl_TexCoord[0].y-ph));
		ao+=compareDepths(depth,d)/aoscale;

		d=getDepth( vec2(gl_TexCoord[0].x-pw,gl_TexCoord[0].y-ph));
		ao+=compareDepths(depth,d)/aoscale;
		
		pw*=2.0;
		ph*=2.0;
		aoMultiplier/=2.0;
		aoscale*=1.2;
		
		d=getDepth( vec2(gl_TexCoord[0].x+pw,gl_TexCoord[0].y+ph));
		ao+=compareDepths(depth,d)/aoscale;

		d=getDepth( vec2(gl_TexCoord[0].x-pw,gl_TexCoord[0].y+ph));
		ao+=compareDepths(depth,d)/aoscale;

		d=getDepth( vec2(gl_TexCoord[0].x+pw,gl_TexCoord[0].y-ph));
		ao+=compareDepths(depth,d)/aoscale;

		d=getDepth( vec2(gl_TexCoord[0].x-pw,gl_TexCoord[0].y-ph));
		ao+=compareDepths(depth,d)/aoscale;

		pw*=2.0;
		ph*=2.0;
		aoMultiplier/=2.0;
		aoscale*=1.2;
		
		d=getDepth( vec2(gl_TexCoord[0].x+pw,gl_TexCoord[0].y+ph));
		ao+=compareDepths(depth,d)/aoscale;

		d=getDepth( vec2(gl_TexCoord[0].x-pw,gl_TexCoord[0].y+ph));
		ao+=compareDepths(depth,d)/aoscale;

		d=getDepth( vec2(gl_TexCoord[0].x+pw,gl_TexCoord[0].y-ph));
		ao+=compareDepths(depth,d)/aoscale;

		d=getDepth( vec2(gl_TexCoord[0].x-pw,gl_TexCoord[0].y-ph));
		ao+=compareDepths(depth,d)/aoscale;
		
		pw*=2.0;
		ph*=2.0;
		aoMultiplier/=2.0;
		aoscale*=1.2;
		
		d=getDepth( vec2(gl_TexCoord[0].x+pw,gl_TexCoord[0].y+ph));
		ao+=compareDepths(depth,d)/aoscale;

		d=getDepth( vec2(gl_TexCoord[0].x-pw,gl_TexCoord[0].y+ph));
		ao+=compareDepths(depth,d)/aoscale;

		d=getDepth( vec2(gl_TexCoord[0].x+pw,gl_TexCoord[0].y-ph));
		ao+=compareDepths(depth,d)/aoscale;

		d=getDepth( vec2(gl_TexCoord[0].x-pw,gl_TexCoord[0].y-ph));
		ao+=compareDepths(depth,d)/aoscale;

		ao/=16.0;
		
		return vec4(1.1-ao);
	}
#endif

#ifdef AA_ENABLED // Stuff only AA uses
	float expDepth2linearDepth( vec2 coord ) {
		float depth = texture2D( sampler1, coord ).x;
		float depth2 = texture2D( sampler2, coord ).x;
		if ( depth2 < 1.0 ) {
			depth = depth2;
		}
		if ( depth == 1.0 ) {
			return INFINITY;
		}
		return ( 2.0 * near ) / ( far + near - depth * ( far - near ) );
	}

	vec4 getAA() {
		vec4 newColor = vec4(0.0);

		float depth = expDepth2linearDepth(gl_TexCoord[0].xy);
		vec2 aspectCorrection = vec2(1.0, aspectRatio) * 0.005;
		float offsetx = 0.12 * aspectCorrection.x;
		float offsety = 0.12 * aspectCorrection.y;
		float depthThreshold=0.01;

		if ( abs( depth - expDepth2linearDepth( gl_TexCoord[0].xy + vec2( offsetx, 0 ) ) ) > depthThreshold || abs( depth - expDepth2linearDepth( gl_TexCoord[0].xy + vec2( -offsetx, 0 ) ) ) > depthThreshold || abs( depth - expDepth2linearDepth( gl_TexCoord[0].xy + vec2( 0, offsety ) ) ) > depthThreshold || abs( depth - expDepth2linearDepth( gl_TexCoord[0].xy + vec2( 0, -offsety ) ) ) > depthThreshold ) {
			newColor += texture2D(sampler0, gl_TexCoord[0].st + vec2(-offsetx, offsety));
			newColor += texture2D(sampler0, gl_TexCoord[0].st + vec2(0, offsety));
			newColor += texture2D(sampler0, gl_TexCoord[0].st + vec2(offsetx, offsety));
			newColor += texture2D(sampler0, gl_TexCoord[0].st + vec2(-offsetx, 0));
			newColor += texture2D(sampler0, gl_TexCoord[0].st);
			newColor += texture2D(sampler0, gl_TexCoord[0].st + vec2(offsetx, 0));
			newColor += texture2D(sampler0, gl_TexCoord[0].st + vec2(-offsetx, -offsety));
			newColor += texture2D(sampler0, gl_TexCoord[0].st + vec2(0, -offsety));
			newColor += texture2D(sampler0, gl_TexCoord[0].st + vec2(offsetx, -offsety));
			newColor /= 9.0;
		} else
			newColor=texture2D(sampler0, gl_TexCoord[0].st);

		return newColor;
	}
#endif

#ifdef BLOOM_ENABLED // Stuff only bloom uses
	vec4 getBloom() {
		vec4 sum = vec4(0);
		vec2 texcoord = vec2(gl_TexCoord[0]);
		for( int i = -3; i < 3; i++) { // Lower loop count for performance
			for ( int j = -3; j < 3; j++ ) {
				sum += texture2D( bgl_RenderedTexture, texcoord + vec2( j, i ) * 0.004 ) * BLOOM_STRENGTH;
			}
		}
		if (texture2D(bgl_RenderedTexture, texcoord).r < 0.3) {
			sum = sum*sum*0.012 + texture2D(bgl_RenderedTexture, texcoord);
		} else {
			if (texture2D(bgl_RenderedTexture, texcoord).r < 0.5) {
				sum = sum*sum*0.009 + texture2D(bgl_RenderedTexture, texcoord);
			} else {
				sum = sum*sum*0.0075 + texture2D(bgl_RenderedTexture, texcoord);
			}
		}
		return sum;
	}
#endif

#ifdef DOF_ENABLED // Stuff only DOF uses
	float samples = float( 0 );
	vec2 space;

	float getCursorDepth( vec2 coord ) {
		return 2.0 * near * far / ( far + near - ( 2.0 * texture2D( sampler1, coord ).x - 1.0 ) * ( far - near ) );
	}

	vec4 getSampleWithBoundsCheck(vec2 offset) {
		vec2 coord = gl_TexCoord[0].st + offset;
		if (coord.s <= 1.0 && coord.s >= 0.0 && coord.t <= 1.0 && coord.t >= 0.0) {
			samples += 1.0;
			return texture2D(sampler0, coord);
		} else {
			return vec4(0.0);
		}
	}

	vec4 getBlurredColor() {
		vec4 blurredColor = vec4( 0.0 );
		float depth = getDepth( gl_TexCoord[0].xy );
		vec2 aspectCorrection = vec2( 1.0, aspectRatio ) * 0.005;

		vec2 ac0_4 = 0.4 * aspectCorrection;	// 0.4
		vec2 ac0_29 = 0.29 * aspectCorrection;	// 0.29
		vec2 ac0_15 = 0.15 * aspectCorrection;	// 0.15
		vec2 ac0_37 = 0.37 * aspectCorrection;	// 0.37
		
		vec2 lowSpace = gl_TexCoord[0].st;
		vec2 highSpace = 1.0 - lowSpace;
		space = vec2( min( lowSpace.s, highSpace.s ), min( lowSpace.t, highSpace.t ) );
			
		if (space.s >= ac0_4.s && space.t >= ac0_4.t) {

			blurredColor += texture2D(sampler0, gl_TexCoord[0].st + vec2(0.0, ac0_4.t));
			blurredColor += texture2D(sampler0, gl_TexCoord[0].st + vec2(ac0_4.s, 0.0));   
			blurredColor += texture2D(sampler0, gl_TexCoord[0].st + vec2(0.0, -ac0_4.t)); 
			blurredColor += texture2D(sampler0, gl_TexCoord[0].st + vec2(-ac0_4.s, 0.0)); 
			blurredColor += texture2D(sampler0, gl_TexCoord[0].st + vec2(ac0_29.s, -ac0_29.t));
			blurredColor += texture2D(sampler0, gl_TexCoord[0].st + vec2(ac0_29.s, ac0_29.t));
			blurredColor += texture2D(sampler0, gl_TexCoord[0].st + vec2(-ac0_29.s, ac0_29.t));
			blurredColor += texture2D(sampler0, gl_TexCoord[0].st + vec2(-ac0_29.s, -ac0_29.t));
			blurredColor += texture2D(sampler0, gl_TexCoord[0].st + vec2(ac0_15.s, ac0_37.t));
			blurredColor += texture2D(sampler0, gl_TexCoord[0].st + vec2(-ac0_37.s, ac0_15.t));
			blurredColor += texture2D(sampler0, gl_TexCoord[0].st + vec2(ac0_37.s, -ac0_15.t));
			blurredColor += texture2D(sampler0, gl_TexCoord[0].st + vec2(-ac0_15.s, -ac0_37.t));
			blurredColor += texture2D(sampler0, gl_TexCoord[0].st + vec2(-ac0_15.s, ac0_37.t));
			blurredColor += texture2D(sampler0, gl_TexCoord[0].st + vec2(ac0_37.s, ac0_15.t)); 
			blurredColor += texture2D(sampler0, gl_TexCoord[0].st + vec2(-ac0_37.s, -ac0_15.t));
			blurredColor += texture2D(sampler0, gl_TexCoord[0].st + vec2(ac0_15.s, -ac0_37.t));
			blurredColor /= 16.0;
			
		} else {
			
			blurredColor += getSampleWithBoundsCheck(vec2(0.0, ac0_4.t));
			blurredColor += getSampleWithBoundsCheck(vec2(ac0_4.s, 0.0));   
			blurredColor += getSampleWithBoundsCheck(vec2(0.0, -ac0_4.t)); 
			blurredColor += getSampleWithBoundsCheck(vec2(-ac0_4.s, 0.0)); 
			blurredColor += getSampleWithBoundsCheck(vec2(ac0_29.s, -ac0_29.t));
			blurredColor += getSampleWithBoundsCheck(vec2(ac0_29.s, ac0_29.t));
			blurredColor += getSampleWithBoundsCheck(vec2(-ac0_29.s, ac0_29.t));
			blurredColor += getSampleWithBoundsCheck(vec2(-ac0_29.s, -ac0_29.t));
			blurredColor += getSampleWithBoundsCheck(vec2(ac0_15.s, ac0_37.t));
			blurredColor += getSampleWithBoundsCheck(vec2(-ac0_37.s, ac0_15.t));
			blurredColor += getSampleWithBoundsCheck(vec2(ac0_37.s, -ac0_15.t));
			blurredColor += getSampleWithBoundsCheck(vec2(-ac0_15.s, -ac0_37.t));
			blurredColor += getSampleWithBoundsCheck(vec2(-ac0_15.s, ac0_37.t));
			blurredColor += getSampleWithBoundsCheck(vec2(ac0_37.s, ac0_15.t)); 
			blurredColor += getSampleWithBoundsCheck(vec2(-ac0_37.s, -ac0_15.t));
			blurredColor += getSampleWithBoundsCheck(vec2(ac0_15.s, -ac0_37.t));
			blurredColor /= samples;
			
		}

		return blurredColor;
	}
#endif

#ifdef CEL_SHADING
	float getEdgeDepth(vec2 coord) {
		float depth = texture2D( sampler1, coord ).x;
		float depth2 = texture2D( sampler2, coord ).x;
		if ( depth2 < 1.0 ) {
			depth = depth2;
		}
	   
		if ( depth == 1.0 ) {
			return INFINITY;
		}
	   
		return depth * near / ( far + ( near - far ) * depth );
	}

	vec4 edgeDetect( vec2 coord ) {
		vec2 o11 = vec2(1.0, aspectRatio)*CEL_EDGE_THICKNESS/displayWidth;
		vec4 color = vec4(0.0);
	 
		float depth = getEdgeDepth(coord);
		float avg = 0.0;
		float laplace = 24.0 * depth;
		float sample;
		int n = 0;
	   
		if (depth < INFINITY) {
			avg += depth;
			++n;
		}
	 
		for (int i = -2; i <= 2; ++i) {
			for (int j = -2; j <= 2; ++j) {
				if (i != 0 || j != 0) {
					sample = getEdgeDepth(coord + vec2(float( i ) * o11.s, float( j ) * o11.t));
					laplace -= sample;
					if (sample < INFINITY) {
						++n;
						avg += sample;
					}
				}
			}
		}

		avg = clamp( avg/ float( n ), 0.0, 1.0);

		if ( laplace > avg * CEL_EDGE_THRESHOLD ) {
			color.rgb = mix( vec3( 0.0 ), gl_Fog.color.rgb, 0.75 * avg * avg);
			color.a = 1.0;
		}

		return color;
	}
#endif

void main() {
    vec4 baseColor;

#ifdef AA_ENABLED
	baseColor = getAA();
#else
	baseColor = texture2D( sampler0, gl_TexCoord[0].st );
#endif

#ifdef SSAO_ENABLED
	vec4 getSSAOdodge = getSSAO();
	baseColor *= getSSAOdodge;
#endif

#ifdef BLOOM_ENABLED
	baseColor = mix( baseColor, getBloom(), 0.5 );
#endif

/* Here we add a second pass to SSAO that will only apply if
   the user has enabled Bloom. The reason for this, is because
   the way it's done, Bloom actually mix the render so far
   with an altered ORIGINAL game frame.
   This means that the SSAO will get partly overwriten.
*/

#ifdef SSAO_ENABLED
	#ifdef BLOOM_ENABLED
		baseColor *= getSSAOdodge;
	#endif
#endif

#ifdef THERMAL_VISION
    vec2 uv = gl_TexCoord[0].xy;
    vec3 pixcol = baseColor.rgb;
    vec3 colors[3];
    colors[0] = vec3(0.,0.,1.);
    colors[1] = vec3(1.,1.,0.);
    colors[2] = vec3(1.,0.,0.);
//    float lum = (pixcol.r+pixcol.g+pixcol.b)/3.;
    float lum = dot(vec3(0.30, 0.59, 0.11), pixcol.rgb);
    int ix = (lum < 0.5)? 0:1;
    vec3 tc = mix(colors[ix],colors[ix+1],(lum-float(ix)*0.5)/0.5);
    baseColor = vec4(tc, 1.0);
#endif

#ifdef GOD_RAYS
	float threshold = 0.99 * far;
	bool foreground = false;
	vec2 depthGR = getDepthGR(gl_TexCoord[0].st);

    if ((worldTime < 14000 || worldTime > 22000) && sunPosition.z < 0 && depthGR.x < threshold && depthGR.y < 0.0)
	{
		vec2 lightPos = sunPosition.xy / -sunPosition.z;
		lightPos.y *= aspectRatio; 
		lightPos = (lightPos + 1.0)/2.0;
		vec2 texCoord = gl_TexCoord[0].st;
		vec2 delta = (texCoord - lightPos) * GR_DENSITY / float(GR_SAMPLES);
		float decay = -sunPosition.z / 100.0;
		
		vec3 color = vec3(0.0);
		
		for (int i = 0; i < GR_SAMPLES; i++)
		{
			texCoord -= delta;
			if (texCoord.x < 0.0 || texCoord.x > 1.0) {
				if (texCoord.y < 0.0 || texCoord.y > 1.0) {
					break;
				}
			}
			vec3 sample = vec3(0.0);
			if (getDepthGR(texCoord).x > threshold) sample = texture2D(sampler0, texCoord).rgb;
			sample *= decay;
			if (distance(texCoord, lightPos) > 0.05) sample *= 0.2;
			color += sample;
			decay *= GR_DECAY;
		}
		baseColor = (baseColor + GR_EXPOSURE * vec4(color, 0.0));
	}
#endif

#ifdef CEL_SHADING
    vec4 outlineColor = edgeDetect( gl_TexCoord[0].st );
    if (outlineColor.a != 0.0) {
        baseColor.rgb = outlineColor.rgb;
    }
#endif

#ifdef DOF_ENABLED
    float depth = getDepth( gl_TexCoord[0].st );
    float cursorDepth = getCursorDepth( vec2( 0.5, 0.5 ) );
    if ( depth < cursorDepth )
        baseColor = mix( baseColor, getBlurredColor(), clamp(2.0 * ((clamp(cursorDepth, 0.0, HYPERFOCAL) - depth) / (clamp(cursorDepth, 0.0, HYPERFOCAL))), 0.0, 1.0));
    else
        baseColor = mix( baseColor, getBlurredColor(), 1.0 - clamp( ( ( ( cursorDepth * HYPERFOCAL ) / ( HYPERFOCAL - cursorDepth ) ) - ( depth - cursorDepth ) ) / ((cursorDepth * HYPERFOCAL) / (HYPERFOCAL - cursorDepth)), 0.0, 1.0));
#endif

#ifdef GAMMA
    if (baseColor[3] == 0.0) {
        baseColor = gl_Fog.color;
    }
    else {
        baseColor.rgb = pow(baseColor.rgb, vec3(1.0/GAMMA));
    }
#endif

#ifdef BW_DREAMS
    vec2 uv3 = gl_TexCoord[0].xy;
    vec4 c = baseColor;
    c += texture2D(sampler0, uv3+0.001);
    c += texture2D(sampler0, uv3+0.003);
    c += texture2D(sampler0, uv3+0.005);
    c += texture2D(sampler0, uv3+0.007);
//    c += texture2D(sampler0, uv3+0.009);
//    c += texture2D(sampler0, uv3+0.011);
    c += texture2D(sampler0, uv3-0.001);
    c += texture2D(sampler0, uv3-0.003);
    c += texture2D(sampler0, uv3-0.005);
    c += texture2D(sampler0, uv3-0.007);
//    c += texture2D(sampler0, uv3-0.009);
//    c += texture2D(sampler0, uv3-0.011);
    c.rgb = vec3((c.r+c.g+c.b)/3.0);
    c = c / 9.5;
    baseColor = c;
#endif

#ifdef LENS
    vec2 lensRadius = vec2(LENS_OUTRAD, LENS_INRAD);
    vec4 Color = baseColor;
    float dist = distance(gl_TexCoord[0].xy, vec2(0.5,0.5));
    Color.rgb *= smoothstep(lensRadius.x, lensRadius.y, dist);
    baseColor = Color;
#endif

#ifdef WANT_PIXELS
    const float rt_w = 1600.0;
    const float rt_h = 1600.0;
    const float pixel_w = 8.0; // 15.0
    const float pixel_h = 4.0; // 10.0

    vec2 uv2 = gl_TexCoord[0].xy;
    float dx = pixel_w*(1./rt_w);
    float dy = pixel_h*(1./rt_h);
    vec2 coord = vec2(dx*floor(uv2.x/dx),
                      dy*floor(uv2.y/dy));
    vec3 tc = texture2D(sampler0, coord).rgb;
    baseColor = vec4(tc, 1.0);
#endif

#ifdef MOVING_BLACKHOLE
    vec2 cen = vec2(0.5,0.5) - gl_TexCoord[0].xy;
    vec2 mcen = - // delete minus for implosion effect
                0.07*log(length(cen))*normalize(cen);
//    gl_FragColor = texture2D(sampler0, gl_TexCoord[0].xy+mcen);
    baseColor *= texture2D(sampler0, gl_TexCoord[0].xy+mcen);
#endif
    gl_FragColor = baseColor;

}
