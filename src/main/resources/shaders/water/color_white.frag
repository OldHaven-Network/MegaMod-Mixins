uniform sampler2D colorMap;
varying vec4 texcoord;
varying vec4 position; //save position dependend parameters (in object space?)
//uniform float water_height;

void main()
{
	
	//positions for larger tiled textures for water effects
	//not really necessary since it is rendered in chunks
	float x = mod(position.x,10.0)/10.0; //used to be 5.0
	float z = mod(position.z,10.0)/10.0;
	
	float visible = 0.0;
	
	//waterfall disabled
	//visible = (-((position.y+water_height)/10.0)); //div 50.0 //values found through trial and error //62.7, 64.7
	//if (visible < 0.0) visible = 0.0; //truncate
	//if (visible > 1.0) visible = 1.0; 
	
	//if (visible != 0.0)
	//   visible = 1.01 - visible; //auto truncate
	//else
	//   visible = 0.01; //this value is taken for water that will be rendered without reflection!
	
	//debug
	//visible = 1.0;
	visible = position.y;
	
	//saves x position parameter
	gl_FragColor = vec4(visible, x, z, 1.0);
}