//for water transparency modification

uniform sampler2D colorMap;
uniform float water_transparency;
uniform float waterfall_transparency;
varying vec4 texcoord;
varying vec4 vert_color;
varying float is_vertical;
varying float fogFactor;

//0.1666
//float water_transparency = 0.1; //.2, 0.12
//float waterfall_transparency = 0.25; //.2

void main()
{
    //water_transparency = 1.0; //.2, 0.12

    vec4 color = texture2D(colorMap,texcoord.rg);
    vec4 out_color = vec4(1.0);
    float alpha = color.a;
    //-1 at surface
    //0 at waterfall
    if (is_vertical <= 0.2) {
    
        //0.0666
        //for surface 1.0, for waterfall 2.0
        //new: for surf 1.0, for waterf 1.5 (is_vertical: surf = -1.0, wf = 0.0)
        //just interpolate btw waterfall and standard..
        alpha = (-is_vertical) * water_transparency + (1.0+is_vertical) * waterfall_transparency;
        //alpha = (1.5 + is_vertical/2.0) * water_transparency;// * (2.0*color.r + 2.0*color.g + 2.0*color.b - 1.0);
        
        //reduce color term to the right (reduces to 0 for surfaces!)
        
        //great at day color.r * (2.0 + is_vertical) * (1.0+is_vertical)*(color.r + color.g + color.b); 
        //sad at night!!
        
        //at day: 1.0 * color * 2.0 * 1.0*(sumcol)
        //planar: 1.0 * color * 1.0 * 0.0 (dang)
        //at night: low * color * 2.0 * 1.0*(sumcol) //too bright
        //Should not affect planar ones
        
        //what is wanted?
        //different alpha and color for planar and flowing
        //linear interpolation between (needs is_vertical: -1 at planar 0 at waterfall)
        
        //at day: 2.0 * color * (sumcolor)
        
        //reduce for planar?
        //1.0 + vert_color
        color.r = color.r * (2.0 + vert_color.r);// * (color.r + color.g + color.b); 
        color.g = color.g * (2.0 + vert_color.g);// * (color.r + color.g + color.b);
        color.b = color.b * (2.0 + vert_color.b);// * (color.r + color.g + color.b);
        
        //color.r = color.r * (2.0 + is_vertical) * (1.0+is_vertical)*(color.r + color.g + color.b);  //great one
        //color.rgb = (2.0*color.r + 2.0*color.g + 2.0*color.b)/4.5 + color.rgb*0.8; ///4.5
    
        //probably close to 0 at night
        //1 at day?
        
        //color.r = is_vertical;
        //color.g = 0.0;
        //color.b = 0.0;
        
        //gl_FragColor = vec4(color.r, color.g, color.b, alpha);
    
        out_color = vec4( vert_color.r*color.r, 
                    vert_color.g*color.g, 
                    vert_color.b*color.b, alpha);
                            
        
    
    } else {
    
        out_color = vec4(vert_color.r*color.r, vert_color.g*color.g, vert_color.b*color.b, alpha);
    
    }
    
    gl_FragColor = out_color;
    
    if (is_vertical > -0.9) {
        gl_FragColor = mix(gl_Fog.color, out_color, fogFactor); //1.3 * tested
        
        //if (fogFactor >= 0.0) gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);
        
    }
	
}