# jME GLES Wireframe render

## About

This is an android jme3 project showing two different wireframe rendering methods GLES:

* Geometry shader implementation: Using this method requires openGLES >= 3.2 and Android API level 24 (Android 7) or newer.
* Barycentric coordinates implementation: This method is available to any openGLES version without API level limitation.

The app requires API level 24 (Android 7) or newer, this could be lowered if removing the code making use of Wireframe.j3md


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
* Implement non geometry shader wireframe rendering based on barycentric coordinates. This will imply CPU code and duplicating vertex information in memory but will support openGL ES <3.2. --> Done
* Include this info jme3 core.


## Usage

Just load the project with latest Android Studio (Tested using 4.1.2) and run 

The sample app shows four spheres from left to right, top to bottom: jME3 default wireframe mode (will render solid on android), Barycentric coordinates approach mimicing jME3's default, Geometry shader approach and Barycentric coordinates approach mimicing geometry shader approach.

Desktop screenshot:
![Alt text](/screenshots/wireframe-desktop.png?raw=true "Desktop screenshot")

Android screenshot:
![Alt text](/screenshots/wireframe-android.png?raw=true "Android screenshot")


## References

* This simple project used the jme3 template from https://github.com/noncom/jme-android-example but updated to current Android Studio and SDK. Feel free to use this as base template for your jME3 android projects
* Designed for jMonkeyEngine 3 https://github.com/jMonkeyEngine/jmonkeyengine/
* Using Heart library from stephengold https://github.com/stephengold/Heart

Wireframe rendering on GLES references:

* https://github.com/martin-pr/possumwood/wiki/Wireframe-using-a-Geometry-Shader 
* https://github.com/rreusser/glsl-solid-wireframe
* https://www.geeks3d.com/hacklab/20180514/demo-wireframe-shader-opengl-3-2-and-opengl-es-3-1/
* https://www.reddit.com/r/opengl/comments/34dhi7/wireframe_shader/
* https://tchayen.github.io/wireframes-with-barycentric-coordinates/
* https://stackoverflow.com/questions/137629/how-do-you-render-primitives-as-wireframes-in-opengl

## License

Feel free to use this the way you want but just remember to list me in the credits ;)


