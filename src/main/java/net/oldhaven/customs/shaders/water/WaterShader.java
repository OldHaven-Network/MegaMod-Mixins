package net.oldhaven.customs.shaders.water;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GLContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class WaterShader {
    private int vertex_shader = 0;
    private int fragment_shader = 0;
    private int program = 0;
    private FloatBuffer matrix = BufferUtils.createFloatBuffer(16);
    private boolean disable_error_msg = true;
    private boolean debug = true;
    private ArrayList value_ids = new ArrayList();
    private boolean pre_opengl20 = false;

    WaterShader(String path) {
        String vert_path = path + ".vert";
        String frag_path = path + ".frag";
        String line = "";
        String vertex_shader_source = "";
        URI uri = null;

        try {
            uri = new URI("file://" + WaterShader.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        } catch (URISyntaxException var12) {
            throw new RuntimeException("URI fault");
        }

        File f = new File(uri);

        BufferedReader reader;
        try {
            for(reader = new BufferedReader(new FileReader(f.getParent() + vert_path)); (line = reader.readLine()) != null; vertex_shader_source = vertex_shader_source + line + "\n") {
            }
        } catch (Exception var14) {
            throw new RuntimeException("Couldnt load shader! Was looking for: " + f.getParent() + vert_path + " \nReason: " + var14);
        }

        String version = GL11.glGetString(7938);
        if (Integer.valueOf(version.substring(0, 1)) < 2) {
            this.pre_opengl20 = true;
            if (!GLContext.getCapabilities().GL_ARB_shader_objects) {
                throw new RuntimeException("Pre OpenGL 2.0 without GL_ARB_shader_objects support.");
            }

            if (!GLContext.getCapabilities().GL_ARB_vertex_shader) {
                throw new RuntimeException("Pre OpenGL 2.0 without GL_ARB_vertex_shader support.");
            }

            if (!GLContext.getCapabilities().GL_ARB_fragment_shader) {
                throw new RuntimeException("Pre OpenGL 2.0 without GL_ARB_vertex_shader support.");
            }
        }

        if (!this.pre_opengl20) {
            this.vertex_shader = this.compile(35633, vertex_shader_source);
        } else {
            this.vertex_shader = this.compilePre20(35633, vertex_shader_source);
        }

        if (this.vertex_shader == 0) {
            System.out.println("Error Vertex Shader!");
        }

        String fragment_shader_source = "";

        try {
            for(reader = new BufferedReader(new FileReader(f.getParent() + frag_path)); (line = reader.readLine()) != null; fragment_shader_source = fragment_shader_source + line + "\n") {
            }
        } catch (Exception var13) {
            var13.printStackTrace();
        }

        System.out.println("Compiling " + vert_path);
        if (!this.pre_opengl20) {
            this.fragment_shader = this.compile(35632, fragment_shader_source);
        } else {
            this.fragment_shader = this.compilePre20(35632, fragment_shader_source);
        }

        if (this.fragment_shader == 0) {
            System.out.println("Error Fragment Shader!");
        }

        if (!this.pre_opengl20) {
            this.link(path);
        } else {
            this.linkPre20(path);
        }

    }

    private int compilePre20(int type, String shader_source) {
        int shader = ARBShaderObjects.glCreateShaderObjectARB(type);
        if (shader == 0) {
            System.out.println("Impossible to create shader object!");
            return 0;
        } else {
            ARBShaderObjects.glShaderSourceARB(shader, shader_source);
            ARBShaderObjects.glCompileShaderARB(shader);
            return shader;
        }
    }

    private int compile(int type, String shader_source) {
        int shader = GL20.glCreateShader(type);
        if (shader == 0) {
            System.out.println("Impossible to create shader object!");
            return 0;
        } else {
            GL20.glShaderSource(shader, shader_source);
            GL20.glCompileShader(shader);
            return shader;
        }
    }

    private void linkPre20(String source) {
        this.program = ARBShaderObjects.glCreateProgramObjectARB();
        ARBShaderObjects.glAttachObjectARB(this.program, this.vertex_shader);
        ARBShaderObjects.glAttachObjectARB(this.program, this.fragment_shader);
        ARBShaderObjects.glLinkProgramARB(this.program);
        IntBuffer result = BufferUtils.createIntBuffer(1);
        ARBShaderObjects.glGetObjectParameterARB(this.program, 35714, result);
        if (result.get(0) != 1) {
            throw new RuntimeException("Shader compile error! \n\nin: " + source + "\n\n" + ARBShaderObjects.glGetInfoLogARB(this.program, 5000));
        } else {
            System.out.println(ARBShaderObjects.glGetInfoLogARB(this.program, 5000));
        }
    }

    private void link(String source) {
        this.program = GL20.glCreateProgram();
        GL20.glAttachShader(this.program, this.vertex_shader);
        GL20.glAttachShader(this.program, this.fragment_shader);
        GL20.glLinkProgram(this.program);
        IntBuffer result = BufferUtils.createIntBuffer(1);
        GL20.glGetProgram(this.program, 35714, result);
        if (result.get(0) != 1) {
            throw new RuntimeException("Shader compile error! \n\nin: " + source + "\n\n" + GL20.glGetProgramInfoLog(this.program, 5000));
        } else {
            System.out.println(GL20.glGetProgramInfoLog(this.program, 5000));
        }
    }

    public void bind() {
        if (!this.pre_opengl20) {
            GL20.glUseProgram(this.program);
        } else {
            ARBShaderObjects.glUseProgramObjectARB(this.program);
        }

    }

    public void unbind() {
        if (!this.pre_opengl20) {
            GL20.glUseProgram(0);
        } else {
            ARBShaderObjects.glUseProgramObjectARB(0);
        }

    }

    public int initValue1i(String name) {
        int location;
        if (!this.pre_opengl20) {
            location = GL20.glGetUniformLocation(this.program, name);
        } else {
            location = ARBShaderObjects.glGetUniformLocationARB(this.program, name);
        }

        if (!this.disable_error_msg && location == -1) {
            System.out.println("Error binding shader int.");
        }

        this.value_ids.add(location);
        if (this.debug) {
            System.out.println("Added: " + name + " at " + (this.value_ids.size() - 1) + " location: " + location);
        }

        return this.value_ids.size() - 1;
    }

    public void setValue1i(int position_in_location_array, int value) {
        if (!this.pre_opengl20) {
            GL20.glUniform1i((Integer)this.value_ids.get(position_in_location_array), value);
        } else {
            ARBShaderObjects.glUniform1iARB((Integer)this.value_ids.get(position_in_location_array), value);
        }

    }

    public int initValue1f(String name) {
        int location;
        if (!this.pre_opengl20) {
            location = GL20.glGetUniformLocation(this.program, name);
        } else {
            location = ARBShaderObjects.glGetUniformLocationARB(this.program, name);
        }

        if (!this.disable_error_msg && location == -1) {
            System.out.println("Error binding shader float.");
        }

        this.value_ids.add(location);
        if (this.debug) {
            System.out.println("Added: " + name + " at " + (this.value_ids.size() - 1) + " location: " + location);
        }

        return this.value_ids.size() - 1;
    }

    public void setValue1f(int position_in_location_array, float value) {
        if (!this.pre_opengl20) {
            GL20.glUniform1f((Integer)this.value_ids.get(position_in_location_array), value);
        } else {
            ARBShaderObjects.glUniform1fARB((Integer)this.value_ids.get(position_in_location_array), value);
        }

    }

    public int initValueVec3f(String name) {
        int location;
        if (!this.pre_opengl20) {
            location = GL20.glGetUniformLocation(this.program, name);
        } else {
            location = ARBShaderObjects.glGetUniformLocationARB(this.program, name);
        }

        if (!this.disable_error_msg && location == -1) {
            System.out.println("Error binding shader vec3.");
        }

        this.value_ids.add(location);
        if (this.debug) {
            System.out.println("Added: " + name + " at " + (this.value_ids.size() - 1) + " location: " + location);
        }

        return this.value_ids.size() - 1;
    }

    public void setValueVec3f(int position_in_location_array, float x, float y, float z) {
        if (!this.pre_opengl20) {
            GL20.glUniform3f((Integer)this.value_ids.get(position_in_location_array), x, y, z);
        } else {
            ARBShaderObjects.glUniform3fARB((Integer)this.value_ids.get(position_in_location_array), x, y, z);
        }

    }

    public int initValueMat4f(String name) {
        int location;
        if (!this.pre_opengl20) {
            location = GL20.glGetUniformLocation(this.program, name);
        } else {
            location = ARBShaderObjects.glGetUniformLocationARB(this.program, name);
        }

        if (!this.disable_error_msg && location == -1) {
            System.out.println("Error binding shader matrix.");
        }

        this.value_ids.add(location);
        if (this.debug) {
            System.out.println("Added: " + name + " at " + (this.value_ids.size() - 1) + " location: " + location);
        }

        return this.value_ids.size() - 1;
    }

    public void setValueMat4f(int position_in_location_array, float[] value) {
        this.matrix.clear();

        for(int i = 0; i < 16; ++i) {
            this.matrix.put(value[i]);
        }

        this.matrix.flip();
        if (!this.pre_opengl20) {
            GL20.glUniformMatrix4((Integer)this.value_ids.get(position_in_location_array), true, this.matrix);
        } else {
            ARBShaderObjects.glUniformMatrix4ARB((Integer)this.value_ids.get(position_in_location_array), true, this.matrix);
        }

    }
}

