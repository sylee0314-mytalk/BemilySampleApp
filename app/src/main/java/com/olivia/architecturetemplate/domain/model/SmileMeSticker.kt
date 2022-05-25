package com.olivia.architecturetemplate.domain.model

import androidx.annotation.DrawableRes
import com.olivia.architecturetemplate.R

/**
 * Bemily
 * 스마일미 스티커 Enum 정의
 * @author HANSANGJUN
 * @version 1.0.0
 * @since 2021-07-22
 **/
enum class SmileMeSticker(@DrawableRes val resId: Int) {
    LOVE_SHOT(R.drawable.smile_me_love_shot),
    PRESENT(R.drawable.smile_me_present),
    BF_LOVE(R.drawable.smile_me_bf_love),
    CHEER_LOVE(R.drawable.smile_me_cheer_love),
    LIKE_BOMB(R.drawable.smile_me_like_bomb),
    NEW_YEAR_SUNRISE(R.drawable.smile_me_new_year_sunrise),
    NEW_YEAR_BOY(R.drawable.smile_me_new_year_boy),
    NEW_YEAR_GIRL(R.drawable.smile_me_new_year_girl),
    HAPPY_NEW_YEAR(R.drawable.smile_me_happy_new_year),
    TIGER_NEW_YEAR(R.drawable.smile_me_tiger_new_year),
    FORTUNE_COME(R.drawable.smile_me_fortune_come),
    AGE_DELIVERY(R.drawable.smile_me_age_delivery),
    LEONG(R.drawable.smile_me_leong),
    LAONG(R.drawable.smile_me_laong),
    HONEY_JAM(R.drawable.smile_me_honey_jam),
    COFFEE(R.drawable.smile_me_coffee),
    YELLOW_CARD(R.drawable.smile_me_yellow_card),
    STAMP(R.drawable.smile_me_stamp),
    ARROW(R.drawable.smile_me_arrow),
    CELEBRATE(R.drawable.smile_me_celebrate),
    CHEERS(R.drawable.smile_me_cheers),
    CHEER_UP(R.drawable.smile_me_cheer_up),
    TIRED(R.drawable.smile_me_tired),
    SICK(R.drawable.smile_me_sick),
    EAT(R.drawable.smile_me_eat),
    SHOOT(R.drawable.smile_me_shoot),
    RAINY(R.drawable.smile_me_rainy),
    DIZZY(R.drawable.smile_me_dizzy),
    COMING(R.drawable.smile_me_coming),
    GOING(R.drawable.smile_me_going),
    COLD(R.drawable.smile_me_cold),
    HOT(R.drawable.smile_me_hot),
    SCARED(R.drawable.smile_me_scared),
    HI(R.drawable.smile_me_hi),
    SORRY(R.drawable.smile_me_sorry),
    OK(R.drawable.smile_me_ok),
    SURPRISE(R.drawable.smile_me_surprise),
    LAUGH(R.drawable.smile_me_laugh),
    CALL_ME(R.drawable.smile_me_call_me),
    DISTRESS(R.drawable.smile_me_distress),
    THANKS(R.drawable.smile_me_thanks),
    ANGRY(R.drawable.smile_me_angry),
    STARING(R.drawable.smile_me_staring),
    CONGRATULATION(R.drawable.smile_me_congratulation),
    QUESTION(R.drawable.smile_me_question),
    TOUCHED(R.drawable.smile_me_touched),
    PLEASE(R.drawable.smile_me_please),
    GLOOM(R.drawable.smile_me_gloom),
    NO(R.drawable.smile_me_no),
    FIGHTING(R.drawable.smile_me_fighting),
    LOVE(R.drawable.smile_me_love),
    SLEEP(R.drawable.smile_me_sleep),
    BEST(R.drawable.smile_me_best);

    companion object {
        fun getSticker(sticker: Int): SmileMeSticker {
            return values()[sticker]
        }
    }
}