package com.volodymyrkozlov.marketplace.handler.listener;

/**
 * Represents a generic message listener interface.
 *
 * @param <T> The type of the message to be listened to.
 */
interface MessageListener<T> {

    /**
     * Called when a message is received.
     *
     * @param message The message received.
     */
    void onMessage(T message);
}
