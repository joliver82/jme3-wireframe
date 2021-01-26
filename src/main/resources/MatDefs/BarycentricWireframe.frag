#ifdef GL_ES
    #extension GL_OES_standard_derivatives : enable
#endif

#import "Common/ShaderLib/GLSLCompat.glsllib"

uniform vec4 m_Color;

varying vec3 baryCoord;
varying vec4 vertColor;

void main(){
    vec4 color = vec4(1.0);

    #ifdef HAS_VERTEXCOLOR
        color *= vertColor;
    #endif

    #ifdef HAS_COLOR
        color *= m_Color;
    #endif

    float alpha = 0.0;

    // Barycentric calc for GLESSL not supporting OES_standard_derivatives (mostly unlikely, 99% GLES devices support it)
    // All those versions not supporting fwidth
    #if defined GL_ES && __VERSION__ < 300 && ! defined GL_OES_standard_derivatives
        float closest_edge = min(baryCoord.x, min(baryCoord.y, baryCoord.z));
        // Should smoothstep be parametrized to define line width somehow?
        alpha=smoothstep(0.97, 1.0, 1.0-closest_edge);

    // Barycentric for Desktop GL, GLESSL>=300 or any GLESSL having OES_standard_derivatives
    #else
        // Should parametrize line width?
        float width=0.5;
        float half_width=width*0.5;
        vec3 fw = fwidth(baryCoord);
        vec3 a3 = smoothstep(fw * (width - half_width), fw * (width + half_width), baryCoord);
        alpha = 1.0-min(a3.x, min(a3.y, a3.z));
    #endif

    gl_FragColor = vec4(color.rgb, alpha);
}