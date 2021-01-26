# jME3 Wireframe render library

## About

This is an java library for jme3 implementing two different wireframe rendering methods for GLES although it works on desktop GL:

* Geometry shader implementation: Using this method requires openGLES >= 3.2 and Android API level 24 (Android 7) or newer. For openGL you need any card supporting geometry shaders.
* Barycentric coordinates implementation: This method is available to any openGLES version without API level limitation.


## Geometry shader implementation details

The implementation is based on the simplest method from https://github.com/martin-pr/possumwood/wiki/Wireframe-using-a-Geometry-Shader which just changes the triangles to line strips so the fragment shader outputs lines instead

Vertex shader: Uses all current jme3 stuff at vertex shaders (morph, skinning...) being able to use this material without any aditional code.

Geometry shader: As said, just changes the triangles into line strips. 

Fragment shader: Paints the requested color. You can set Color and/or VertexColor same way you would do when using jme3's Unshaded material


## Barycentric coordinates implementation details

The mesh is expanded so you have the full list of triangles without any indexing. Then the bary coords are set as follows: v1(1,0,0), v2(0,1,0), v3(0,0,1) for each triangle. This data is stored in the normal buffer because it matches our type and size requirements and we don't need normals as we're rendering wireframe. This could cause bad behaviour with inner jme stuff like skinning (has not been tested)

Vertex shader: It works as usual calculating gl_Position but we're just keeping the normal as it is (AKA bary coord).

Fragment shader: Calculates the minimal distance to the edge and sets alpha accordingly, if distance<0.02 sets alpha>0. It uses the requested color. You can set Color and/or VertexColor same way you would do when using jme3's Unshaded material

Improved rendering to use "newer" GLSL capabilities (fwidth) so wireframe lines have static width when possible, otherwise fallback to previous implementation having worse looking.

## Future work

Not sure I'll spend more time or not into this, but...

* Implement any of the other techniques from @martin-pr's tutorial for better looking and/or more configurability.
* Implement a Mesh subclass adding a new buffer type to avoid messing with the normal buffer.


## Usage

* Download latest release and add it as library to your jme3 project
* If using gradle, just add "org.joliver82.jme3-wireframe:$version" to your dependencies

There's a sample app at https://github.com/joliver82/jME3-GLES-wireframe showing four spheres from left to right, top to bottom: jME3 default wireframe mode (will render solid on android), Barycentric coordinates approach mimicing jME3's default, Geometry shader approach and Barycentric coordinates approach mimicing geometry shader approach.

Desktop screenshot:
![Alt text](/screenshots/wireframe-desktop.png?raw=true "Desktop screenshot")

Android screenshot:
![Alt text](/screenshots/wireframe-android.png?raw=true "Android screenshot")


## References

* Designed for jMonkeyEngine 3 https://github.com/jMonkeyEngine/jmonkeyengine/
* Using Heart library from stephengold https://github.com/stephengold/Heart

Wireframe rendering on GLES references:

* https://github.com/martin-pr/possumwood/wiki/Wireframe-using-a-Geometry-Shader 
* https://github.com/rreusser/glsl-solid-wireframe
* https://www.geeks3d.com/hacklab/20180514/demo-wireframe-shader-opengl-3-2-and-opengl-es-3-1/
* https://www.reddit.com/r/opengl/comments/34dhi7/wireframe_shader/
* https://tchayen.github.io/wireframes-with-barycentric-coordinates/
* https://stackoverflow.com/questions/137629/how-do-you-render-primitives-as-wireframes-in-opengl


