/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import platform.UIKit.UIViewController

actual abstract class BottomNavigationScreen actual constructor(
    private val screenFactory: ScreenFactory
) : Screen<Args.Empty>() {
    actual abstract val items: List<BottomNavigationItem>

    override fun createViewController(): UIViewController {
        return BottomNavigationViewController(this, screenFactory).apply {
        }
    }

    actual var selectedItemIndex: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
}

