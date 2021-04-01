package be.hogent.faith.faith

import be.hogent.faith.faith.models.SecurityProperties
import java.util.concurrent.TimeUnit

import org.springframework.web.util.WebUtils

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.servlet.http.Cookie

import javax.servlet.http.HttpServletResponse

import javax.servlet.http.HttpServletRequest


@Service
class CookieService @Autowired constructor(var httpServletRequest: HttpServletRequest, var httpServletResponse:HttpServletResponse, var restSecProps:SecurityProperties) {

    fun getCookie(name: String?): Cookie? {
        return WebUtils.getCookie(httpServletRequest, name!!)
    }

    fun setCookie(name: String?, value: String?, expiryInDays: Int) {
        val expiresInSeconds = TimeUnit.DAYS.toSeconds(expiryInDays.toLong()).toInt()
        val cookie = Cookie(name, value)
//        cookie.secure = restSecProps.cookieProps!!.secure
//        cookie.path = restSecProps.cookieProps!!.path
//        cookie.domain = restSecProps.cookieProps!!.domain
        cookie.maxAge = expiresInSeconds
        httpServletResponse.addCookie(cookie)
    }

    fun setSecureCookie(name: String?, value: String?, expiryInDays: Int) {
        val expiresInSeconds = TimeUnit.DAYS.toSeconds(expiryInDays.toLong()).toInt()
        val cookie = Cookie(name, value)
//        cookie.isHttpOnly = restSecProps.cookieProps!!.httpOnly
//        cookie.secure = restSecProps.cookieProps!!.secure
//        cookie.path = restSecProps.cookieProps!!.path
//        cookie.domain = restSecProps.cookieProps!!.domain
        cookie.maxAge = expiresInSeconds
        httpServletResponse.addCookie(cookie)
    }

    fun setSecureCookie(name: String?, value: String?) {
        val expiresInMinutes: Int = restSecProps.cookieProps!!.maxAgeInMinutes
        setSecureCookie(name, value, expiresInMinutes)
    }

    fun deleteSecureCookie(name: String?) {
        val expiresInSeconds = 0
        val cookie = Cookie(name, null)
//        cookie.isHttpOnly = restSecProps.cookieProps!!.httpOnly
//        cookie.secure = restSecProps.cookieProps!!.secure
//        cookie.path = restSecProps.cookieProps!!.path
//        cookie.domain = restSecProps.cookieProps!!.domain
        cookie.maxAge = expiresInSeconds
        httpServletResponse.addCookie(cookie)
    }

    fun deleteCookie(name: String?) {
        val expiresInSeconds = 0
        val cookie = Cookie(name, null)
//        cookie.path = restSecProps.cookieProps!!.path
//        cookie.domain = restSecProps.cookieProps!!.domain
        cookie.maxAge = expiresInSeconds
        httpServletResponse.addCookie(cookie)
    }
}