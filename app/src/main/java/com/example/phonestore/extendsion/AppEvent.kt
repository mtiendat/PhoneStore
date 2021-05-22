package com.example.phonestore.extendsion

import com.example.phonestore.model.PopUp
import java.util.concurrent.CopyOnWriteArraySet

object AppEvent {
    private var notifyCode: Set<NotifyCode> = CopyOnWriteArraySet()
    //Start Account Suspended
    private var accountSuspendedListeners: Set<AccountSuspendedListener> = CopyOnWriteArraySet<AccountSuspendedListener>()
    fun onAccountSuspended() {
        accountSuspendedListeners.forEach { it.onSuspendedAccount() }
    }
    // Popup
    private var popupEventListeners: Set<PopupEventListener> = CopyOnWriteArraySet()

    fun addPopupEventListener(listener: PopupEventListener?) {
        if (listener != null)
            popupEventListeners = popupEventListeners.plus(listener)
    }

    fun unRegisterPopupEventListener(listener: PopupEventListener?) {
        popupEventListeners = popupEventListeners.minus(listener ?: return)
    }

    fun notifyShowPopUp(popup: PopUp? = null) {
        for (listener in popupEventListeners)
            listener.onShowPopup(popup)
    }

    fun notifyClosePopUp() {
        for (listener in popupEventListeners)
            listener.onClosePopup()
    }
    //Start Account Not Activated
    private var accountListener: Set<AccountListener> = CopyOnWriteArraySet<AccountListener>()

    fun registerAccountNotActivatedListener(listener: AccountListener?) {
        if (listener != null)
            accountListener = accountListener.plus(listener)
    }

    fun unRegisterAccountNotActivatedListener(listener: AccountListener?) {
        accountListener = accountListener.minus(listener ?: return)
    }

    fun onAccountNotActivated() {
        accountListener.forEach { it.onAccountNotActivated() }
    }
    fun notifyCode(code: Any) {
        for (listener in notifyCode)
            listener.notifyCode(code)
    }
}
interface PopupEventListener {
    fun onShowPopup(popup: PopUp?)
    fun onClosePopup()
}
interface AccountSuspendedListener {
    fun onSuspendedAccount()
}
interface AccountListener {
    fun onAccountNotActivated()
}
interface NotifyCode {
    fun notifyCode(code: Any)
}