#import "Common/ShaderLib/GLSLCompat.glsllib"

uniform vec4 m_Color;

varying vec4 vertColor;

void main(){
    vec4 color = vec4(1.0);

    #ifdef HAS_VERTEXCOLOR
        color *= vertColor;
    #endif

    #ifdef HAS_COLOR
        color *= m_Color;
    #endif

    gl_FragColor = color;
}