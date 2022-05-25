package com.bemily.messenger.smileme.domain.model.filter

import android.opengl.GLES20
import com.otaliastudios.cameraview.filter.BaseFilter
import com.otaliastudios.cameraview.filter.OneParameterFilter

/**
 * Bemily
 * 스마일미 관련 따뜻한 필터 정의
 * @author HANSANGJUN
 * @version 1.0.0
 * @since 2021-07-22
 **/
class WarmFilter : BaseFilter(), OneParameterFilter {
    private var scale = 0.5f // -1...1
    private var scaleLocation = -1
    /**
     * Returns the current temperature.
     *
     * @see .setTemperature
     * @return temperature
     */
    /**
     * Sets the new temperature value:
     * -1.0: cool colors
     * 0.0: no change
     * 1.0: warm colors
     *
     * @param value new value
     */
    var temperature: Float
        get() = scale
        set(value) {
            var value = value
            if (value < -1f) value = -1f
            if (value > 1f) value = 1f
            scale = value
        }

    override fun setParameter1(value: Float) {
        temperature = 2f * value - 1f
    }

    override fun getParameter1(): Float {
        return (temperature + 1f) / 2f
    }

    override fun getFragmentShader(): String {
        return FRAGMENT_SHADER
    }

    override fun onCreate(programHandle: Int) {
        super.onCreate(programHandle)
        scaleLocation = GLES20.glGetUniformLocation(programHandle, "scale")
    }

    override fun onDestroy() {
        super.onDestroy()
        scaleLocation = -1
    }

    override fun onPreDraw(timestampUs: Long, transformMatrix: FloatArray) {
        super.onPreDraw(timestampUs, transformMatrix)
        GLES20.glUniform1f(scaleLocation, scale)
    }

    companion object {
        private const val FRAGMENT_SHADER = ("#extension GL_OES_EGL_image_external : require\n"
                + "precision mediump float;\n"
                + "uniform samplerExternalOES sTexture;\n"
                + "uniform float scale;\n"
                + "varying vec2 " + DEFAULT_FRAGMENT_TEXTURE_COORDINATE_NAME + ";\n"
                + "void main() {\n"
                + "  vec4 color = texture2D(sTexture, " + DEFAULT_FRAGMENT_TEXTURE_COORDINATE_NAME + ");\n"
                + "  vec3 new_color = color.rgb;\n"
                + "  new_color.r = color.r + color.r * ( 1.0 - color.r) * scale;\n"
                + "  new_color.b = color.b - color.b * ( 1.0 - color.b) * scale;\n"
                + "  if (scale > 0.0) { \n"
                + "    new_color.g = color.g + color.g * ( 1.0 - color.g) * scale * 0.25;\n"
                + "  }\n"
                + "  float max_value = max(new_color.r, max(new_color.g, new_color.b));\n"
                + "  if (max_value > 1.0) { \n"
                + "     new_color /= max_value;\n"
                + "  } \n"
                + "  gl_FragColor = vec4(new_color, color.a);\n"
                + "}\n")
    }
}
