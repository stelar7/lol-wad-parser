package viewer.rendering.shaders;


import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Program implements AutoCloseable
{
    int id;
    
    public Program()
    {
        id = glCreateProgram();
    }
    
    public void attach(Shader shader)
    {
        glAttachShader(id, shader.getId());
    }
    
    public void bindVertLocation(String name, int loc)
    {
        glBindAttribLocation(id, loc, name);
    }
    
    public void bindFragLocation(String name, int loc)
    {
        glBindFragDataLocation(id, 0, name);
    }
    
    public void link()
    {
        glLinkProgram(id);
        
        int status = glGetShaderi(id, GL_LINK_STATUS);
        if (status != GL_TRUE)
        {
            String log = glGetProgramInfoLog(id);
            System.err.println(log);
        }
    }
    
    public void bind()
    {
        glUseProgram(id);
    }
    
    public void unbind()
    {
        glUseProgram(0);
    }
    
    @Override
    public void close()
    {
        glDeleteProgram(id);
    }
    
    public int getId()
    {
        return id;
    }
}
