//#version 120

uniform float timer;
uniform sampler2D colorMap;
uniform sampler2D reflectedColorMap;
uniform sampler2D stencilMap;
uniform vec3 water_color;
varying vec4 texcoord;

void main ()
{
    //vec3 water_color = vec3(0.0, 0.4, 0.2); //0.22,0.2 (now uniform through config)
    float in_water; //'boolean'

    vec4 color;
    vec4 stencil = texture2D(stencilMap,texcoord.rg);
    
    if (stencil.r > 0.05) in_water = 1.0;
    else in_water = 0.0;
    
    //distortion begin
    float x_scale = 1.0;
    float z_scale = 1.0;
    
    float used_timer = timer;
    float time_scale = 2.0; //2.0
    float size_scale = 1.6*6.3; //also dependent on radius
    
    if (stencil.r <= 0.15) {
        size_scale *= 6.0;
        time_scale *= 1.5;
    } else {
        size_scale *= stencil.r;
    }

    //timer needs to be 'in period'
    if (stencil.r >= 0.5) { //
        x_scale = ( 0.995 + (sin(2.0*time_scale*3.14159*used_timer - sin(0.5*size_scale*3.14159*stencil.g) + (size_scale*3.14159*stencil.g))/100.0)); //scales btw 0.995 and 1.005
    } //- sin(0.5*size_scale*3.14159*stencil.b)
    z_scale = ( 0.995 + (sin(sin(time_scale*3.14159*used_timer)  + 1.5*sin(0.8*size_scale*3.14159*stencil.b))/150.0));
    vec2 disturbed = vec2(x_scale*texcoord.x, z_scale*texcoord.y);
    
    vec4 reflection = texture2D(reflectedColorMap,disturbed.rg);
    //if (x_scale + z_scale > 2.00099) reflection *= 1.8; //to monitor effects...!
    
    time_scale = 3.0; //2.0
    size_scale = 2.4*6.3 * stencil.r;
    
    //timer needs to be 'in period'
    if (stencil.r >= 0.5) { //- sin(0.25*size_scale*3.14159*stencil.g)
        x_scale = ( 0.995 + (sin(2.0*time_scale*3.14159*used_timer - sin(0.25*size_scale*3.14159*stencil.g) + size_scale*3.14159*stencil.g)/100.0)); //scales btw 0.995 and 1.005
    }
    z_scale = ( 0.995 + (sin(sin(time_scale*3.14159*used_timer) + 1.5*sin(size_scale*3.14159*stencil.b))/100.0));
    vec2 disturbed_2 = vec2(x_scale*texcoord.x, z_scale*texcoord.y);
    //distortion end
    
    vec4 reflection_2 = texture2D(reflectedColorMap,disturbed_2.rg);
    
        
    reflection = (reflection+reflection_2)/2.0;
    
    //'refraction'(for all under-water)
    if (in_water > 0.05) {
        float look_up_range = 0.008; //0.005 //0.008
        //costs performance! (masking to avoid outside water look-ups, alternative another scene clipping)
        if (texture2D(stencilMap,vec2(disturbed.r + look_up_range, disturbed.g + look_up_range)).r > 0.001 &&
            texture2D(stencilMap,vec2(disturbed.r - look_up_range, disturbed.g - look_up_range)).r > 0.001 &&
            texture2D(stencilMap,vec2(disturbed.r, disturbed.g)).r > 0.001) {
            color = texture2D(colorMap,disturbed.rg); //drunken effect without stencil if
        } else {
            color = texture2D(colorMap,texcoord.rg);
            //waterfall test
            //if (stencil.r < 0.1) {
            //    color = vec4(1.0);
            //}
        }
        //color *= 0.666; //darken under water //removed!
    } else {
        color = texture2D(colorMap,texcoord.rg);
    }
    
    //combine reflection and scene at water surfaces
    float reflection_strength = 0.5 * (stencil.r-0.1); //0.1, 0.14, 0.16, 0.17
    float disable_refl = stencil.r-0.1;
    
    if (disable_refl <= 0.0) disable_refl = 0.0;
    
    //times inverted color.r for a stronger reflection in darker water parts!
    //used to be 8.0, 6.0, 3.5
    vec3 reflection_color = vec3(1.0, 1.0, 1.0);
    reflection_color =  reflection_strength * disable_refl * reflection.rgb;// * reflection.rgb * in_water * (1.0-(color.r*color.g*color.b));
    
    //more color in darker water in relation to the reflection
    //color darkened
    float difference = (reflection_color.r+reflection_color.g+reflection_color.b)/3.0 - (color.r + color.g + color.b)/5.5;
    if (difference < 0.0) difference = 0.0;
    vec3 regular_color = color.rgb * (1.0-in_water*reflection_strength) + (in_water * ((difference) * water_color));
    
    vec4 out_color = vec4(regular_color, 1.0) + vec4(reflection_color, 1.0);
    
    gl_FragColor = out_color;
    
    //debug section
    //gl_FragColor = texture2D(colorMap,texcoord.rg);
    //gl_FragColor = stencil;
    //gl_FragColor = vec4(vec3(stencil.r), 1.0);
    
}