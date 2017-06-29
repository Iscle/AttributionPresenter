package com.franmontiel.attributionhelper.listeners;

import com.franmontiel.attributionhelper.entities.Attribution;

/**
 * Interface definition for a callback to be invoked when the attribution item is clicked.
 */
public interface OnAttributionClickListener {
    /**
     * Called when the attribution item is clicked.
     * @param attribution object representing the clicked item
     * @return true if the event has been consumed, false otherwise
     */
    boolean onAttributionClick(Attribution attribution);
}
