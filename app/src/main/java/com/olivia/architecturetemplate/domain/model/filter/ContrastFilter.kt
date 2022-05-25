package com.bemily.messenger.smileme.domain.model.filter

import android.opengl.GLES20
import com.otaliastudios.cameraview.filter.BaseFilter
import com.otaliastudios.cameraview.filter.OneParameterFilter

/**
 * Bemily
 * 스마일미 관련 선명한 필터 정의
 * @author HANSANGJUN
 * @version 1.0.0
 * @since 2021-07-22
 **/
class ContrastFilter : BaseFilter(), OneParameterFilter {
    private var contrast = 1.5f
    private var contrastLocation = -1

    /**
     * Sets the current contrast adjustment.
     * 1.0: no adjustment
     * 2.0: increased contrast
     *
     * @param contrast contrast
     */
    fun setContrast(contrast: Float) {
        var contrast = contrast
        if (contrast < 1.0f) contrast = 1.0f
        if (contrast > 2.0f) contrast = 2.0f
        this.contrast = contrast
    }

    /**
     * Returns the current contrast.
     *
     * @see .setContrast
     * @return contrast
     */
    fun getContrast(): Float {
        return contrast
    }

    override fun setParameter1(value: Float) {
        // parameter is 0...1, contrast is 1...2.
        setContrast(value + 1)
    }

    override fun getParameter1(): Float {
        // parameter is 0...1, contrast is 1...2.
        return getContrast() - 1f
    }

    override fun getFragmentShader(): String {
        return FRAGMENT_SHADER
    }

    override fun onCreate(programHandle: Int) {
        super.onCreate(programHandle)
        contrastLocation = GLES20.glGetUniformLocation(programHandle, "contrast")
    }

    override fun onDestroy() {
        super.onDestroy()
        contrastLocation = -1
    }

    override fun onPreDraw(timestampUs: Long, transformMatrix: FloatArray) {
        super.onPreDraw(timestampUs, transformMatrix)
        GLES20.glUniform1f(contrastLocation, contrast)
    }

    companion object {
        private const val FRAGMENT_SHADER = ("#extension GL_OES_EGL_image_external : require\n"
                + "precision mediump float;\n"
                + "uniform samplerExternalOES sTexture;\n"
                + "uniform float contrast;\n"
                + "varying vec2 " + DEFAULT_FRAGMENT_TEXTURE_COORDINATE_NAME + ";\n"
                + "void main() {\n"
                + "  vec4 color = texture2D(sTexture, " + DEFAULT_FRAGMENT_TEXTURE_COORDINATE_NAME + ");\n"
                + "  color -= 0.5;\n"
                + "  color *= contrast;\n"
                + "  color += 0.5;\n"
                + "  gl_FragColor = color;\n"
                + "}\n")
    }
}
