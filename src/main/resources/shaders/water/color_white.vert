varying vec4 texcoord; 
varying vec4 position;
uniform mat4 inverse_view;
//uniform vec3 view_vector;

void main()
{
    texcoord = gl_MultiTexCoord0;

    //waterfall transparency disabled
    //position = inverse_view * gl_ModelViewMatrix * gl_Vertex;
    
    //always 0.0/-1.0/0.0 (why??? off is the value!)
    //if (gl_Normal.y >= -0.1 && gl_Normal.y <= 0.1) position.y = 0.1;
    //else position.y = 1.0;
    
    if (gl_Normal.y <= -0.99) {
        //position.y = 0.0;
        //result: depending on view vector all water is bright or dark
        //position.y = dot(gl_Normal,normalize(view_vector));
        //alternative
        //(comment: normally vectors are transformed differently)
        //should not depend on the view direction!
        //vec4 tr_normal = gl_ModelViewMatrix * vec4(gl_Normal, 0.0);
        //position.y = 0.1 + 1.0-dot(normalize(tr_normal),vec4(0.0, 0.0, -1.0, 0.0));
        
        position = inverse_view * gl_ModelViewMatrix * gl_Vertex;
        
        //distance dependent?
        float root = sqrt(position.x*position.x + position.z*position.z);
        position.y = 0.1 + sqrt(sqrt(root))/2.5; //3.0
        
    } else {
        position.y = 0.1 + gl_Normal.y*-1.0; //pointing up is negative
    }
    
    position.x = gl_Vertex.x;
    position.z = gl_Vertex.z;
    
    if (gl_Normal.y > 0.9) {
        position.y = 0.00;
    }
    
    //debug
    //gl_Position = gl_ModelViewMatrix * gl_Vertex;
    //gl_Position = inverse_view * gl_Position;
    //gl_Position = gl_ProjectionMatrix * gl_Position;
    
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}