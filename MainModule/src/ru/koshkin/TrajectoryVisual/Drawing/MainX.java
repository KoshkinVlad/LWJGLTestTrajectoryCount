package ru.koshkin.TrajectoryVisual.Drawing;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;
import ru.koshkin.TrajectoryVisual.core.*;

import org.lwjgl.LWJGLException;

import java.awt.*;
import java.nio.FloatBuffer;

public class MainX implements Drawable {

    private final double SCREEN_HEIGHT;
    private final double SCREEN_WIDTH;


    public static void main(String[] args) {
        new MainX();
    }

    public MainX() {
        CoordsCalculator calculator = new CoordsCalculator(15.5, 100, 35, this);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double maxLength = calculator.maxLength();
        double maxHeight = calculator.maxHeight();
        SCREEN_WIDTH = screenSize.getWidth();
        SCREEN_HEIGHT = screenSize.getHeight();
        try {
            DrawSomething();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
//        Thread t = new Thread(calculator);
//        t.start();
    }

    private void DrawSomething() throws LWJGLException {
        Display.setTitle("Trajectory Visualizer 0.1 alpha");
        Display.setDisplayMode(new DisplayMode((int) 800, (int) 600));
        Display.setResizable(false);
        Display.setVSyncEnabled(false);
        Display.setFullscreen(false);


        ContextAttribs attribs = new ContextAttribs(4, 3, ContextAttribs.CONTEXT_COMPATIBILITY_PROFILE_BIT_ARB);
        attribs.withForwardCompatible(true);
        attribs.withProfileCore(true);
        Display.create(new PixelFormat(), attribs);
        System.out.println("OpenGL Version: " + GL11.glGetString(GL11.GL_VERSION));
        GL11.glViewport(0, 0, 800, 600);
        float r, g, b;
        r = g = b = 0;

        float[] vertices = {
                -0.5f, -0.5f, 0.0f,
                -0.5f, 0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
        };
        int vboID = GL15.glGenBuffers();
        int vaiOD = GL30.glGenVertexArrays();
        FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
        buffer.put(vertices);
        buffer.flip();

        GL30.glBindVertexArray(vaiOD);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLAT, false, 0, 0);
        GL20.glEnableVertexAttribArray(0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);

        int error = GL11.glGetError();
        if (error != GL11.GL_NO_ERROR) {
            throw new RuntimeException("Error: " + GLU.gluErrorString(error));
        }


        while (!Display.isCloseRequested()) {
            r += 0.025f;
            g += 0.015f;
            b += 0.010f;
            GL11.glClearColor((float) Math.sin(r), (float) Math.sin(g), (float) Math.sin(b), 1.0f);
            GL11.glClearColor((float) Math.sin(r), (float) Math.sin(g), (float) Math.sin(b), 1.0f);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            GL30.glBindVertexArray(vaiOD);
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertices.length / 3);
            GL30.glBindVertexArray(0);

            if (Display.isResizable()) {
                GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
            }

            Display.sync(120);
            Display.update();
        }

        GL20.glDisableVertexAttribArray(0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
        GL15.glDeleteBuffers(vboID);
        GL30.glDeleteVertexArrays(vaiOD);
        Display.destroy();
    }


    @Override
    public void getCurrentSituation(double x, double y) {
        System.out.println(x + " " + y);
    }
}
