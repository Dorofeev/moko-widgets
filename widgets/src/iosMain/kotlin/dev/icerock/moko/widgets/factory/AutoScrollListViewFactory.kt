/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.ListWidget
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.bind
import platform.Foundation.NSIndexPath
import platform.UIKit.UITableView
import platform.UIKit.UITableViewScrollPosition
import platform.UIKit.layoutIfNeeded
import platform.UIKit.subviews

actual class AutoScrollListViewFactory actual constructor(
    private val listViewFactory: ViewFactory<ListWidget<out WidgetSize>>,
    private val isAlwaysAutoScroll: Boolean
) : ViewFactory<ListWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: ListWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val bundle = listViewFactory.build(widget, size, viewFactoryContext)
        val view = bundle.view

        val tableView: UITableView = findTableView(view)
            ?: throw ClassCastException("UITableView was not found in the View hierarchy and could not be cast.")

        var isAutoScrolled = false

        widget.items.bind { units ->
            if ((!isAutoScrolled || isAlwaysAutoScroll) && units.isNotEmpty()) {
                tableView.reloadData()
                view.layoutIfNeeded()

                val indexPath = NSIndexPath.indexPathWithIndex((units.size - 1).toULong())

                tableView.scrollToRowAtIndexPath(
                    indexPath = indexPath,
                    atScrollPosition = UITableViewScrollPosition.UITableViewScrollPositionBottom,
                    animated = true
                )

                isAutoScrolled = true
            }
        }

        return bundle
    }

    private fun findTableView(view: View): UITableView? {
        return when (view) {
            is UITableView -> view
            else -> {
                var tableView: UITableView? = null

                view.subviews.forEach {
                    val subview = it as View

                    if (subview is UITableView) {
                        tableView = subview
                    }
                }

                return tableView
            }
        }
    }
}