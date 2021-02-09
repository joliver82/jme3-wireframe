# jME3 Wireframe render library

## About

This is an java library for jme3 implementing two different wireframe rendering methods for GLES although it works on desktop GL:

* Geometry shader implementation: Using this method requires openGLES >= 3.2 and Android API level 24 (Android 7) or newer. For openGL you need any card supporting geometry shaders.
* Barycentric coordinates implementation: This method is available to any openGLES version without API level limitation.

## Implementation details

### Geometry shader implementation details

The implementation is based on the simplest method from https://github.com/martin-pr/possumwood/wiki/Wireframe-using-a-Geometry-Shader which just changes the triangles to line strips so the fragment shader outputs lines instead

Vertex shader: Uses all current jme3 stuff at vertex shaders (morph, skinning...) being able to use this material without any aditional code.

Geometry shader: As said, just changes the triangles into line strips. 

Fragment shader: Paints the requested color. You can set Color and/or VertexColor same way you would do when using jme3's Unshaded material


### Barycentric coordinates implementation details

The mesh is expanded so you have the full list of triangles without any indexing. Then the bary coords are set as follows: v1(1,0,0), v2(0,1,0), v3(0,0,1) for each triangle. This data is stored in a deprecated buffer (Reserved0) that hasn't been removed from jme3 for ages, so I'm assuming it's safe enough to use it right now. Also there's a method and a material to use the normal buffer instead because we don't need normals as we're rendering wireframe although this could cause bad behaviour with inner jme stuff like skinning (has not been tested).

Vertex shader: It works as usual calculating gl_Position but we're just keeping the reserved or normal as it is (AKA bary coord).

Fragment shader: Calculates the minimal distance to the edge and sets alpha accordingly, if distance<0.02 sets alpha>0. It uses the requested color. You can set Color and/or VertexColor same way you would do when using jme3's Unshaded material

Improved rendering to use "newer" GLSL capabilities (fwidth) so wireframe lines have static width when possible, otherwise fallback to previous implementation having worse looking.

## Future work

Not sure I'll spend more time or not into this, but...

* Implement any of the other techniques from @martin-pr's tutorial for better looking and/or more configurability.
* Implement a Mesh subclass adding a new buffer type to avoid messing with the normal buffer.


## Usage

Include it in your project either:

* Download latest release and add it as library to your jme3 project in the IDE you're using
* If using gradle, just add jitpack.io repository and add implementation 'com.github.joliver82:jme3-wireframe:1.0.2' to your dependencies

### Geometry approach

This is the default and easier to include, just set your spatial to use the wireframe material (MatDefs/Wireframe.j3md) and set the desired color as you would do for Unshaded.j3md

### Barycentric coordinate

You need to make your mesh store the barycentric coordinates by running 
```
Mesh newMesh = BarycentricCoordGenerator.setBarycentricCoords(currentMesh);
```
Add the new mesh to the desired spatial instead and set it to use "MatDefs/BarycentricWireframe.j3md" material. Same as default wireframe material you can set the desired color

Please note that this expands the mesh removing indexing and creates a normal buffer having the barycentric coordinates so some inner jme3 functionalities (like animation, skinning or any other) could fail. 

### Sample app

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


