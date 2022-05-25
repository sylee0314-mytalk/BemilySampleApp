package com.bemily.messenger.smileme.domain.model

import com.bemily.messenger.smileme.domain.model.filter.ContrastFilter
import com.bemily.messenger.smileme.domain.model.filter.CoolFilter
import com.bemily.messenger.smileme.domain.model.filter.WarmFilter
import com.otaliastudios.cameraview.filter.Filter
import com.otaliastudios.cameraview.filter.NoFilter
import com.otaliastudios.cameraview.filters.BlackAndWhiteFilter
import com.otaliastudios.cameraview.filters.SepiaFilter


/**
 * Bemily
 * 스마일미 필터 Enum 정의
 * @author HANSANGJUN
 * @version 1.0.0
 * @since 2021-07-22
 **/
enum class SmileMeFilter(private val filterClass: Class<out Filter?>) {
    NONE(NoFilter::class.java),
    CONTRAST(ContrastFilter::class.java),
    MONO(BlackAndWhiteFilter::class.java),
    SEPIA(SepiaFilter::class.java),
    WARM(WarmFilter::class.java),
    COOL(CoolFilter::class.java);

    fun newInstance(): Filter {
        return try {
            filterClass.newInstance()!!
        } catch (e: IllegalAccessException) {
            NoFilter()
        } catch (e: InstantiationException) {
            NoFilter()
        }
    }

    companion object {
        fun getFilter(filter: Int): SmileMeFilter {
            return values()[filter]
        }
    }

}