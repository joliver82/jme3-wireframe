/*
 * Copyright (c) 2021 Jesus Oliver
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of the copyright holder nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.joliver82.jme3utils.wireframe;

import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;

import java.nio.FloatBuffer;

import jme3utilities.MyMesh;

/**
 * Helper class to simplify te setting of barycentric coordinates for wireframe rendering supporing any GLES version
 *
 */ 
public class BarycentricCoordGenerator {

    /**
     * Calculates barycentric coordinates and stores them in the Reserved0 buffer
     * so we don't need to overwrite the normal buffer
     *
     * Note the Reserved0 vertexbuffer type (previously known as MiscAttrib) has been deprecated for
     * long and could be removed. Still exists in jme 3.3.x
     *
     * If this method don't work fall back to normal approach
     *
     * The mesh needs to be in triangle mode, oherwise nothing is done
     *
     * @param mesh
     */
    public static Mesh setBarycentricCoords(Mesh mesh)
    {
        Mesh outMesh=mesh;

        if(mesh.getMode()==Mesh.Mode.Triangles)
        {
            outMesh=MyMesh.expand(mesh);

            FloatBuffer baryBuffer = BufferUtils.createFloatBuffer(outMesh.getVertexCount()*3);
            outMesh.setBuffer(VertexBuffer.Type.Reserved0, 3, baryBuffer);

            for (int triIndex = 0; triIndex < outMesh.getTriangleCount(); ++triIndex) {
                //vertex1 barycoord
                baryBuffer.put(1);
                baryBuffer.put(0);
                baryBuffer.put(0);

                //vertex2 barycoord
                baryBuffer.put(0);
                baryBuffer.put(1);
                baryBuffer.put(0);

                //vertex2 barycoord
                baryBuffer.put(0);
                baryBuffer.put(0);
                baryBuffer.put(1);
            }
            baryBuffer.flip();
        }

        return outMesh;
    }

    /**
     * Calculates barycentric coordinates and stores them in the normal buffer
     * so we don't need to mess with inner jme3 stuff
     *
     * The mesh needs to be in triangle mode, oherwise nothing is done
     *
     * @param mesh
     */
    public static Mesh setBarycentricCoordsOverNormals(Mesh mesh)
    {
        Mesh outMesh=mesh;

        if(mesh.getMode()==Mesh.Mode.Triangles)
        {
            outMesh=MyMesh.expand(mesh);

            FloatBuffer normalBuffer = BufferUtils.createFloatBuffer(outMesh.getVertexCount()*3);
            outMesh.setBuffer(VertexBuffer.Type.Normal, 3, normalBuffer);

            for (int triIndex = 0; triIndex < outMesh.getTriangleCount(); ++triIndex) {
                //vertex1 barycoord
                normalBuffer.put(1);
                normalBuffer.put(0);
                normalBuffer.put(0);

                //vertex2 barycoord
                normalBuffer.put(0);
                normalBuffer.put(1);
                normalBuffer.put(0);

                //vertex2 barycoord
                normalBuffer.put(0);
                normalBuffer.put(0);
                normalBuffer.put(1);
            }
            normalBuffer.flip();
        }

        return outMesh;
    }
}
