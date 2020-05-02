uniform sampler2D colorMap;
varying vec4 texcoord;

void main()
{
    vec4 color = texture2D(colorMap,texcoord.rg);
    
    if (color.a < 0.6) color.a = 0.0; //previous 1.0

    //debug
    //color.a = 0.0;

	gl_FragColor = vec4(0.0,0.0,0.0,color.a);
}