package com.gub.app

object Const {

    // Base URL for both API server and video stream (can be localhost or a remote IP)
    private const val BASE_URL = "localhost"
    // private const val BASE_URL = "35.240.216.70" // Uncomment for production or remote server

    // Server host for API requests
    private const val DEFAULT_SERVER_URL = BASE_URL

    // Host for video stream services (HTTP and WebSocket)
    private const val DEFAULT_STREAM_HOST = BASE_URL

    // Port used by the Ktor API server
    private const val SERVER_PORT = 8080

    // Port used for streaming video (e.g., from a camera server)
    private const val STREAM_PORT = 1234

    // Full URL to communicate with the backend API (e.g., "localhost:8080")
    const val SERVER_URL = "$DEFAULT_SERVER_URL:$SERVER_PORT"

    // Full HTTP URL to access the video stream (e.g., "http://localhost:1234")
    const val STREAM_URL = "http://$DEFAULT_STREAM_HOST:$STREAM_PORT"

    // Full WebSocket URL to receive live stream frames (e.g., "ws://localhost:1234/ws")
    const val STREAM_WEBSOCKET_URL = "ws://$DEFAULT_STREAM_HOST:$STREAM_PORT/ws"
}