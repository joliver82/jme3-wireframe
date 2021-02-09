#import "Common/ShaderLib/GLSLCompat.glsllib"
#import "Common/ShaderLib/Skinning.glsllib"
#import "Common/ShaderLib/Instancing.glsllib"
#import "Common/ShaderLib/MorphAnim.glsllib"

attribute vec3 inPosition;

attribute vec4 inColor;
attribute vec3 inNormal;

varying vec3 baryCoord;
varying vec4 vertColor;

void main(){

    #ifdef HAS_VERTEXCOLOR
        vertColor = inColor;
    #endif

    vec4 modelSpacePos = vec4(inPosition, 1.0);

    #ifdef NUM_MORPH_TARGETS
        Morph_Compute(modelSpacePos);
    #endif

    #ifdef NUM_BONES
        Skinning_Compute(modelSpacePos);
    #endif

    baryCoord = inNormal;
    gl_Position = TransformWorldViewProjection(modelSpacePos);
}
