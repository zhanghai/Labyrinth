package cn.edu.zju.cs.graphics.labyrinth;


import cn.edu.zju.cs.graphics.labyrinth.util.GlUtils;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengles.GLES20.*;

/**
 * Created by xuxin on 2017/1/12.
 */
public class Button extends Element {
    private float mScaleX=1;
    private float mScaleY=1;
    private boolean mPressed=false;

    Button(){};
    Button(float width, float height, float PosX, float PosY, int program){
        super( width, height, PosX, PosY, program);
    }
    boolean OnMouseDown(float mouseX, float mouseY, float Height)
    {
        mouseY = Height-mouseY;
//        if( mouseX > mPosX && mouseX < mPosX+mWidth &&
//                mouseY > mPosY && mouseY < mPosY+mHeight )
        if(true)
        {
            System.out.printf("button is pressed .... /n");
            mScaleX=1.1f;
            mScaleY=1.1f;
            mPressed = true;

            return true;
        }
        return false;
    }
    void OnMouseUp()
    {
        mScaleX=1.0f;
        mScaleY=1.0f;
        mPressed = false;
    }

    public void ModelTransform()
    {
        mModelMatrix
                .identity()
                .translate(mPosX, mPosX,0f)
                .scale(mWidth,mHeight, 1f)
                .scale(mScaleX,mScaleY,1);
    }

}
